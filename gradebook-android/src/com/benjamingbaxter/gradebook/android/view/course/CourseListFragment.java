package com.benjamingbaxter.gradebook.android.view.course;

import java.util.HashSet;
import java.util.Set;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.content.QueryCreator;
import com.benjamingbaxter.gradebook.android.content.QueryLoader;
import com.benjamingbaxter.gradebook.android.dao.ScreenDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.view.QueryWrapperWithFakeDelete;
import com.benjamingbaxter.gradebook.android.view.SwipeDismissListViewTouchListener;
import com.benjamingbaxter.gradebook.android.widget.QueryAdapter;
import com.benjamingbaxter.gradebook.android.widget.ViewBinding;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Course;
import com.jensdriller.libs.undobar.UndoBar;

public class CourseListFragment extends MasterListFragment
		implements
		ViewBinding<View, Course>,
		LoaderManager.LoaderCallbacks<Query<Course>> {

	private static final long serialVersionUID = 1322349121775672944L;

	protected static final int OUR_LOADER_ID = 0;
	
	protected CourseDao courseDao;

	// This is the Adapter being used to display the list's data.
	protected QueryAdapter<Course> mAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Give some text to display if there is no data. In a real
		// application this would come from a resource.
		setEmptyText("No Courses");

		// // We have a menu item to show in action bar.
		setHasOptionsMenu(true);

		// FIXME: This should be injected.
		courseDao = new SqliteCourseDao(
				new ScreenDbHelper(getActivity()));

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new QueryAdapter<Course>(getActivity(), null,
				R.layout.list_item_course, this);
		setListAdapter(mAdapter);

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				getListView(),
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						Query<Course> query = mAdapter.getQuery();
						final Set<Long> deletedIds = new HashSet<Long>();
						for (int position : reverseSortedPositions) {
							Course course = mAdapter.getItem(position);
							courseDao.delete(course);
							deletedIds.add(course.getId());
							query = new QueryWrapperWithFakeDelete<Course>(query, position);
						}
						mAdapter.swapQuery(query);
						//TODO: FIXME!
						//if there are any items left after deleting one, 
						//default to first? Safest option. What if they
						//delete the item they are looking at? Move the details
						//to whom? Defaulting to first until there is clarity.
						if( mAdapter.getCount() > 0 ) {
							openDetailsIfScreenSizeIsBigEnough(mAdapter.getItem(0));
						} else {
							//if no one is left, time to add a new guy.
							openAddDetailsIfScreenSizeIsBigEnough();
						}
						getLoaderManager().restartLoader(OUR_LOADER_ID, null, CourseListFragment.this);
						new UndoBar.Builder(getActivity())
							.setMessage(reverseSortedPositions.length  + " candidate(s) deleted")
							.setListener(new UndoBar.Listener() {
								@Override
								public void onUndo(Parcelable token) {
									for (Long id : deletedIds) {
										courseDao.restore(id);
									}
									//this will open a new query for us. 
									//We just need to make sure the candidates are restored. 
									getLoaderManager().restartLoader(OUR_LOADER_ID, null, CourseListFragment.this);
								}
								@Override
								public void onHide() {
									//do not have to do anything since it is already marked for deletion. 
								}
							})
							.show(true);
					}
				});
		getListView().setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling, we don't look for swipes.
		getListView().setOnScrollListener(touchListener.makeScrollListener());

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(OUR_LOADER_ID, null, this);
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
		openDetails(mAdapter.getItem(position));
	}

	@Override
	public Loader<Query<Course>> onCreateLoader(int id, Bundle args) {
		return new QueryLoader<Course>(getActivity(),
				new QueryCreator<Course>() {
					public Query<Course> createQuery() {
						return courseDao.findAll();
					}
				});
	}

	@Override
	public void onLoadFinished(Loader<Query<Course>> loader,
			Query<Course> query) {
		// Swap the new query in. (The loader will take care of closing
		// the old query once we return.)
		mAdapter.swapQuery(query);
		
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		
		//load the first detail if needed as well
		if( mAdapter.getCount() > 0 ) {
			initDetail(mAdapter.getItem(0));
		} else {
			openAddDetails();
		}
	}

	@Override
	public void onLoaderReset(Loader<Query<Course>> loader) {
		// This is called when the last Query provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapQuery(null);
	}

	@Override
	public void bindView(View view, Course course) {
		((TextView) view.findViewById(R.id.text_title)).setText(course
				.getTitle());
		((TextView) view.findViewById(R.id.text_section)).setText(course
				.getSection());
	}
	
	@Override
	protected void doUpdate() {
		getLoaderManager().restartLoader(OUR_LOADER_ID, null, CourseListFragment.this);
	}
}
