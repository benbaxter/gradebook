package com.benjamingbaxter.gradebook.android.view.course;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.model.Course;

public class DashboardListFragment extends MasterListFragment<Course> {
	
	private static final long serialVersionUID = 1322349121775672944L;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No Course Material");

		// // We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// FIXME: This should be injected.
		mutableRepository = new SqliteCourseDao(
				new GradebookDbHelper(getActivity()));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.master_details_list, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add) {
			openAddDetails();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}