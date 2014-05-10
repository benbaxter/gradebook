package com.benjamingbaxter.gradebook.android.view.assignment;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;

public class AssignmentMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	private static final long serialVersionUID = -7242403633414594081L;

	@Override
	public String getTitle() {
		return getString(R.string.title_section_students);
	}

	@Override
	protected MasterListFragment getMasterFragment() {
		return new AssignmentListFragment();
	}

	@Override
	protected DetailsFragment getDetailFragment() {
		return new AssignmentDetailFragment();
	}

}
