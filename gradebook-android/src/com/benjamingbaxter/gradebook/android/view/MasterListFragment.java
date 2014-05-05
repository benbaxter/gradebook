package com.benjamingbaxter.gradebook.android.view;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;

import com.benjamingbaxter.gradebook.android.GradebookListFragment;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment.MasterListener;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public abstract class MasterListFragment extends GradebookListFragment 
	implements Serializable, MasterListener {
	
	private static final long serialVersionUID = 5626896934033946319L;
	public final static String EXTRA_DETAILS_LISTENER = MasterListFragment.class.getPackage().getName() + "MasterListFragment.EXTRA_DETAILS_LISTENER";
	
	public static interface DetailsListener {
		void initDetail(ScreenModelObject detail);
		void openDetails(ScreenModelObject detail);
		void openDetailsIfScreenSizeIsBigEnough(ScreenModelObject detail);
		void openAddDetails();
		void openAddDetailsIfScreenSizeIsBigEnough();
		void onUpdateListUpdated();
	}
	
	protected DetailsListener mDetailsListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Bundle args = getArguments();
		if( args != null && args.containsKey(EXTRA_DETAILS_LISTENER) ) {
			Object obj = args.get(EXTRA_DETAILS_LISTENER);
			if ( obj instanceof DetailsListener ) {
				mDetailsListener = (DetailsListener) obj;
			}
		}
	}
	
	@Override
	public void setArguments(Bundle bundle) {
		super.setArguments(bundle);
		Object obj = bundle.get(EXTRA_DETAILS_LISTENER);
		if ( obj instanceof DetailsListener ) {
			mDetailsListener = (DetailsListener) obj;
		}
	}
	
	public void initDetail(ScreenModelObject detail) {
		mDetailsListener.initDetail(detail);
	}
	
	public void openDetails(ScreenModelObject detail) {
		mDetailsListener.openDetails(detail);
	}
	
	public void openDetailsIfScreenSizeIsBigEnough(ScreenModelObject detail) {
		mDetailsListener.openDetailsIfScreenSizeIsBigEnough(detail);
	}
	
	public void openAddDetails() {
		mDetailsListener.openAddDetails();
	}
	
	public void openAddDetailsIfScreenSizeIsBigEnough() {
		mDetailsListener.openAddDetailsIfScreenSizeIsBigEnough();
	}
	
	public void update() {
		doUpdate();
		mDetailsListener.onUpdateListUpdated();
	}
	
	protected abstract void doUpdate();
}
