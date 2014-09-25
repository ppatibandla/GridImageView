package com.ppatibandla.codepath.example.gridimageview.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	public String fullUrl;
	public String thumbUrl;
	public int width;
	public int height;
	public String title;
	public int tbWidth;
	public int tbHeight;
	
	public ImageResult(JSONObject result) throws JSONException {
		fullUrl = result.getString("url");
		thumbUrl = result.getString("tbUrl");
		title = result.getString("title");
		width = result.getInt("width");
		height = result.getInt("height");
		tbWidth = result.getInt("tbWidth");
		tbHeight = result.getInt("tbHeight");
		
	}
	
	public static ArrayList<ImageResult> fromJsonArary(JSONArray results) throws JSONException {
		ArrayList<ImageResult> resultArray = new ArrayList<ImageResult>();
		for (int i = 0; i < results.length(); i++) {
			resultArray.add(new ImageResult(results.getJSONObject(i)));
		}
		return resultArray;
	}
}
