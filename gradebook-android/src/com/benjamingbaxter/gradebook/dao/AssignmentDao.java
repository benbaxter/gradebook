package com.benjamingbaxter.gradebook.dao;

import com.benjamingbaxter.gradebook.model.Assignment;

public interface AssignmentDao extends MutableRepository<Assignment> {

	public Query<Assignment> findByCourseId(Long id);
	
	public Query<Assignment> findByCourseIdAndAssignmentTypeId(Long courseId, Long assId);
	
	public void deleteByCourseId(Long id);
	
}
