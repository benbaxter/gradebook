package com.benjamingbaxter.gradebook.dao;

import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.Student;

public interface StudentDao extends MutableRepository<Student> {

	public Query<Student> findAllForCourse(Course course);
}
