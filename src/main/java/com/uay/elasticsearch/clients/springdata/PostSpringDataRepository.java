package com.uay.elasticsearch.clients.springdata;

import com.uay.elasticsearch.model.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSpringDataRepository extends ElasticsearchRepository<Post, String> {
}
