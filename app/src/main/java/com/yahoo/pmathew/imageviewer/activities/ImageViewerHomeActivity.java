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
import com.yahoo.pmathew.imageviewer.model.ImageResult;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer_home);
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        imageResults = new ArrayList();
        imageResultAdapter = new ImageResultAdapter(this, imageResults);

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
        String query = etQuery.getText().toString();
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();
        //https://ajax.googleapis.com/ajax/services/search/images?q=funny&v=1.0&rsz=8
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q=" + query + "&v=1.0&rsz=8";

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
}
