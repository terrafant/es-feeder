package com.uay.elasticsearch.clients;

import com.uay.elasticsearch.model.Post;

import java.util.List;

public interface PostClient {

    void save(List<Post> posts);

    List<Post> searchWithInSituAnalyzer(String query);

    List<Post> fuzzySearchWithKeywordFilter(String query, String keyword);
}
