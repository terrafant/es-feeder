package com.uay.elasticsearch.clients.jest;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.JsonQueryHolder;
import com.uay.elasticsearch.clients.BlogpostClient;
import com.uay.elasticsearch.model.Blogpost;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service(EsConstants.BLOGPOST_CLIENT)
@Profile("jest")
public class BlogpostJestClient implements BlogpostClient {

    private static final Logger logger = LoggerFactory.getLogger(BlogpostJestClient.class);

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
    public List<Blogpost> searchQuery(String query) {
        return search(JsonQueryHolder.constructQuerySearchRequest(query));
    }

    @Override
    public List<Blogpost> searchWithInSituAnalyzer(String query) {
        return search(JsonQueryHolder.constructSearchRequestWithInSituAnalyzer(query));
    }

    @Override
    public List<Blogpost> fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(JsonQueryHolder.constructFuzzySearchRequestWithKeywordFilter(query, keyword));
    }

    @Override
    public void save(List<Blogpost> blogposts) {
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(EsConstants.INDEX)
                .defaultType(EsConstants.TYPE)
                .addAction(constructPostIndices(blogposts))
                .build();
        try {
            client.execute(bulk);
            logger.info("Data was successfully imported");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Blogpost> search(String jsonQuery) {
        Search search = new Search.Builder(jsonQuery)
                .addIndex(EsConstants.INDEX)
                .addType(EsConstants.TYPE)
                .build();
        try {
            SearchResult searchResult = client.execute(search);
            List<SearchResult.Hit<Blogpost, Void>> hits = searchResult.getHits(Blogpost.class);
            return hits.stream()
                    .map(hit -> hit.source)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Index> constructPostIndices(List<Blogpost> blogposts) {
        return blogposts.stream()
                .map(post -> new Index.Builder(post).build())
                .collect(Collectors.toList());
    }
}
