package com.thescreenapp.android.content;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * An abstract implementation of a {@link Loader} that contains the logic
 * that most implementations will require.
 * 
 * <p>
 * This implementation is based heavily off the {@link AsyncTaskLoader}
 * documentation.
 * </p>
 * 
 * {@see http://developer.android.com/reference/android/content/AsyncTaskLoader.html}
 */
public abstract class AbstractLoader<T> extends AsyncTaskLoader<T> {
	T mData;

	public AbstractLoader(Context context) {
		super(context);
		mData = null;
	}

	/**
	 * Called when there is new data to deliver to the client. The super class
	 * will take care of delivering it; the implementation here just adds a
	 * little more logic.
	 */
	@Override
	public void deliverResult(T data) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}

		T oldData = mData;
		mData = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldData' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldData != null) {
			onReleaseResources(oldData);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (mData != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mData);
		}

		if (takeContentChanged() || mData == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}
	
	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(T data) {
		super.onCanceled(data);

		// At this point we can release the resources associated with 'query'
		// if needed.
		onReleaseResources(data);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'mQuery'
		// if needed.
		if (mData != null) {
			onReleaseResources(mData);
			mData = null;
		}
	}

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
	protected void onReleaseResources(T data) {
	}
}
