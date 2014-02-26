package com.thescreenapp.android.view.interview;

import android.support.v4.app.Fragment;

import com.thescreenapp.android.R;
import com.thescreenapp.android.view.DetailsFragment;
import com.thescreenapp.android.view.MasterDetailFragment;
import com.thescreenapp.android.view.NavigationBarFragment;

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
