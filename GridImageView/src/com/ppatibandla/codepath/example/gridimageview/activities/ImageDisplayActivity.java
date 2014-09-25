package com.ppatibandla.codepath.example.gridimageview.activities;

import com.ppatibandla.codepath.example.gridimageview.R;
import com.ppatibandla.codepath.example.gridimageview.R.layout;
import com.ppatibandla.codepath.example.gridimageview.models.ImageResult;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		getActionBar().hide();
		
		ImageResult info = (ImageResult) getIntent().getSerializableExtra("info");
		ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
		Picasso.with(this).load(info.fullUrl).into(ivImage);
	}
}
