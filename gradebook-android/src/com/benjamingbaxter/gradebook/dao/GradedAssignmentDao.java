package com.benjamingbaxter.gradebook.dao;

import com.benjamingbaxter.gradebook.model.GradedAssignment;

public interface GradedAssignmentDao extends MutableRepository<GradedAssignment> {

	public Query<GradedAssignment> findByCourseIdAndAssignmentIdAndStudentId
			(Long courseId, Long assId, Long studentId);
	
}
