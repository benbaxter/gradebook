package com.benjamingbaxter.gradebook.model;

import java.util.Date;

public class AssignmentWeight extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private AssignmentType assignmentType;
	private Double weight;
	private Long courseId;
	
	public AssignmentWeight() {
		super();
	}
	
	public AssignmentWeight(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}

	public AssignmentType getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(AssignmentType assignmentType) {
		this.assignmentType = assignmentType;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

}
