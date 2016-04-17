package com.uay.elasticsearch.clients.springdata;

import com.uay.elasticsearch.model.Blogpost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogpostSpringDataRepository extends ElasticsearchRepository<Blogpost, String> {
}
