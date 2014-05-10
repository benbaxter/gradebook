package com.benjamingbaxter.gradebook.android.view.assignment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
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
import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentTypeDao;
import com.benjamingbaxter.gradebook.android.view.MasterListFragment;
import com.benjamingbaxter.gradebook.android.widget.QueryAdapter;
import com.benjamingbaxter.gradebook.android.widget.ViewBinding;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.ListQuery;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Assignment;
import com.benjamingbaxter.gradebook.model.AssignmentType;

public class AssignmentListFragment extends MasterListFragment
		implements
		// OnQueryTextListener,
		// OnCloseListener,
		ViewBinding<View, Assignment>,
		LoaderManager.LoaderCallbacks<Query<Assignment>> {

	private static final long serialVersionUID = 1322349121775672944L;

	public static final String BUNDLE_ASSIGNMENT_TYPE = "bundle_assignment";
	
	private AssignmentType assignmentType;
	
	protected static final int OUR_LOADER_ID = 0;
	
	protected AssignmentTypeDao assignmentTypeDao;

	// This is the Adapter being used to display the list's data.
	protected QueryAdapter<Assignment> mAdapter;

	// // The SearchView for doing filtering.
	// SearchView mSearchView;
	//
	// // If non-null, this is the current filter the user has provided.
	// String mCurFilter;

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
		assignmentTypeDao = new SqliteAssignmentTypeDao(new GradebookDbHelper(getActivity()));

		// Create an empty adapter we will use to display the loaded data.
//		mAdapter = new QueryAdapter<Assignment>(getActivity(), null,
//				android.R.layout.simple_list_item_1, this);
//		setListAdapter(mAdapter);

//		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
//				getListView(),
//				new SwipeDismissListViewTouchListener.DismissCallbacks() {
//					@Override
//					public boolean canDismiss(int position) {
//						return true;
//					}
//
//					@Override
//					public void onDismiss(ListView listView,
//							int[] reverseSortedPositions) {
//						Query<Assignment> query = mAdapter.getQuery();
//						final Set<Long> deletedIds = new HashSet<Long>();
//						for (int position : reverseSortedPositions) {
//							Student student = mAdapter.getItem(position);
//							assignmentTypeDao.delete(student);
//							deletedIds.add(student.getId());
//							query = new QueryWrapperWithFakeDelete<Assignment>(query, position);
//						}
//						mAdapter.swapQuery(query);
//						//TODO: FIXME!
//						//if there are any items left after deleting one, 
//						//default to first? Safest option. What if they
//						//delete the item they are looking at? Move the details
//						//to whom? Defaulting to first until there is clarity.
//						if( mAdapter.getCount() > 0 ) {
//							openDetailsIfScreenSizeIsBigEnough(mAdapter.getItem(0));
//						} else {
//							//if no one is left, time to add a new guy.
//							openAddDetailsIfScreenSizeIsBigEnough();
//						}
//						getLoaderManager().restartLoader(OUR_LOADER_ID, null, AssignmentListFragment.this);
////						mAdapter.notifyDataSetChanged();
//						new UndoBar.Builder(getActivity())
//							.setMessage(reverseSortedPositions.length  + " student(s) deleted")
//							.setListener(new UndoBar.Listener() {
//								@Override
//								public void onUndo(Parcelable token) {
//									for (Long id : deletedIds) {
//										studentDao.restore(id);
//									}
//									//this will open a new query for us. 
//									//We just need to make sure the candidates are restored. 
//									getLoaderManager().restartLoader(OUR_LOADER_ID, null, AssignmentListFragment.this);
//								}
//								@Override
//								public void onHide() {
//									//do not have to do anything since it is already marked for deletion. 
//								}
//							})
//							.show(true);
//					}
//				});
//		getListView().setOnTouchListener(touchListener);
//		// Setting this scroll listener is required to ensure that during
//		// ListView scrolling, we don't look for swipes.
//		getListView().setOnScrollListener(touchListener.makeScrollListener());

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(OUR_LOADER_ID, null, this);
	}

//	public static class MySearchView extends SearchView {
//		public MySearchView(Context context) {
//			super(context);
//		}
//
//		// The normal SearchView doesn't clear its search text when
//		// collapsed, so we will do this for it.
//		@Override
//		public void onActionViewCollapsed() {
//			setQuery("", false);
//			super.onActionViewCollapsed();
//		}
//	}
//
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// Place an action bar item for searching.
//		MenuItem item = menu.add("Search");
//		item.setIcon(android.R.drawable.ic_menu_search);
//		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
//				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
//		mSearchView = new MySearchView(getActivity());
//		mSearchView.setOnQueryTextListener(this);
//		mSearchView.setOnCloseListener(this);
//		mSearchView.setIconifiedByDefault(true);
//		item.setActionView(mSearchView);
//	}

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

	//
	// public boolean onQueryTextChange(String newText) {
	// // Called when the action bar search text has changed. Update
	// // the search filter, and restart the loader to do a new query
	// // with this filter.
	// String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
	// // Don't do anything if the filter hasn't actually changed.
	// // Prevents restarting the loader when restoring state.
	// if (mCurFilter == null && newFilter == null) {
	// return true;
	// }
	// if (mCurFilter != null && mCurFilter.equals(newFilter)) {
	// return true;
	// }
	// mCurFilter = newFilter;
	// getLoaderManager().restartLoader(0, null, this);
	// return true;
	// }
	//
	// @Override
	// public boolean onQueryTextSubmit(String query) {
	// // Don't care about this.
	// return true;
	// }
	//
	// @Override
	// public boolean onClose() {
	// if (!TextUtils.isEmpty(mSearchView.getQuery())) {
	// mSearchView.setQuery(null, true);
	// }
	// return true;
	// }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Insert desired behavior here.
		Log.i("FragmentComplexList", "Item clicked: " + id);
		openDetails(mAdapter.getItem(position));
	}

	@Override
	public Loader<Query<Assignment>> onCreateLoader(int id, Bundle args) {
		return new QueryLoader<Assignment>(getActivity(),
				new QueryCreator<Assignment>() {
					public Query<Assignment> createQuery() {
//						return studentDao.findAllForCourse(getGradebookApplication().getSelectedCourse());
						return new ListQuery<Assignment>(new ArrayList<Assignment>());
					}
				});
	}

	@Override
	public void onLoadFinished(Loader<Query<Assignment>> loader,
			Query<Assignment> query) {
		// Swap the new query in. (The loader will take care of closing
		// the old query once we return.)
//		mAdapter.swapQuery(query);
//		
//		// The list should now be shown.
//		if (isResumed()) {
//			setListShown(true);
//		} else {
//			setListShownNoAnimation(true);
//		}
//		
//		//load the first detail if needed as well
//		if( mAdapter.getCount() > 0 ) { 
//			initDetail(mAdapter.getItem(0));
//		} else {
//			openAddDetails();
//		}
	}

	@Override
	public void onLoaderReset(Loader<Query<Assignment>> loader) {
		// This is called when the last Query provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
//		mAdapter.swapQuery(null);
	}

	@Override
	public void bindView(View view, Assignment assignment) {
		((TextView) view.findViewById(android.R.id.text1)).setText(assignment
				.getTitle());
	}
	
	@Override
	protected void doUpdate() {
//		getLoaderManager().restartLoader(OUR_LOADER_ID, null, AssignmentListFragment.this);
	}
}
