package com.benjamingbaxter.gradebook.android.view.interview;

import android.support.v4.app.Fragment;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;

public class InterviewMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	@Override
	public String getTitle() {
		return getString(R.string.title_section_interviews);
	}

	@Override
	protected Fragment getMasterFragment() {
		return new InterviewListFragment();
	}

	@Override
	protected DetailsFragment getDetailFragment() {
		return new InterviewDetailFragment();
	}

}
