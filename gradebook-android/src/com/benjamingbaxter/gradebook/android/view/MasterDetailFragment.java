package com.benjamingbaxter.gradebook.android.view;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment.DetailsListener;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public abstract class MasterDetailFragment<T extends ScreenModelObject> extends Fragment 
	implements Serializable, DetailsListener<T> {

	private static final long serialVersionUID = 8045372239266387723L;
	private static final String MASTER_FRAG_TAG = "master_frag_tag";
	private static final String DETAILS_FRAG_TAG = "details_frag_tag";

	private boolean detailsInline = false;

	public static final String BUNDLE_CALLBACKS = MasterDetailFragment.class.getPackage().getName() + ".bundle_callbacks";
	public static final String BUNDLE_ASSIGNMENT_TYPE = MasterDetailFragment.class.getPackage().getName() + ".bundle_assignment_type";
	
	Callbacks callbacks;
	
	public static interface Callbacks {
		void onSectionAttached(MasterDetailFragment<?> fragment);
		void onUpdateListUpdated();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	View rootView;
    	
    	Bundle bundledArgs = getArguments();
    	if( bundledArgs != null && bundledArgs.containsKey(BUNDLE_CALLBACKS)) {
    		callbacks = (Callbacks) bundledArgs.get(BUNDLE_CALLBACKS);
    	}
    	
    	
    	Bundle bundleForMaster = new Bundle();
    	bundleForMaster.putSerializable(MasterListFragment.EXTRA_DETAILS_LISTENER, this);
    	bundleForMaster.putAll(getArguments());
    	
    	//TODO: FIXME! how to make this more generic? What if it is not 
    	//list/details setup? What if it is a two pane view?
    	MasterListFragment<? extends ScreenModelObject> masterFragment = getMasterFragment();
    	masterFragment.setArguments(bundleForMaster);
    	
    	Bundle bundleForDeatils = new Bundle();
    	bundleForDeatils.putSerializable(DetailsFragment.EXTRA_MASTER_LISTENER, masterFragment);
    	bundleForDeatils.putAll(getArguments());
    	
    	Fragment detailFragment = getDetailFragment();
    	detailFragment.setArguments(bundleForDeatils);
    	
    	// On a tablet (600+ dp width), display master and detail side-by-side.
    	// On a phone (< 600 dp width), display just the master fragment.
    	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    	int widthInDp = (int) Math.round((float)displayMetrics.widthPixels / displayMetrics.density);
    	detailsInline = widthInDp >= 600;
    	if ( detailsInline ) {
            rootView = inflater.inflate(R.layout.fragment_master_detail, container, false);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.section_master, masterFragment, MASTER_FRAG_TAG);
            transaction.add(R.id.section_detail, detailFragment, DETAILS_FRAG_TAG);
            transaction.commit();
    	} else {
            rootView = inflater.inflate(R.layout.fragment_master_detail_single, container, false);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.section_master_detail, masterFragment, MASTER_FRAG_TAG);
            transaction.commit();
    	}
    	
        return rootView;
    }

    public void onUpdateListUpdated() {
    	if( callbacks != null ) {
    		callbacks.onUpdateListUpdated();
    	}
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callbacks) {
        	((Callbacks)activity).onSectionAttached(this);
        }
        Bundle args = getArguments();
        if( args != null && args.containsKey(BUNDLE_CALLBACKS)) {
    		callbacks = (Callbacks) args.get(BUNDLE_CALLBACKS);
    	}
    }
    
    @Override
    public void initDetail(T detail) {
    	//only load detail if on tablet
    	if (detailsInline) {
			loadDetailInline(detail);
		}
    }

    
    @Override
    public void openDetails(T detail) {
    	if (detailsInline) {
			loadDetailInline(detail);
		} else {
			Bundle bundle = new Bundle();
	    	bundle.putSerializable(DetailsFragment.EXTRA_DETAILS_ID, detail);
	    	
	    	bringDetailsFragmentToFront(bundle);
		}
    }
    
    @Override
    public void openDetailsIfScreenSizeIsBigEnough(T detail) {
    	if( detailsInline ) {
    		openDetails(detail);
    	}
    }
    
    @Override
    public void openAddDetails() {
    	if (detailsInline) {
    		((DetailsFragment<?>) 
        			getChildFragmentManager().findFragmentByTag(DETAILS_FRAG_TAG))
        				.openAddDetails();
		} else {
			Bundle bundle = new Bundle();
	    	bundle.putSerializable(DetailsFragment.EXTRA_DETAILS_ADD_MODE, true);
	    	
	    	bringDetailsFragmentToFront(bundle);
		}
    }
    
    @Override
    public void openAddDetailsIfScreenSizeIsBigEnough() {
    	if( detailsInline ) {
    		openAddDetails();
    	}
    }
    
	private void bringDetailsFragmentToFront(Bundle bundle) {
		MasterListFragment<?> masterFrag = (MasterListFragment<?>) getChildFragmentManager()
				.findFragmentByTag(MASTER_FRAG_TAG);
		bundle.putSerializable(DetailsFragment.EXTRA_MASTER_LISTENER, masterFrag);
		
		DetailsFragment<?> detailFrag = getNonNullDetailFragment();
		detailFrag.setArguments(bundle);
		
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		//do not want to remove, just move to the background
		transaction.addToBackStack(MASTER_FRAG_TAG);
		transaction.hide(masterFrag);

		transaction.add(R.id.section_master_detail, detailFrag,DETAILS_FRAG_TAG);
		
		transaction.commit();
	}
    
    private void loadDetailInline(T detail) {
    	@SuppressWarnings("unchecked")
		DetailsFragment<T> detailFrag = (DetailsFragment<T>) 
    			getChildFragmentManager().findFragmentByTag(DETAILS_FRAG_TAG);
    	detailFrag.loadDetails(detail);
    }

    protected abstract MasterListFragment<?> getMasterFragment();
    protected abstract DetailsFragment<?> getDetailFragment();
    
    private DetailsFragment<?> getNonNullDetailFragment() {
    	DetailsFragment<?> detailFrag = (DetailsFragment<?>) 
    			getChildFragmentManager().findFragmentByTag(DETAILS_FRAG_TAG);
    	if( detailFrag == null ) {
    		detailFrag = getDetailFragment();
    	}
    	return detailFrag;
    }
    
}
