package com.uay;

import com.uay.elasticsearch.clients.PostClient;
import com.uay.elasticsearch.model.Post;
import com.uay.google.GoogleBlogpostsRetriever;
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

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    private PostClient postSpringDataClient;
    @Autowired
    private PostClient postNativeClient;
    @Autowired
    private PostClient postJestClient;
    @Autowired
    private PostClient postRestClient;
    @Autowired
    private GoogleBlogpostsRetriever googleBlogpostsRetriever;

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        importData();
        searchData();
    }

    private void searchData() {
        logger.info("--- Search");
        List<Post> posts = postJestClient.searchWithInSituAnalyzer("query");
//        posts = postSpringDataClient.searchWithInSituAnalyzer("query");
//        posts = postRestClient.searchWithInSituAnalyzer("query");
//        posts = postNativeClient.searchWithInSituAnalyzer("query");
        posts.stream().forEach(post -> logger.info(post.getTitle()));

        logger.info("--- Fuzzy search");
//        posts = postJestClient.fuzzySearchWithKeywordFilter("widespreat", "ldap");
//        posts = postSpringDataClient.fuzzySearchWithKeywordFilter("widespreat", "ldap");
//        posts = postRestClient.fuzzySearchWithKeywordFilter("widespreat", "ldap");
        posts = postNativeClient.fuzzySearchWithKeywordFilter("widespreat", "ldap");
        posts.stream().forEach(post -> logger.info(post.getTitle()));
    }

    private void importData() {
        logger.info("--- Retrieving blog posts");
        List<Post> posts = googleBlogpostsRetriever.retrievePosts();
        logger.info("--- Saving blogposts");
        postRestClient.save(posts);
//        postSpringDataClient.save(posts);
//        postNativeClient.save(posts);
//        postJestClient.save(posts);
        logger.info("--- Blogposts are saved");
    }
}
