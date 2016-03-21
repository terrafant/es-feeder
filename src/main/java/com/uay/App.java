package com.uay;

import com.uay.elasticsearch.clients.esnative.PostNativeClient;
import com.uay.elasticsearch.clients.jest.PostJestClient;
import com.uay.elasticsearch.clients.springdata.PostSpringDataRepository;
import com.uay.google.GoogleBlogpostsRetriever;
import com.uay.elasticsearch.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

import java.io.IOException;
import java.util.List;

@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class App implements CommandLineRunner {

    @Autowired
    private PostSpringDataRepository postSpringDataRepository;
    @Autowired
    private PostNativeClient postNativeClient;
    @Autowired
    private PostJestClient postJestClient;
    @Autowired
    private GoogleBlogpostsRetriever googleBlogpostsRetriever;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = googleBlogpostsRetriever.retrievePosts();
        postSpringDataRepository.save(posts);
//        postNativeClient.save(posts);
//        postJestClient.save(posts);
    }
}
