package com.benjamingbaxter.gradebook.model;

import java.util.Date;

public class AssignmentType extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private String type;
	private double weight;
	private Assignment assignment;
	
	public AssignmentType() {
		super();
	}
	
	public AssignmentType(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}
	
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}

}
