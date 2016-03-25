package com.uay.google;

import com.uay.elasticsearch.EsFactory;
import com.uay.elasticsearch.model.Post;
import com.uay.google.model.GooglePosts;
import com.uay.google.model.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleBlogpostsRetriever {

    private static final String BROWSER_KEY = "BROWSER_KEY";
    private static final String BLOG_ID = "BLOG_ID";
    private static final String URL = "https://www.googleapis.com/blogger/v3/blogs/" + BLOG_ID + "/posts";

    @Autowired
    private RestTemplate restTemplate;

    public List<Post> retrievePosts() {
        GooglePosts googlePosts = null;
        List<Post> posts = new ArrayList<>();
        do {
            googlePosts = restTemplate.getForObject(constructUri(googlePosts), GooglePosts.class);
            posts.addAll(convertToPosts(googlePosts));
        } while (StringUtils.isNotEmpty(googlePosts.getNextPageToken()));
        return posts;
    }

    private URI constructUri(GooglePosts googlePosts) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("key", BROWSER_KEY);
        if (googlePosts != null) {
            builder.queryParam("pageToken", googlePosts.getNextPageToken());
        }
        return builder.build().encode().toUri();
    }

    private List<Post> convertToPosts(GooglePosts googlePosts) {
        return googlePosts.getItems().stream()
                .map(this::convertToPost)
                .collect(Collectors.toList());
    }

    private Post convertToPost(Item googlePost) {
        Post post = new Post();
        post.setTitle(googlePost.getTitle());
        post.setBody(googlePost.getContent());
        post.setDate(googlePost.getPublished());
        post.setAuthor(googlePost.getAuthor().getDisplayName());
        post.setKeywords(googlePost.getLabels());
        return post;
    }
}
