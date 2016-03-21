package com.uay.elasticsearch.model;

import com.uay.elasticsearch.EsConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = EsConstants.INDEX, type = EsConstants.TYPE)
public class Post {

    @Id
    private String id;
    private String date;
    private String author;
    private String title;
    private String body;
    private List<String> keywords;

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
        return "Post{" +
                "date='" + date + '\'' +
                ", keywords='" + keywords + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
