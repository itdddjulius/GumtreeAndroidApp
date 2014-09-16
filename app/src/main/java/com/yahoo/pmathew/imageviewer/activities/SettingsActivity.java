package com.yahoo.pmathew.imageviewer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yahoo.pmathew.imageviewer.R;
import com.yahoo.pmathew.imageviewer.model.SettingsConfig;

public class SettingsActivity extends Activity {
    private Spinner spType;
    private Spinner spSize;
    private Spinner spColor;
    private EditText etSite;
    private SettingsConfig config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        config = (SettingsConfig) getIntent().getSerializableExtra("settingsConfig");

        spColor = (Spinner) findViewById(R.id.spColor);
        spSize = (Spinner) findViewById(R.id.spImageSize);
        spType = (Spinner) findViewById(R.id.spType);
        etSite = (EditText) findViewById(R.id.etSite);

        ArrayAdapter<CharSequence> imageTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.im_type, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(imageTypeAdapter);

        ArrayAdapter<CharSequence> imageColorAdapter = ArrayAdapter.createFromResource(this,
                R.array.im_color, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(imageColorAdapter);

        ArrayAdapter<CharSequence> imageSizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.im_size, android.R.layout.simple_spinner_item);
        imageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSize.setAdapter(imageSizeAdapter);

        if(config.size == null){
            spSize.setSelection(0);
        } else {
            spSize.setSelection(imageSizeAdapter.getPosition(config.size));
        }

        if(config.colorFilter == null){
            spColor.setSelection(0);
        } else {
            spColor.setSelection(imageColorAdapter.getPosition(config.colorFilter));
        }

        if(config.type == null){
            spType.setSelection(0);
        } else {
            spType.setSelection(imageTypeAdapter.getPosition(config.type));
        }

        if(config.site != null){
            etSite.setText(config.site);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
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

    public void onSave(View view){
        if(spType.getSelectedItemPosition() == 0){
            config.type = null;
        } else {
            config.type = (String) spType.getSelectedItem();
        }

        if(spColor.getSelectedItemPosition() == 0){
            config.colorFilter = null;
        } else {
            config.colorFilter = (String) spColor.getSelectedItem();
        }

        if(spSize.getSelectedItemPosition() == 0){
            config.size = null;
        } else {
            config.size = (String) spSize.getSelectedItem();
        }

        if(etSite.getText().toString().length()==0){
            config.site = null;
        } else {
            config.site = etSite.getText().toString();
        }

        Intent i = new Intent();
        i.putExtra("settingsConfig", config);
        setResult(RESULT_OK, i);
        finish();
    }
}
