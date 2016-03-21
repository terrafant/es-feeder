package com.uay.google.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "kind",
    "id",
    "blog",
    "published",
    "updated",
    "etag",
    "url",
    "selfLink",
    "title",
    "content",
    "author",
    "replies",
    "labels"
})
public class Item {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("id")
    private String id;
    @JsonProperty("blog")
    private Blog blog;
    @JsonProperty("published")
    private String published;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("etag")
    private String etag;
    @JsonProperty("url")
    private String url;
    @JsonProperty("selfLink")
    private String selfLink;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("author")
    private Author author;
    @JsonProperty("replies")
    private Replies replies;
    @JsonProperty("labels")
    private List<String> labels = new ArrayList<String>();

    /**
     * 
     * @return
     *     The kind
     */
    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    /**
     * 
     * @param kind
     *     The kind
     */
    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The blog
     */
    @JsonProperty("blog")
    public Blog getBlog() {
        return blog;
    }

    /**
     * 
     * @param blog
     *     The blog
     */
    @JsonProperty("blog")
    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    /**
     * 
     * @return
     *     The published
     */
    @JsonProperty("published")
    public String getPublished() {
        return published;
    }

    /**
     * 
     * @param published
     *     The published
     */
    @JsonProperty("published")
    public void setPublished(String published) {
        this.published = published;
    }

    /**
     * 
     * @return
     *     The updated
     */
    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    /**
     * 
     * @param updated
     *     The updated
     */
    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * 
     * @return
     *     The etag
     */
    @JsonProperty("etag")
    public String getEtag() {
        return etag;
    }

    /**
     * 
     * @param etag
     *     The etag
     */
    @JsonProperty("etag")
    public void setEtag(String etag) {
        this.etag = etag;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The selfLink
     */
    @JsonProperty("selfLink")
    public String getSelfLink() {
        return selfLink;
    }

    /**
     * 
     * @param selfLink
     *     The selfLink
     */
    @JsonProperty("selfLink")
    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The content
     */
    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 
     * @return
     *     The author
     */
    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    /**
     * 
     * @param author
     *     The author
     */
    @JsonProperty("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * 
     * @return
     *     The replies
     */
    @JsonProperty("replies")
    public Replies getReplies() {
        return replies;
    }

    /**
     * 
     * @param replies
     *     The replies
     */
    @JsonProperty("replies")
    public void setReplies(Replies replies) {
        this.replies = replies;
    }

    /**
     * 
     * @return
     *     The labels
     */
    @JsonProperty("labels")
    public List<String> getLabels() {
        return labels;
    }

    /**
     * 
     * @param labels
     *     The labels
     */
    @JsonProperty("labels")
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

}
