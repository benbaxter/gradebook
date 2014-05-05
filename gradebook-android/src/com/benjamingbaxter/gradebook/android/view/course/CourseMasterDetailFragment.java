package com.benjamingbaxter.gradebook.android.view.course;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;

public class CourseMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	private static final long serialVersionUID = 4113164379371995217L;
	
	@Override
	public String getTitle() {
		return getString(R.string.title_section_courses);
	}

	@Override
	protected MasterListFragment getMasterFragment() {
		return new CourseListFragment();
	}

	@Override
	protected DetailsFragment getDetailFragment() {
		return new CourseDetailFragment();
	}

}
