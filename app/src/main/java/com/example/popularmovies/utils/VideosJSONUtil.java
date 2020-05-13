package com.example.popularmovies.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 10-05-2020 07:46 AM
 */
public class VideosJSONUtil {
    private static final String TAG = "VideosJSONUtil";

    private static final String JSON_RESULT_ARRAY = "results";

    private static final String VIDEO_ID = "id";
    private static final String VIDEO_KEY = "key";
    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_SITE = "site";
    private static final String VIDEO_SIZE = "size";
    private static final String VIDEO_TYPE = "type";

    public static List<Videos> parseVideoJson(JSONObject object) {
        Log.d(TAG, "parseVideoJson: ");
        List<Videos> videosList = new ArrayList<>();
        try {
            JSONArray resultArray = object.getJSONArray(JSON_RESULT_ARRAY);
            for (int item = 0; item < resultArray.length(); item++) {
                JSONObject videoObject = resultArray.getJSONObject(item);

                String id = videoObject.optString(VIDEO_ID);
                String key = videoObject.optString(VIDEO_KEY);
                String name = videoObject.optString(VIDEO_NAME);
                String site = videoObject.optString(VIDEO_SITE);
                int size = videoObject.optInt(VIDEO_SIZE);
                String type = videoObject.optString(VIDEO_TYPE);

                videosList.add(new Videos(id, key, name, site, size, type));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videosList;
    }
}
