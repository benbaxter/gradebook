package com.benjamingbaxter.gradebook.android;

import android.support.v4.app.ListFragment;

public class GradebookListFragment extends ListFragment {

	public GradebookApplication getGradebookApplication() {
		return (GradebookApplication) getActivity().getApplication();
	}
}
