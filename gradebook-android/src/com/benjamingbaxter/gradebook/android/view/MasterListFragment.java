package com.benjamingbaxter.gradebook.android.view;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.benjamingbaxter.gradebook.android.GradebookListFragment;
import com.benjamingbaxter.gradebook.android.R;
import com.benjamingbaxter.gradebook.android.content.QueryCreator;
import com.benjamingbaxter.gradebook.android.content.QueryLoader;
import com.benjamingbaxter.gradebook.android.view.DetailsFragment.MasterListener;
import com.benjamingbaxter.gradebook.android.widget.QueryAdapter;
import com.benjamingbaxter.gradebook.android.widget.ViewBinding;
import com.benjamingbaxter.gradebook.dao.MutableRepository;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;
import com.jensdriller.libs.undobar.UndoBar;

public abstract class MasterListFragment<T extends ScreenModelObject> extends GradebookListFragment 
	implements Serializable, 
				MasterListener, 
				ViewBinding<View, T>, 
				LoaderManager.LoaderCallbacks<Query<T>> {
	
	private static final long serialVersionUID = 5626896934033946319L;
	public final static String EXTRA_DETAILS_LISTENER = MasterListFragment.class.getPackage().getName() + "MasterListFragment.EXTRA_DETAILS_LISTENER";
	
	public static interface DetailsListener<T extends ScreenModelObject> {
		void initDetail(T detail);
		void openDetails(T detail);
		void openDetailsIfScreenSizeIsBigEnough(T detail);
		void openAddDetails();
		void openAddDetailsIfScreenSizeIsBigEnough();
		void onUpdateListUpdated();
	}
	
	protected DetailsListener<T> mDetailsListener;
	
	protected static final int OUR_LOADER_ID = 0;

	// This is the Adapter being used to display the list's data.
	protected QueryAdapter<T> mAdapter;
	
	protected MutableRepository<T> mutableRepository;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new QueryAdapter<T>(getActivity(), null,
				R.layout.master_list_item, this);
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
						Query<T> query = mAdapter.getQuery();
						final Set<Long> deletedIds = new HashSet<Long>();
						for (int position : reverseSortedPositions) {
							T item = mAdapter.getItem(position);
							mutableRepository.delete(item);
							deletedIds.add(item.getId());
							query = new QueryWrapperWithFakeDelete<T>(query, position);
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
						//update the navbar menu and this list on delete
						update();
						new UndoBar.Builder(getActivity())
							.setMessage(reverseSortedPositions.length  + " candidate(s) deleted")
							.setListener(new UndoBar.Listener() {
								@Override
								public void onUndo(Parcelable token) {
									for (Long id : deletedIds) {
										mutableRepository.restore(id);
									}
									//this will open a new query for us. 
									//We just need to make sure the candidates are restored. 

									//update the navbar menu and this list on undo
									update();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Bundle args = getArguments();
		if( args != null && args.containsKey(EXTRA_DETAILS_LISTENER) ) {
			Object obj = args.get(EXTRA_DETAILS_LISTENER);
			if ( obj instanceof DetailsListener ) {
				mDetailsListener = (DetailsListener<T>) obj;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setArguments(Bundle bundle) {
		super.setArguments(bundle);
		Object obj = bundle.get(EXTRA_DETAILS_LISTENER);
		if ( obj instanceof DetailsListener ) {
			mDetailsListener = (DetailsListener<T>) obj;
		}
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		openDetails(mAdapter.getItem(position));
	}
	
	public void initDetail(T detail) {
		mDetailsListener.initDetail(detail);
	}
	
	public void openDetails(T detail) {
		mDetailsListener.openDetails(detail);
	}
	
	public void openDetailsIfScreenSizeIsBigEnough(T detail) {
		mDetailsListener.openDetailsIfScreenSizeIsBigEnough(detail);
	}
	
	public void openAddDetails() {
		mDetailsListener.openAddDetails();
	}
	
	public void openAddDetailsIfScreenSizeIsBigEnough() {
		mDetailsListener.openAddDetailsIfScreenSizeIsBigEnough();
	}
	
	public void update() {
		mDetailsListener.onUpdateListUpdated();
		getLoaderManager().restartLoader(OUR_LOADER_ID, null, this);
	}

	@Override
	public Loader<Query<T>> onCreateLoader(int id, Bundle args) {
		return new QueryLoader<T>(getActivity(),
				new QueryCreator<T>() {
					public Query<T> createQuery() {
						return mutableRepository.findAll();
					}
				});
	}

	@Override
	public void onLoadFinished(Loader<Query<T>> loader,
			Query<T> query) {
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
	public void onLoaderReset(Loader<Query<T>> loader) {
		// This is called when the last Query provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapQuery(null);
	}

	@Override
	public void bindView(View view, T item) {
		((TextView) view.findViewById(R.id.text_title)).setText(item.display());
	}

}
