package com.yahoo.pmathew.imageviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yahoo.pmathew.imageviewer.R;
import com.yahoo.pmathew.imageviewer.adapter.ImageResultAdapter;
import com.yahoo.pmathew.imageviewer.listener.EndlessScrollListener;
import com.yahoo.pmathew.imageviewer.model.ImageResult;
import com.yahoo.pmathew.imageviewer.model.SettingsConfig;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ImageViewerHomeActivity extends Activity {

    private EditText etQuery;
    private GridView gvResults;
    private List<ImageResult> imageResults;
    private ImageResultAdapter imageResultAdapter;
    private SettingsConfig settingsConfig;
    private final int REQUEST_CODE = 1234567;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer_home);
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        imageResults = new ArrayList();
        imageResultAdapter = new ImageResultAdapter(this, imageResults);
        settingsConfig = new SettingsConfig();

        gvResults.setAdapter(imageResultAdapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ImageViewerHomeActivity.this, ImageDisplayActivity.class);
                ImageResult imageResult = imageResults.get(position);
                intent.putExtra("url", imageResult.fullUrl);
                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ONACTI", ":)");
        if (resultCode == RESULT_OK) {
            // Extract name value from result extras
            settingsConfig = (SettingsConfig)data.getSerializableExtra("settingsConfig");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_viewer_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(View button){
        String query = getQuery();



        AsyncHttpClient client = new AsyncHttpClient();
        //https://ajax.googleapis.com/ajax/services/search/images?q=funny&v=1.0&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q=" + query + "&v=1.0&rsz=8&start=0";

        client.get(searchUrl,new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONArray imageArray = response.getJSONObject("responseData").getJSONArray("results");
                    imageResultAdapter.clear();
                    imageResultAdapter.addAll(ImageResult.getImages(imageArray));
                } catch (JSONException e) {
                    Log.e("DEBUG",response.toString(),e);
                }
                Log.e("DEBUG",imageResults.toString());

            }

        });
    }

    private String getQuery(){
        String query = etQuery.getText().toString();
        if(settingsConfig.colorFilter != null){
            query=query+"&imgcolor="+settingsConfig.colorFilter;
        }

        if(settingsConfig.size != null){
            if("Small".equalsIgnoreCase(settingsConfig.size)) {
                query = query + "&imgsz=small";
            }

            if("Medium".equalsIgnoreCase(settingsConfig.size)) {
                query = query + "&imgsz=medium";
            }

            if("Large".equalsIgnoreCase(settingsConfig.size)) {
                query = query + "&imgsz=xxlarge";
            }

            if("Extra Large".equalsIgnoreCase(settingsConfig.size)) {
                query = query + "&imgsz=huge";
            }
        }

        if(settingsConfig.type != null){
            query=query+"&imgtype="+settingsConfig.type;
        }

        Log.d("debug", query);

        return query;
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        String query = getQuery();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q=" + query + "&v=1.0&rsz=8&start=" + offset*8;
        AsyncHttpClient client = new AsyncHttpClient();
        //https://ajax.googleapis.com/ajax/services/search/images?q=funny&v=1.0&rsz=8

        client.get(searchUrl,new JsonHttpResponseHandler(){

            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONArray imageArray = response.getJSONObject("responseData").getJSONArray("results");
                    imageResultAdapter.addAll(ImageResult.getImages(imageArray));
                } catch (JSONException e) {
                    Log.e("DEBUG",response.toString(),e);
                }
                Log.e("DEBUG",imageResults.toString());

            }
        });

    }

    public void onSettingsAction(MenuItem v){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("settingsConfig", settingsConfig);
        startActivityForResult(intent, REQUEST_CODE);
    }

}
