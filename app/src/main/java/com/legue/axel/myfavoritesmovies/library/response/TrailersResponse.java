package com.legue.axel.myfavoritesmovies.library.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.legue.axel.myfavoritesmovies.database.model.Trailer;

import java.util.List;

public class TrailersResponse {

    @SerializedName("id")
    @Expose
    private int videoResponseId;
    @SerializedName("results")
    @Expose
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
