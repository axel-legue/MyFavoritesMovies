package com.legue.axel.myfavoritesmovies.library.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.legue.axel.myfavoritesmovies.database.model.Movie;

import java.util.List;

public class MoviesResponse {
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("total_results")
    @Expose
    private int totalResult;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> movieList;

    public MoviesResponse(int page, int totalResult, int totalPages, List<Movie> movieList) {
        this.page = page;
        this.totalResult = totalResult;
        this.totalPages = totalPages;
        this.movieList = movieList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
