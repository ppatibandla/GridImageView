package com.ppatibandla.codepath.example.gridimageview.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppatibandla.codepath.example.gridimageview.R;
import com.ppatibandla.codepath.example.gridimageview.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	class ViewHolder {
		ImageView ivResultImage;
		TextView tvResultTitle;
	};

	public ImageResultsAdapter(Context context, List<ImageResult> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
			ViewHolder tag = new ViewHolder();
			tag.ivResultImage = (ImageView) convertView.findViewById(R.id.ivResultImage);
			tag.tvResultTitle = (TextView) convertView.findViewById(R.id.tvResultTitle);
			convertView.setTag(tag);
		}
		
		ViewHolder tag = (ViewHolder) convertView.getTag();
		
		tag.tvResultTitle.setText(Html.fromHtml(imageInfo.title));

		/*
		Log.d("getView", "width : " + String.valueOf(parent.getWidth()));
		tag.ivResultImage.getLayoutParams().height =
				(int) ((float) convertView.getWidth() * ((float)imageInfo.tbHeight / imageInfo.tbWidth));
		Log.d("getView", "height : " + String.valueOf(parent.getHeight()));
*/
		Picasso.with(getContext()).load(imageInfo.thumbUrl).into(tag.ivResultImage);
		
		return convertView;
	}
}
