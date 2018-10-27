package com.legue.axel.myfavoritesmovies.library.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.legue.axel.myfavoritesmovies.database.model.Review;

import java.util.List;

public class ReviewsResponse {

    @Expose
    private int idPreviewResponse;
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Review> reviewList;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_results")
    @Expose
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
