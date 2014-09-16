package com.yahoo.pmathew.imageviewer.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.pmathew.imageviewer.R;
import com.yahoo.pmathew.imageviewer.model.ImageResult;

import java.util.List;

/**
 * Created by pmathew on 9/15/14.
 */
public class ImageResultAdapter extends ArrayAdapter<ImageResult> {
    public ImageResultAdapter(Context context, List<ImageResult> imageResults) {
        super(context, android.R.layout.simple_list_item_1, imageResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_result_item, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);
        imageView.setImageResource(0);
        textView.setText(Html.fromHtml(imageResult.title));

        Picasso.with(getContext()).load(imageResult.thumbUrl).into(imageView);
        return convertView;
    }
}
