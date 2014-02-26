package com.benjamingbaxter.gradebook.android.view.candidate;

import android.support.v4.app.Fragment;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;

public class CandidateMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	private static final long serialVersionUID = -7242403633414594081L;

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
