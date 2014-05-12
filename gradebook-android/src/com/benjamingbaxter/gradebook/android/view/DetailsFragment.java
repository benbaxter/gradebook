package com.benjamingbaxter.gradebook.android.view;

import android.os.Bundle;
import android.view.View;

import com.benjamingbaxter.gradebook.android.GradebookFragment;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public abstract class DetailsFragment<T extends ScreenModelObject> extends GradebookFragment {
	
	public final static String EXTRA_DETAILS_ID = DetailsFragment.class.getPackage().getName() + "DetailsFragment.EXTRA_DETAILS_ID";
	public final static String EXTRA_DETAILS_ADD_MODE = DetailsFragment.class.getPackage().getName() + "DetailsFragment.EXTRA_DETAILS_ADD_MODE";
	public final static String EXTRA_MASTER_LISTENER = DetailsFragment.class.getPackage().getName() + "DetailsFragment.EXTRA_MASTER_LISTENER";
	
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
	
	public abstract void loadDetails(T detail);
	
	public abstract void openAddDetails();
	
	protected void updateMaster() {
		mMasterListener.update();
	}
	
	protected abstract void bindModelToDisplayableView(View view);
	
	protected abstract void bindModelToEditableView(View view);
	
	protected abstract T bindViewToModel();
	
	protected abstract class DetailMutableModelOnClickListener extends MutateModelOnClickListener {

		@Override
		protected void viewToModel() {
			bindViewToModel();
		}

		@Override
		protected View view() {
			return getView();
		}

		@Override
		protected void modelToView(View view) {
			bindModelToDisplayableView(view);
		}
	}

}