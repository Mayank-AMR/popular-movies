package com.example.popularmovies.utils;

import android.util.Log;

import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project Popular Movies
 * @Created_by Mayank Kumar on 11-05-2020 12:21 AM
 */
public class ReviewJSONUtil {
    private static final String TAG = "ReviewJSONUtil";

    private static final String REVIEW_AUTHoR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_ID = "id";
    private static final String REVIEW_URL = "url";
    private static final String JSON_RESULT_ARRAY = "results";

    public static List<Review> parseReviewJson(JSONObject object){
        Log.d(TAG, "parseReviewJson: ");

        List<Review> reviewList = new ArrayList<>();
        try {
            JSONArray resultArray = object.getJSONArray(JSON_RESULT_ARRAY);
            for (int item = 0; item < resultArray.length(); item++) {
                JSONObject reviewObject = resultArray.getJSONObject(item);

                String author = reviewObject.optString(REVIEW_AUTHoR);
                String content = reviewObject.optString(REVIEW_CONTENT);
                String id = reviewObject.optString(REVIEW_ID);
                String url = reviewObject.optString(REVIEW_URL);

                reviewList.add(new Review(id,author,content,url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewList;
    }
}
