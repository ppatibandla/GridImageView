package com.ppatibandla.codepath.example.gridimageview.activities;

import com.ppatibandla.codepath.example.gridimageview.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchFiltersDialog extends DialogFragment {

	public class SearchOptions {
		String imageSize;
		String colorFilter;
		String imageType;
		String siteFilter;
	}
	
	public interface SearchFilterListner{
		void onFinishedSearchFilterDialog(SearchOptions s);
	}
	public SearchFiltersDialog() {
		
	}
	
	private Spinner spImageSize;
	private Spinner spColorFilter;
	private Spinner spImageType;
	private EditText etSiteFilter;
	
	private static class LastSelection {
		public LastSelection() {
			spImageSize = 0;
			spColorFilter = 0;
			spImageType = 0;
			etSiteFilter = new String();
		}
		int spImageSize;
		int spColorFilter;
		int spImageType;
		String etSiteFilter;
	}
	
	public static LastSelection lastSelection = new LastSelection();
	
	public static SearchFiltersDialog newInstance(String title) {
		SearchFiltersDialog frag = new SearchFiltersDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	private void setAdapter(Spinner s, int resid) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        resid, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		s.setAdapter(adapter);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_filter, container);
		spImageSize = (Spinner) view.findViewById(R.id.spImageSize);
		spColorFilter = (Spinner) view.findViewById(R.id.spColorFilter);
		spImageType = (Spinner) view.findViewById(R.id.spImageType);
		etSiteFilter = (EditText) view.findViewById(R.id.etSiteFilter);
		setAdapter(spImageSize, R.array.image_sizes_array);
		setAdapter(spColorFilter, R.array.color_filter_array);
		setAdapter(spImageType, R.array.image_type_array);
		
		spImageSize.setSelection(lastSelection.spImageSize);
		spColorFilter.setSelection(lastSelection.spColorFilter);
		spImageType.setSelection(lastSelection.spImageType);
		
		if (! lastSelection.etSiteFilter.isEmpty()) {
			etSiteFilter.setText(lastSelection.etSiteFilter);
		}
		
		Button btSave = (Button) view.findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchOptions options = new SearchOptions();
				options.imageSize = (String) spImageSize.getSelectedItem();
				options.colorFilter = (String) spColorFilter.getSelectedItem();
				options.imageType = (String) spImageType.getSelectedItem();
				options.siteFilter = etSiteFilter.getText().toString();
				
				lastSelection.spImageSize = spImageSize.getSelectedItemPosition();
				lastSelection.spColorFilter = spColorFilter.getSelectedItemPosition();
				lastSelection.spImageType = spImageType.getSelectedItemPosition();
				lastSelection.etSiteFilter = etSiteFilter.getText().toString();
				
				SearchFilterListner listner = (SearchFilterListner) getActivity();
				listner.onFinishedSearchFilterDialog(options);
				dismiss();
			}
		});
		
		String title = getArguments().getString("title");
		getDialog().setTitle(title);

		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		return view;
	}
	
}
