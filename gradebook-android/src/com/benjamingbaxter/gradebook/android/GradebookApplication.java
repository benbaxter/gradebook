package com.benjamingbaxter.gradebook.android;

import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.Course;

public class GradebookApplication extends Application implements CourseChangeObserver {

	protected static final String LAST_SELECTED_COURSE_ID_PREF = "lastSelectedCourseId";
	
	protected Course selectedCourse;
	private SharedPreferences preferences;
	protected CourseDao courseDao;
	protected Set<CourseChangeObserver> courseChangeObservers;
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		courseDao = new SqliteCourseDao(new GradebookDbHelper(this));
		
		long id = preferences.getLong(LAST_SELECTED_COURSE_ID_PREF, -1);
		if( id == -1 ) {
			Query<Course> query = courseDao.findAll();
			if( query.count() > 0 ) {
				setSelectedCourse(query.get(0));
			}
		} else {
			Course course = courseDao.findById(id);
			if( course != null ) {
				setSelectedCourse(course);
			}
		}
		
		getCourseChangeObservers().add(this);
		
	}
	
	@Override
	public void onCourseChanged() {
		preferences.edit().putLong(LAST_SELECTED_COURSE_ID_PREF, selectedCourse.getId()).commit();
	}
	
	public Set<CourseChangeObserver> getCourseChangeObservers() {
		if( courseChangeObservers == null ) {
			courseChangeObservers = new HashSet<CourseChangeObserver>();
		}
		return courseChangeObservers;
	}
	
	public void addCourseChangeObserver(CourseChangeObserver observer) {
		getCourseChangeObservers().add(observer);
	}
	
	
	public Course getSelectedCourse() {
		return selectedCourse;
	}
	
	public void setSelectedCourse(Course selectedCourse) {
		this.selectedCourse = selectedCourse;
		notifyCourseChanged();
	}

	private void notifyCourseChanged() {
		for (CourseChangeObserver observer : getCourseChangeObservers() ) {
			observer.onCourseChanged();
		}
	}
}
