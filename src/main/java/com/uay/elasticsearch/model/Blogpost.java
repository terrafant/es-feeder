package com.uay.elasticsearch.model;

import com.uay.elasticsearch.EsConstants;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Map;

@Document(indexName = EsConstants.INDEX, type = EsConstants.TYPE)
public class Blogpost {

    public static final String BODY_FIELD = "body";
    public static final String KEYWORDS_FIELD = "keywords";
    public static final String AUTHOR_FIELD = "author";
    public static final String TITLE_FIELD = "title";
    public static final String DATE_FIELD = "date";

    @Id
    private String id;
    private String date;
    private String author;
    private String title;
    private String body;
    private List<String> keywords;

    public Blogpost() {
    }

    public Blogpost(Map<String, Object> source) {
        setTitle(source.get(TITLE_FIELD).toString());
        setAuthor(source.get(AUTHOR_FIELD).toString());
        setBody(source.get(BODY_FIELD).toString());
        setDate(source.get(DATE_FIELD).toString());
        setKeywords((List<String>) source.get(KEYWORDS_FIELD));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
