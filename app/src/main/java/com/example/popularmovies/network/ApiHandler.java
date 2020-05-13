package com.example.popularmovies.network;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class ApiHandler {
    private static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String MOVIE_API_KEY_PARAM = "api_key";

    //private static final String API_KEY = "";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185";
    private static final String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private static final String VIDEO_BASE_URL = "http://api.themoviedb.org/3/movie/";


    public static String movieUri(String sortBy) {
        Uri mUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(MOVIE_API_KEY_PARAM, API_KEY)
                .build();
        return mUri.toString();
    }

    public static String posterUri(String posterName) {
        return POSTER_BASE_URL + posterName;
    }

    public static String backdropUri(String backdrop) {
        return BACKDROP_BASE_URL + backdrop;
    }

    public static String videoUri(int id){
        final String video = "videos";
        Uri videoUri = Uri.parse(VIDEO_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(video)
                .appendQueryParameter(MOVIE_API_KEY_PARAM, API_KEY)
                .build();
        return videoUri.toString();

    }
    public static String reviewUri(int id){
        final String review = "reviews";
        Uri reviewUri = Uri.parse(VIDEO_BASE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(review)
                .appendQueryParameter(MOVIE_API_KEY_PARAM, API_KEY)
                .build();
        return reviewUri.toString();

    }

    //From StackOverflow
    public static void launchVideoPlayer(Context context, String key) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
