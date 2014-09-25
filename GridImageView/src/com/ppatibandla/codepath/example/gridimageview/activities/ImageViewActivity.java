package com.ppatibandla.codepath.example.gridimageview.activities;

import java.nio.channels.AsynchronousCloseException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.ppatibandla.codepath.example.gridimageview.R;
import com.ppatibandla.codepath.example.gridimageview.R.id;
import com.ppatibandla.codepath.example.gridimageview.R.layout;
import com.ppatibandla.codepath.example.gridimageview.R.menu;
import com.ppatibandla.codepath.example.gridimageview.activities.SearchFiltersDialog.SearchFilterListner;
import com.ppatibandla.codepath.example.gridimageview.activities.SearchFiltersDialog.SearchOptions;
import com.ppatibandla.codepath.example.gridimageview.adapters.EndlessScrollListener;
import com.ppatibandla.codepath.example.gridimageview.adapters.ImageResultsAdapter;
import com.ppatibandla.codepath.example.gridimageview.models.ImageResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

public class ImageViewActivity extends FragmentActivity implements
		SearchFilterListner {
	private Context c;
	private GridView gvImages;
	private String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter resultsAdapter;
	private final int gvImagesVisibilityThreshold = 12;
	private String query;
	private int nextStart = 0;

	public final int MAX_IMAGE_RESULTS = 64;
	public final int IMAGE_RESULTS_PER_REQ = 8;
	private boolean pendingFetch = false;

	public SearchOptions searchOptions = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		setContentView(R.layout.activity_image_view);
		gvImages = (GridView) findViewById(R.id.gvImages);

		imageResults = new ArrayList<ImageResult>();
		resultsAdapter = new ImageResultsAdapter(this, imageResults);
		gvImages.setAdapter(resultsAdapter);
		gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent i = new Intent(c, ImageDisplayActivity.class);
				ImageResult imageInfo = imageResults.get(position);
				i.putExtra("info", imageInfo);
				startActivity(i);
			}
		});

		gvImages.setOnScrollListener(new EndlessScrollListener(
				gvImagesVisibilityThreshold) {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				fetchNextImages();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_menu, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String searchQuery) {
				if (searchQuery.equals(query)) {
					// Nothing to do, return.
					return true;
				}
				query = searchQuery;

				Log.d("onQueryTextSubmit", query + ", start : " + nextStart);
				return refetchImages();
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		MenuItem settings = menu.findItem(R.id.action_settings);

		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	public boolean refetchImages() {
		resultsAdapter.clear();
		nextStart = 0;
		return fetchNextImages();

	}

	public boolean fetchNextImages() {
		if (nextStart >= MAX_IMAGE_RESULTS) {
			return false;
		}
		
		if (pendingFetch) {
			return false;
		}
		
		AsyncHttpClient client = new AsyncHttpClient();

		JsonHttpResponseHandler httpRespHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONArray resultsJSON = null;
				try {
					JSONObject responseData = response
							.getJSONObject("responseData");
					if (responseData != null) {
						resultsJSON = response.getJSONObject("responseData")
								.getJSONArray("results");
						resultsAdapter.addAll(ImageResult
								.fromJsonArary(resultsJSON));
					} else {
						nextStart -= IMAGE_RESULTS_PER_REQ;
						fetchNextImages();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				pendingFetch = false;
				super.onSuccess(statusCode, headers, response);
			}
		};
		String url = searchUrl + "&rsz=" + IMAGE_RESULTS_PER_REQ + "&start="
				+ nextStart + "&q=" + query;
		if (searchOptions != null) {
			if (!searchOptions.imageSize.contentEquals("any")) {
				url += "&imgsz=" + searchOptions.imageSize;
			}
			if (!searchOptions.colorFilter.contentEquals("any")) {
				url += "&imgcolor=" + searchOptions.colorFilter;
			}
			if (!searchOptions.imageType.contentEquals("any")) {
				url += "&imgtype=" + searchOptions.imageType;
			}
			if (!searchOptions.siteFilter.isEmpty()) {
				url += "&as_sitesearch=" + searchOptions.siteFilter;
			}

		}

		Log.i("Query", url);
		nextStart += IMAGE_RESULTS_PER_REQ;

		client.get(url, httpRespHandler);
		pendingFetch = true;
		return true;
	}

	public void onSettingsClick(MenuItem mi) {
		FragmentActivity fa = (FragmentActivity) c;
		DialogFragment settingsDialog = SearchFiltersDialog
				.newInstance("Advanced Search Options");
		settingsDialog.show(fa.getSupportFragmentManager(),
				"fragment_search_settings");
	}

	@Override
	public void onFinishedSearchFilterDialog(SearchOptions s) {
		// TODO Auto-generated method stub
		searchOptions = s;
		if (query != null) {
			refetchImages();
		}
		// Toast.makeText(c, s.imageSize, Toast.LENGTH_SHORT).show();
	}
}
