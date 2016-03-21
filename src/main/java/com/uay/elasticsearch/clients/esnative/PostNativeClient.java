package com.uay.elasticsearch.clients.esnative;

import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.EsFactory;
import com.uay.elasticsearch.model.Post;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class PostNativeClient {

    private static final Logger logger = LoggerFactory.getLogger(PostNativeClient.class);
    @Autowired
    private EsFactory esFactory;

    public void save(List<Post> posts) {
        Client client = esFactory.getClient();

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

    private IndexRequestBuilder constructIndexRequestBuilder(Client client, Post post) {
        try {
            return client
                    .prepareIndex(EsConstants.INDEX, EsConstants.TYPE)
                    .setSource(jsonBuilder()
                                    .startObject()
                                    .field("date", post.getDate())
                                    .field("author", post.getAuthor())
                                    .field("body", post.getBody())
                                    .field("title", post.getTitle())
                                    .field("keywords", post.getKeywords())
                                    .endObject()
                    );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
