package com.uay.configuration;

import com.uay.elasticsearch.EsFactory;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.uay.elasticsearch")
public class ElasticsearchConfiguration {

    @Autowired
    private EsFactory esFactory;

    @Bean
    public Client client() throws UnknownHostException {
        return esFactory.getClient();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }


}