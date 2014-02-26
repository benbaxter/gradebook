package com.thescreenapp.android.content;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.thescreenapp.dao.Query;

/**
 * A {@link Loader} that can execute a {@link Query} in the background.
 * 
 * <p>
 * This implementation is based heavily off the {@link AsyncTaskLoader}
 * documentation.
 * </p>
 * 
 * {@see 
 * http://developer.android.com/reference/android/content/AsyncTaskLoader.html}
 */
public class QueryLoader<T> extends AbstractLoader<Query<T>> {
	QueryCreator<T> mQueryCreator;

	public QueryLoader(Context context, QueryCreator<T> queryCreator) {
		super(context);
		mQueryCreator = queryCreator;
	}

	/**
	 * This is where the bulk of our work is done. This function is called in a
	 * background thread and should generate a new set of data to be published
	 * by the loader.
	 */
	@Override
	public Query<T> loadInBackground() {
		Query<T> query = mQueryCreator.createQuery();
		// Make sure the query is actually executed.
		query.count();
		return query;
	}

	@Override
	protected void onReleaseResources(Query<T> query) {
		query.close();
	}
}
