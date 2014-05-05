package com.benjamingbaxter.gradebook.android.view.course;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.widget.ViewBinding;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.model.Course;

public class DashboardListFragment extends MasterListFragment
		implements
		ViewBinding<View, Course> {
	
	private static final long serialVersionUID = 1322349121775672944L;

	protected CourseDao courseDao;
	
	private Course course;
	
	// This is the Adapter being used to display the list's data.
	protected ArrayAdapter<String> adapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Bundle args = getArguments();
		if( args != null && args.containsKey(DashboardMasterDetailFragment.EXTRA_COURSE) ) {
			course = (Course) args.get(DashboardMasterDetailFragment.EXTRA_COURSE);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No Course Material");

		// // We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// FIXME: This should be injected.
		courseDao = new SqliteCourseDao(
				new GradebookDbHelper(getActivity()));

		// Create an empty adapter we will use to display the loaded data.
		List<String> options = new ArrayList<String>();
		options.add("Dashboard");
		options.add("Students");
		options.add("Programs");
		options.add("Quizes");
		options.add("Labs");
		options.add("Tests");
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, options);

		setListAdapter(adapter);

		setListShown(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.candidate, menu);

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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Insert desired behavior here.
		Log.i("FragmentComplexList", "Item clicked: " + id);
//		openDetails(adapter.getItem(position));
	}

	@Override
	public void bindView(View view, Course course) {
		((TextView) view.findViewById(R.id.text_title)).setText(course
				.getCourseName());
	}
	
	@Override
	protected void doUpdate() {
		// TODO Auto-generated method stub
		
	}
}