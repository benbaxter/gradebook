package com.benjamingbaxter.gradebook.android.view.course;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment;
import com.benjamingbaxter.gradebook.android.view.MasterDetailFragment;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.view.NavigationBarFragment;
import com.benjamingbaxter.gradebook.model.Course;

public class DashboardMasterDetailFragment extends MasterDetailFragment implements NavigationBarFragment {
	
	private static final long serialVersionUID = 4113164379371995217L;
	public final static String EXTRA_COURSE = DashboardMasterDetailFragment.class.getPackage().getName() + "CourseOverviewMasterDetailFragment.EXTRA_COURSE";

	Course course;
	
	@Override
	public void onAttach(Activity activity) {
		Bundle args = getArguments();
		if( args != null && args.containsKey(EXTRA_COURSE) ) {
			course = (Course) args.get(EXTRA_COURSE);
		}

		super.onAttach(activity);
	}
	
	@Override
	public String getTitle() {
		if( course != null ) {
			return course.getCourseName();
		}
		return getString(R.string.title_section_courses);
	}

	@Override
	protected MasterListFragment getMasterFragment() {
		DashboardListFragment frag = new DashboardListFragment();
		frag.setArguments(getArguments());
		return frag;
	}

	@Override
	protected DetailsFragment getDetailFragment() {
		DashboardDetailFragment frag = new DashboardDetailFragment();
		frag.setArguments(getArguments());
		return frag;
	}

}
