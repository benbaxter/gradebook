package com.benjamingbaxter.gradebook.android.widget;

import android.view.View;

/**
 * A read-only binding that sets the fields in a view from an object.
 * 
 * @author eric
 */
public interface ViewBinding<V extends View, T> {
	/**
	 * Set the fields in the view from the values of the fields in the object.
	 * 
	 * @param view    The view to populate
	 * @param object  The object to read from
	 */
	void bindView(V view, T object);
}
