package com.thescreenapp.android.view.candidate;

import android.support.v4.app.Fragment;

import com.thescreenapp.android.R;
import com.thescreenapp.android.view.DetailsFragment;
import com.thescreenapp.android.view.MasterDetailFragment;
import com.thescreenapp.android.view.NavigationBarFragment;

public class CandidateMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	@Override
	public String getTitle() {
		return getString(R.string.title_section_candidates);
	}

	@Override
	protected Fragment getMasterFragment() {
		return new CandidateListFragment();
	}

	@Override
	protected DetailsFragment getDetailFragment() {
		return new CandidateDetailFragment();
	}

}
