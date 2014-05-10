package com.benjamingbaxter.gradebook.dao;

import com.benjamingbaxter.gradebook.model.AssignmentWeight;

public interface AssignmentWeightDao extends MutableRepository<AssignmentWeight> {

	public Query<AssignmentWeight> findByCourseId(Long id);
	
	public void deleteByCourseId(Long id);
	
}
