package com.legue.axel.myfavoritesmovies.database.model;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    private String idReview;
    private String author;
    private String content;
    private String url;

    public Review() {
    }

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
