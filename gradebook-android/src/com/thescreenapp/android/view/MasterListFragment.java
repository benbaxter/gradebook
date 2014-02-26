package com.thescreenapp.android.view;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.thescreenapp.android.view.DetailsFragment.MasterListener;
import com.thescreenapp.model.ScreenModelObject;

public abstract class MasterListFragment extends ListFragment 
	implements Serializable, MasterListener {
	
	private static final long serialVersionUID = 5626896934033946319L;
	public final static String EXTRA_DETAILS_LISTENER = "com.thescreenapp.android.view.EXTRA_DETAILS_LISTENER";
	
	public static interface DetailsListener {
		void initDetail(ScreenModelObject detail);
		void openDetails(ScreenModelObject detail);
		void openDetailsIfScreenSizeIsBigEnough(ScreenModelObject detail);
		void openAddDetails();
		void openAddDetailsIfScreenSizeIsBigEnough();
	}
	
	protected DetailsListener mDetailsListener;
	
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
	
	@Override
	public void update() {
		doUpdate();
	}
	
	protected abstract void doUpdate();
}
