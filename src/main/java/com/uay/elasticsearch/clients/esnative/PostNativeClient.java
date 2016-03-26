package com.uay.elasticsearch.clients.esnative;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.PostClient;
import com.uay.elasticsearch.model.Post;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.uay.elasticsearch.model.Post.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class PostNativeClient implements PostClient {

    private static final Logger logger = LoggerFactory.getLogger(PostNativeClient.class);

    @Autowired
    private Client client;

    @Override
    public void save(List<Post> posts) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        posts.stream()
                .forEach(post -> bulkRequest.add(constructIndexRequestBuilder(client, post)));

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        logger.info("Data was successfully imported");
        logger.debug("Bulk request took: " + bulkResponse.getTook());
        if (bulkResponse.hasFailures()) {
            logger.error("Failure in bulkResponse = " + bulkResponse);
        }
    }

    @Override
    public List<Post> searchWithInSituAnalyzer(String query) {
        return search(QueryBuilders.matchPhraseQuery(BODY_FIELD, query));
    }

    @Override
    public List<Post> fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(QueryBuilders.boolQuery()
                .must(QueryBuilders.fuzzyQuery(BODY_FIELD, query).fuzziness(Fuzziness.AUTO))
                .filter(QueryBuilders.termQuery(KEYWORDS_FIELD, keyword))
        );
    }

    private List<Post> search(QueryBuilder queryBuilder) {
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(EsConstants.INDEX)
                .setTypes(EsConstants.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .addHighlightedField(BODY_FIELD);

        logger.debug("ElasticSearch Query using Java Client API:\n " + searchRequestBuilder.internalBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        return transformToPosts(searchResponse);
    }

    private List<Post> transformToPosts(SearchResponse searchResponse) {
        List<Post> posts = new ArrayList<>();
        for(SearchHit hit: searchResponse.getHits().getHits()) {
            posts.add(new Post(hit.getSource()));
        }
        return posts;
    }

    private IndexRequestBuilder constructIndexRequestBuilder(Client client, Post post) {
        try {
            return client
                    .prepareIndex(EsConstants.INDEX, EsConstants.TYPE)
                    .setSource(jsonBuilder()
                                    .startObject()
                                    .field(DATE_FIELD, post.getDate())
                                    .field(AUTHOR_FIELD, post.getAuthor())
                                    .field(BODY_FIELD, post.getBody())
                                    .field(TITLE_FIELD, post.getTitle())
                                    .field(KEYWORDS_FIELD, post.getKeywords())
                                    .endObject()
                    );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
