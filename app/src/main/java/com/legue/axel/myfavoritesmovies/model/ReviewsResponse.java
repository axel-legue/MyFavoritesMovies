package com.legue.axel.myfavoritesmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

    private int idPreviewResponse;
    private int page;
    @SerializedName("results")
    private List<Review> reviewList;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public ReviewsResponse() {
    }

    public int getIdPreviewResponse() {
        return idPreviewResponse;
    }

    public void setIdPreviewResponse(int idPreviewResponse) {
        this.idPreviewResponse = idPreviewResponse;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
