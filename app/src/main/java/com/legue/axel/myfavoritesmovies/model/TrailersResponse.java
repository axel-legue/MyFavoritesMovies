package com.legue.axel.myfavoritesmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("id")
    private int videoResponseId;
    @SerializedName("results")
    private List<Trailer> videoList;

    public TrailersResponse() {
    }

    public int getVideoResponseId() {
        return videoResponseId;
    }

    public void setVideoResponseId(int videoResponseId) {
        this.videoResponseId = videoResponseId;
    }

    public List<Trailer> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Trailer> videoList) {
        this.videoList = videoList;
    }
}
