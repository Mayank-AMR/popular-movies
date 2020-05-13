package com.example.popularmovies.utils;

import com.example.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieJSONUtils {

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_POPULARITY = "popularity";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_VOTE_AVERAGE = "vote_average";
    public static final String KEY_VOTE_COUNT = "vote_count";
    public static final String KEY_RELEASE_DATE = "release_date";

    public static final String KEY_MOVIE_ARRAY = "results";


    public static List<Movie> parseMovie(String json){
        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(json);
            JSONArray results = jo.getJSONArray(KEY_MOVIE_ARRAY);
            for (int i = 0; i<results.length();i++){
                JSONObject movieObj = results.getJSONObject(i);

                int id = movieObj.optInt(KEY_ID);
                String title = movieObj.optString(KEY_TITLE);
                String overview = movieObj.optString(KEY_OVERVIEW);
                String posterPath = movieObj.optString(KEY_POSTER_PATH);
                String backdropPath = movieObj.optString(KEY_BACKDROP_PATH);
                double popularity = movieObj.optDouble(KEY_POPULARITY);
                boolean video = movieObj.optBoolean(KEY_VIDEO);
                double voteAverage = movieObj.optDouble(KEY_VOTE_AVERAGE);
                int voteCount = movieObj.optInt(KEY_VOTE_COUNT);
                String releaseDate = movieObj.optString(KEY_RELEASE_DATE);

                movieList.add(new Movie(id,title,overview,posterPath,backdropPath,popularity,video,voteAverage,voteCount,releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
