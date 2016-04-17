package com.uay;

import com.uay.elasticsearch.clients.BlogpostClient;
import com.uay.elasticsearch.model.Blogpost;
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

    private static final String EXISTENT_KEYWORD = "folder";
    private static final String NON_EXISTENT_KEYWORD = "discovery";
    private static final String MISTYPED_KEYWORD = "widespreat";
    private static final String FILTER = "ldap";

    @Autowired
    private BlogpostClient blogpostClient;

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

    private void importData() {
        logger.info("--- Retrieving blog posts");
        List<Blogpost> blogposts = googleBlogpostsRetriever.retrievePosts();

        logger.info("--- Saving blogposts");
        blogpostClient.save(blogposts);
        logger.info("--- Blogposts are saved");
    }

    private void searchData() {
        searchExistentKeyword();
        searchNonExistentKeyword();
        searchMistypedKeyword();
    }

    private void searchExistentKeyword() {
        logger.info("- Search for an existent keyword: " + EXISTENT_KEYWORD);

        logger.info("--- Simple search results:");
        List<Blogpost> blogposts = blogpostClient.searchQuery(EXISTENT_KEYWORD);
        blogposts.stream().forEach(post -> logger.info(post.getTitle()));
    }

    private void searchNonExistentKeyword() {
        logger.info("- Search for a non existent keyword: " + NON_EXISTENT_KEYWORD);

        logger.info("--- Simple search results:");
        List<Blogpost> blogposts = blogpostClient.searchQuery(NON_EXISTENT_KEYWORD);
        blogposts.stream().forEach(post -> logger.info(post.getTitle()));

        logger.info("--- Search with in_situ analyzer results:");
        blogposts = blogpostClient.searchWithInSituAnalyzer(NON_EXISTENT_KEYWORD);
        blogposts.stream().forEach(post -> logger.info(post.getTitle()));
    }

    private void searchMistypedKeyword() {
        logger.info("- Search for a mistyped keyword: " + NON_EXISTENT_KEYWORD);

        logger.info("--- Simple search results:");
        List<Blogpost> blogposts = blogpostClient.searchQuery(MISTYPED_KEYWORD);
        blogposts.stream().forEach(post -> logger.info(post.getTitle()));

        logger.info("--- Fuzzy search results:");
        blogposts = blogpostClient.fuzzySearchWithKeywordFilter(MISTYPED_KEYWORD, FILTER);
        blogposts.stream().forEach(post -> logger.info(post.getTitle()));
    }
}
