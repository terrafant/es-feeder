package com.uay.elasticsearch.clients;

import com.uay.elasticsearch.model.Blogpost;

import java.util.List;

public interface BlogpostClient {

    void save(List<Blogpost> blogposts);

    List<Blogpost> searchQuery(String query);

    List<Blogpost> searchWithInSituAnalyzer(String query);

    List<Blogpost> fuzzySearchWithKeywordFilter(String query, String keyword);
}
