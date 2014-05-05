package com.benjamingbaxter.gradebook.android;

import android.support.v4.app.Fragment;

public class GradebookFragment extends Fragment {

	public GradebookApplication getGradebookApplication() {
		return (GradebookApplication) getActivity().getApplication();
	}
}
