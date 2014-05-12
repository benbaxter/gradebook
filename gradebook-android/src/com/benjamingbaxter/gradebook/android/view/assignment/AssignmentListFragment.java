package com.benjamingbaxter.gradebook.android.view.assignment;

import static com.benjamingbaxter.gradebook.android.view.MasterDetailFragment.BUNDLE_ASSIGNMENT_TYPE;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.content.QueryCreator;
import com.benjamingbaxter.gradebook.android.content.QueryLoader;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentTypeDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.dao.AssignmentDao;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Assignment;
import com.benjamingbaxter.gradebook.model.AssignmentType;

public class AssignmentListFragment extends MasterListFragment<Assignment> {

	private static final long serialVersionUID = 1322349121775672944L;

	private AssignmentType assignmentType;
	
	protected AssignmentTypeDao assignmentTypeDao;
	
	protected AssignmentDao assignmentDao;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		setAssignmentTypeFromArgs();
	}

	private void setAssignmentTypeFromArgs() {
		Bundle args = getArguments();
		if( args != null && args.containsKey(BUNDLE_ASSIGNMENT_TYPE)) {
			assignmentType = (AssignmentType) args.get(BUNDLE_ASSIGNMENT_TYPE);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setAssignmentTypeFromArgs();
		
		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No " + assignmentType.getLabel() + "(s)");

		// // We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// FIXME: This should be injected.
		GradebookDbHelper dbHelper = new GradebookDbHelper(getActivity());
		mutableRepository = new SqliteAssignmentDao(dbHelper);
		assignmentTypeDao = new SqliteAssignmentTypeDao(dbHelper);
	}
	
	@Override
	public Loader<Query<Assignment>> onCreateLoader(int id, Bundle args) {
		return new QueryLoader<Assignment>(getActivity(),
				new QueryCreator<Assignment>() {
					public Query<Assignment> createQuery() {
						return ((AssignmentDao) mutableRepository)
								.findByCourseIdAndAssignmentTypeId(
										getGradebookApplication().getSelectedCourse().getId(),
										assignmentType.getId());
					}
				});
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

	@Override
	public void bindView(View view, Assignment item) {
		super.bindView(view, item);
		
		((TextView) view.findViewById(R.id.text_right))
			.setText(String.valueOf(item.getPossiblePoints()));
	}
	
}
