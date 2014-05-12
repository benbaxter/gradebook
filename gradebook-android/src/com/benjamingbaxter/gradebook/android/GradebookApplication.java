package com.benjamingbaxter.gradebook.android;

import java.util.HashSet;
import java.util.Set;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.benjamingbaxter.gradebook.android.dao.GradebookDbHelper;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentTypeDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteAssignmentWeightDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteCourseDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteGradedAssignmentDao;
import com.benjamingbaxter.gradebook.android.dao.SqliteStudentDao;
import com.benjamingbaxter.gradebook.dao.AssignmentDao;
import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.AssignmentWeightDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.GradedAssignmentDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.dao.StudentDao;
import com.benjamingbaxter.gradebook.model.Course;

public class GradebookApplication extends Application implements CourseChangeObserver {

	protected static final String LAST_SELECTED_COURSE_ID_PREF = "lastSelectedCourseId";
	
	protected Course selectedCourse;
	private SharedPreferences preferences;
	protected CourseDao courseDao;
	protected AssignmentDao assignmentDao;
	protected AssignmentTypeDao assignmentTypeDao;
	protected AssignmentWeightDao assignmentWeightDao;
	protected GradedAssignmentDao gradedAssignmentDao;
	protected StudentDao studentDao;
	
	protected Set<CourseChangeObserver> courseChangeObservers;
	
	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		GradebookDbHelper dbHelper = new GradebookDbHelper(this);
		courseDao = new SqliteCourseDao(dbHelper);
		assignmentDao = new SqliteAssignmentDao(dbHelper);
		assignmentTypeDao = new SqliteAssignmentTypeDao(dbHelper);
		assignmentWeightDao = new SqliteAssignmentWeightDao(dbHelper);
		gradedAssignmentDao = new SqliteGradedAssignmentDao(dbHelper, assignmentDao);
		studentDao = new SqliteStudentDao(dbHelper);
		
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
	
	
	public AssignmentDao getAssignmentDao() {
		return assignmentDao;
	}
	
	public AssignmentTypeDao getAssignmentTypeDao() {
		return assignmentTypeDao;
	}
	
	public AssignmentWeightDao getAssignmentWeightDao() {
		return assignmentWeightDao;
	}
	
	public CourseDao getCourseDao() {
		return courseDao;
	}
	
	public GradedAssignmentDao getGradedAssignmentDao() {
		return gradedAssignmentDao;
	}
	
	public StudentDao getStudentDao() {
		return studentDao;
	}
}
