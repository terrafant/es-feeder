package com.uay.elasticsearch.clients.rest;

import static com.uay.elasticsearch.EsConstants.INDEX_TYPE_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PostRestClient {

    @Autowired
    private RestTemplate restTemplate;

    private Gson gson = new GsonBuilder()
            .setDateFormat(EsConstants.DATE_FORMAT)
            .create();

    public void save(List<Post> posts) {
        restTemplate.postForLocation(INDEX_TYPE_URL + "/_bulk", constructBulkRequest(posts));
    }

    /**
     * Constructs String for bulk request like:
     * {"index":{}}
     * {"author":"Anton Udovychenko", "title":"title", "body":"body", "keywords":["keyword"]...}
     * @param posts
     * @return
     */
    private String constructBulkRequest(List<Post> posts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Post post: posts) {
            stringBuilder.append("{\"index\":{}}");
            stringBuilder.append("\n");
            stringBuilder.append(gson.toJson(post));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
