package com.uay.elasticsearch.clients.jest;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.JsonQueryHolder;
import com.uay.elasticsearch.clients.PostClient;
import com.uay.elasticsearch.model.Post;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostJestClient implements PostClient{

    private static final Logger logger = LoggerFactory.getLogger(PostJestClient.class);

    private JestClient client;

    @PostConstruct
    public void setup() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                        .Builder("http://" + EsConstants.HOST_NAME + ":" + EsConstants.REST_PORT)
                        .multiThreaded(true)
                        .build()
        );
        client = factory.getObject();
    }

    @PreDestroy
    public void cleanUp() {
        client.shutdownClient();
    }

    @Override
    public void save(List<Post> posts) {
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(EsConstants.INDEX)
                .defaultType(EsConstants.TYPE)
                .addAction(constructPostIndices(posts))
                .build();
        try {
            client.execute(bulk);
            logger.info("Data was successfully imported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> searchWithInSituAnalyzer(String query) {
        return search(JsonQueryHolder.constructSearchRequestWithInSituAnalyzer(query));
    }

    @Override
    public List<Post> fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(JsonQueryHolder.constructFuzzySearchRequestWithKeywordFilter(query, keyword));
    }

    private List<Post> search(String jsonQuery) {
        Search search = new Search.Builder(jsonQuery)
                .addIndex(EsConstants.INDEX)
                .addType(EsConstants.TYPE)
                .build();
        try {
            SearchResult searchResult = client.execute(search);
            List<SearchResult.Hit<Post, Void>> hits = searchResult.getHits(Post.class);
            return hits.stream()
                    .map(hit -> hit.source)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Index> constructPostIndices(List<Post> posts) {
        return posts.stream()
                .map(post -> new Index.Builder(post).build())
                .collect(Collectors.toList());
    }
}
