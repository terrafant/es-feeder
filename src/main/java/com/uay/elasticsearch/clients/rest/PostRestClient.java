package com.uay.elasticsearch.clients.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.JsonQueryHolder;
import com.uay.elasticsearch.clients.PostClient;
import com.uay.elasticsearch.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uay.elasticsearch.EsConstants.INDEX_TYPE_URL;

@Service
public class PostRestClient implements PostClient {

    @Autowired
    private RestTemplate restTemplate;

    private final Gson gson = new GsonBuilder()
            .setDateFormat(EsConstants.DATE_FORMAT)
            .create();

    @Override
    public void save(List<Post> posts) {
        restTemplate.postForLocation(INDEX_TYPE_URL + "/_bulk", constructBulkRequest(posts));
    }

    @Override
    public List<Post> searchWithInSituAnalyzer(String query) {
        return search(JsonQueryHolder.constructSearchRequestWithInSituAnalyzer(query));
    }

    @Override
    public List<Post>  fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(JsonQueryHolder.constructFuzzySearchRequestWithKeywordFilter(query, keyword));
    }

    public List<Post> search(String jsonQuery) {
        Map map = restTemplate.postForObject(INDEX_TYPE_URL + "/_search", jsonQuery, Map.class);
        return transformToPosts(map);
    }

    private List<Post> transformToPosts(Map<String, Map<String, List<Map<String, Map<String, Object>>>>> map) {
        List<Map<String, Map<String, Object>>> hits = map.get("hits").get("hits");
        return hits.stream()
                .map(hit -> new Post(hit.get("_source")))
                .collect(Collectors.toList());
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
