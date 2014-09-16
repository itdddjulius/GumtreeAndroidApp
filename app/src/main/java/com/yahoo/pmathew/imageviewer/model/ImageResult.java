package com.yahoo.pmathew.imageviewer.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmathew on 9/15/14.
 */
public class ImageResult {
    public String fullUrl;
    public String thumbUrl;
    public String title;

    public ImageResult(JSONObject json){
        try {
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");

        } catch (Exception e){
            Log.e("DEBUG", "failed", e);
        }
    }

    public static List<ImageResult> getImages(JSONArray imagesArray){
        List<ImageResult> results = new ArrayList<ImageResult>();
        try {

            for(int i=0; i<imagesArray.length();i++){
                results.add(new ImageResult(imagesArray.getJSONObject(i)));
            }


        } catch (Exception e){
            Log.e("DEBUG", "failed", e);
        }
        return results;
    }
}
