package com.uay.elasticsearch.clients.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uay.elasticsearch.EsConstants;
import com.uay.elasticsearch.clients.JsonQueryHolder;
import com.uay.elasticsearch.clients.BlogpostClient;
import com.uay.elasticsearch.model.Blogpost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.uay.elasticsearch.EsConstants.INDEX_TYPE_URL;

@Service(EsConstants.BLOGPOST_CLIENT)
@Profile("rest")
public class BlogpostRestClient implements BlogpostClient {

    @Autowired
    private RestTemplate restTemplate;

    private final Gson gson = new GsonBuilder()
            .setDateFormat(EsConstants.DATE_FORMAT)
            .create();

    @Override
    public List<Blogpost> searchQuery(String query) {
        return search(JsonQueryHolder.constructQuerySearchRequest(query));
    }

    @Override
    public List<Blogpost> searchWithInSituAnalyzer(String query) {
        return search(JsonQueryHolder.constructSearchRequestWithInSituAnalyzer(query));
    }

    @Override
    public List<Blogpost>  fuzzySearchWithKeywordFilter(String query, String keyword) {
        return search(JsonQueryHolder.constructFuzzySearchRequestWithKeywordFilter(query, keyword));
    }

    @Override
    public void save(List<Blogpost> blogposts) {
        restTemplate.postForLocation(INDEX_TYPE_URL + "/_bulk", constructBulkRequest(blogposts));
    }

    private List<Blogpost> search(String jsonQuery) {
        Map map = restTemplate.postForObject(INDEX_TYPE_URL + "/_search", jsonQuery, Map.class);
        return transformToPosts(map);
    }

    private List<Blogpost> transformToPosts(Map<String, Map<String, List<Map<String, Map<String, Object>>>>> map) {
        List<Map<String, Map<String, Object>>> hits = map.get("hits").get("hits");
        return hits.stream()
                .map(hit -> new Blogpost(hit.get("_source")))
                .collect(Collectors.toList());
    }

    /**
     * Constructs String for bulk request like:
     * {"index":{}}
     * {"author":"Anton Udovychenko", "title":"title", "body":"body", "keywords":["keyword"]...}
     * @param blogposts
     * @return
     */
    private String constructBulkRequest(List<Blogpost> blogposts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Blogpost blogpost : blogposts) {
            stringBuilder.append("{\"index\":{}}");
            stringBuilder.append("\n");
            stringBuilder.append(gson.toJson(blogpost));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
