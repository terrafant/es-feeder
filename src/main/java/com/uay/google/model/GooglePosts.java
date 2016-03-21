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
    "nextPageToken",
    "items",
    "etag"
})
public class GooglePosts {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("nextPageToken")
    private String nextPageToken;
    @JsonProperty("items")
    private List<Item> items = new ArrayList<Item>();
    @JsonProperty("etag")
    private String etag;

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
     *     The nextPageToken
     */
    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * 
     * @param nextPageToken
     *     The nextPageToken
     */
    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * 
     * @return
     *     The items
     */
    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
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

}
