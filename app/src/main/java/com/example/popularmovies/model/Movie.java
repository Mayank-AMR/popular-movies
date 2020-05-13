package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double popularity;
    private boolean video;
    private double voteAverage;
    private int voteCount;
    private String releaseDate;
    private boolean favorite = false;

    public Movie(int id, String title, String overview, String posterPath, String backdropPath, double popularity, boolean video, double voteAverage, int voteCount, String releaseDate) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        //dest.writeByte(video);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(releaseDate);
    }

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        //video = in.readByte() != 0;
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        releaseDate = in.readString();
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

    //-------------------------- Stage 2nd --------------------------------------------------------

//    public static Movie fromCursor(Cursor cursor) {
//        Movie movie = new Movie();
//        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID)));
//        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
//        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
//        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));
//        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP)));
//        movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY)));
//        //movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VIDEO)) == 1);
//        movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
//        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
//        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
//        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE)) == 1);
//        return movie;
//    }

//    public ContentValues toContentValues() {
//        ContentValues cv = new ContentValues();
//        cv.put(MovieContract.MovieEntry.COLUMN_ID, getId());
//        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, getTitle());
//        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, getOverview());
//        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, getPosterPath());
//        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP, getBackdropPath());
//        cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY, getPopularity());
//        //cv.put(MovieEntry.COLUMN_VIDEO, isVideo());
//        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, getVoteAverage());
//        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, getVoteCount());
//        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, getReleaseDate());
//        cv.put(MovieContract.MovieEntry.COLUMN_FAVORITE, isFavorite());
//        return cv;
//    }
}
