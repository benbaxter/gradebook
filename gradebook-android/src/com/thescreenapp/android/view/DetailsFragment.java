package com.thescreenapp.android.view;

import com.thescreenapp.model.ScreenModelObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class DetailsFragment extends Fragment {
	
	public final static String EXTRA_DETAILS_ID = "com.thescreenapp.android.view.EXTRA_DETAILS_ID";
	public final static String EXTRA_DETAILS_ADD_MODE = "com.thescreenapp.android.view.EXTRA_DETAILS_ADD_MODE";
	public final static String EXTRA_MASTER_LISTENER = "com.thescreenapp.android.view.EXTRA_MASTER_LISTENER";
	
	//TODO: FIXME! maybe not call it master listener?
	//what if we wanted this to update a sibling fragment instead?
	public static interface MasterListener {
		//TODO: think of a better name
		//some options: notifyDetailsChanged? update? triggerDetailsChanged?
		public void update();
	}
	
	protected MasterListener mMasterListener;
	
	@Override
	public void setArguments(Bundle bundle) {
		super.setArguments(bundle);
		Object obj = bundle.get(EXTRA_MASTER_LISTENER);
		if ( obj instanceof MasterListener ) {
			mMasterListener = (MasterListener) obj;
		}
	}
	
	public abstract void loadDetails(ScreenModelObject detail);
	
	public abstract void openAddDetails();
	
	protected void updateMaster() {
		mMasterListener.update();
	}
}