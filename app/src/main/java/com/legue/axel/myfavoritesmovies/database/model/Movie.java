package com.legue.axel.myfavoritesmovies.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movie")
public class Movie implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private Boolean adult;
    @SerializedName("overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    @Ignore
    private List<Integer> genreIds;
    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private float popularity;
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private int voteCount;
    @SerializedName("video")
    private boolean isVideo;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private float voteAverage;

    @Ignore
    public Movie() {
    }

    public Movie(
            int id,
            String posterPath,
            Boolean adult,
            String overview,
            String releaseDate,
            String originalTitle,
            String originalLanguage,
            String title,
            String backdropPath,
            float popularity,
            int voteCount,
            boolean isVideo,
            float voteAverage) {

        this.id = id;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.isVideo = isVideo;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        isVideo = in.readByte() != 0;
        voteAverage = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeFloat(voteAverage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }


}
