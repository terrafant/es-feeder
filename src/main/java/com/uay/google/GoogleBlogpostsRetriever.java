package com.uay.google;

import com.uay.elasticsearch.model.Blogpost;
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

    public List<Blogpost> retrievePosts() {
        GooglePosts googlePosts = null;
        List<Blogpost> blogposts = new ArrayList<>();
        do {
            googlePosts = restTemplate.getForObject(constructUri(googlePosts), GooglePosts.class);
            blogposts.addAll(convertToPosts(googlePosts));
        } while (StringUtils.isNotEmpty(googlePosts.getNextPageToken()));
        return blogposts;
    }

    private URI constructUri(GooglePosts googlePosts) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("key", BROWSER_KEY);
        if (googlePosts != null) {
            builder.queryParam("pageToken", googlePosts.getNextPageToken());
        }
        return builder.build().encode().toUri();
    }

    private List<Blogpost> convertToPosts(GooglePosts googlePosts) {
        return googlePosts.getItems().stream()
                .map(this::convertToPost)
                .collect(Collectors.toList());
    }

    private Blogpost convertToPost(Item googlePost) {
        Blogpost blogpost = new Blogpost();
        blogpost.setTitle(googlePost.getTitle());
        blogpost.setBody(googlePost.getContent());
        blogpost.setDate(googlePost.getPublished());
        blogpost.setAuthor(googlePost.getAuthor().getDisplayName());
        blogpost.setKeywords(googlePost.getLabels());
        return blogpost;
    }
}
