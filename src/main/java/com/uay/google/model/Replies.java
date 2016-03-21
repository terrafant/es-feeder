package com.uay.google.model;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "totalItems",
    "selfLink"
})
public class Replies {

    @JsonProperty("totalItems")
    private String totalItems;
    @JsonProperty("selfLink")
    private String selfLink;

    /**
     * 
     * @return
     *     The totalItems
     */
    @JsonProperty("totalItems")
    public String getTotalItems() {
        return totalItems;
    }

    /**
     * 
     * @param totalItems
     *     The totalItems
     */
    @JsonProperty("totalItems")
    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
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

}
