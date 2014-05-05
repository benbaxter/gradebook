package com.benjamingbaxter.gradebook.android;

import android.support.v4.app.FragmentActivity;

public class GradebookFragmentActivity extends FragmentActivity {

	public GradebookApplication getGradebookApplication() {
		return (GradebookApplication) getApplication();
	}
}
