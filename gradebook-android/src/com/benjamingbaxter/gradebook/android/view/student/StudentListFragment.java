package com.benjamingbaxter.gradebook.android.view.student;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.content.QueryCreator;
import com.benjamingbaxter.gradebook.android.content.QueryLoader;
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteStudentDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Student;

public class StudentListFragment extends MasterListFragment<Student>
		implements
		 OnQueryTextListener,
		 OnCloseListener {

	private static final long serialVersionUID = 1322349121775672944L;

	 // The SearchView for doing filtering.
	 SearchView mSearchView;
	
	 // If non-null, this is the current filter the user has provided.
	 String mCurFilter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No Students");

		// // We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// FIXME: This should be injected.
		mutableRepository = new SqliteStudentDao(
				new GradebookDbHelper(getActivity()));
	}

	public static class MySearchView extends SearchView {
		public MySearchView(Context context) {
			super(context);
		}

		// The normal SearchView doesn't clear its search text when
		// collapsed, so we will do this for it.
		@Override
		public void onActionViewCollapsed() {
			setQuery("", false);
			super.onActionViewCollapsed();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.student, menu);

		// Place an action bar item for searching.
		MenuItem item = menu.add("Search");
		item.setIcon(android.R.drawable.ic_menu_search);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		mSearchView = new MySearchView(getActivity());
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setOnCloseListener(this);
		mSearchView.setIconifiedByDefault(true);
		item.setActionView(mSearchView);
		
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

	 public boolean onQueryTextChange(String newText) {
		 // Called when the action bar search text has changed. Update
		 // the search filter, and restart the loader to do a new query
		 // with this filter.
		 String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
		 // Don't do anything if the filter hasn't actually changed.
		 // Prevents restarting the loader when restoring state.
		 if (mCurFilter == null && newFilter == null) {
			 return true;
		 }
		 if (mCurFilter != null && mCurFilter.equals(newFilter)) {
			 return true;
		 }
		 mCurFilter = newFilter;
		 getLoaderManager().restartLoader(0, null, this);
		 return true;
	 }
	
	 @Override
	 public boolean onQueryTextSubmit(String query) {
		 // Don't care about this.
		 return true;
	 }
	
	 @Override
	 public boolean onClose() {
		 if (!TextUtils.isEmpty(mSearchView.getQuery())) {
			 mSearchView.setQuery(null, true);
		 } 
		 return true;
	 }
	 

	@Override
	public Loader<Query<Student>> onCreateLoader(int id, Bundle args) {
		return new QueryLoader<Student>(getActivity(),
			new QueryCreator<Student>() {
				public Query<Student> createQuery() {
					if( mCurFilter == null || mCurFilter.isEmpty()) {
						return mutableRepository.findAll();
					} else {
						return mutableRepository.findByDisplayCriteria(mCurFilter);
					}
				}
			});
	}
}
