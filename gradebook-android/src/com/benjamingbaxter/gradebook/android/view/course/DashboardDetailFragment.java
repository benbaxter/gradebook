package com.benjamingbaxter.gradebook.android.view.course;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public class DashboardDetailFragment extends DetailsFragment {
	
	protected final String TAG = this.getClass().getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_detail_course, container, false);
		return rootView;
	}

	@Override
	public void loadDetails(ScreenModelObject detail) {
		loadDetails(detail, getView());
	}
	
	protected void loadDetails(ScreenModelObject detail, View view) {
		Log.d(TAG, "Loading dashboard...");
	}

	@Override
	public void openAddDetails() {
		openAddDetails(getView());
	}
	
	protected void openAddDetails(View view) {
	}

	@Override
	protected void bindModelToDisplayableView(View view) {
	}

	@Override
	protected void bindModelToEditableView(View view) {
	}

	@Override
	protected ScreenModelObject bindViewToModel() {
		return null;
	}
}
