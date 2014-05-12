package com.benjamingbaxter.gradebook.model;

import java.util.Date;

public class AssignmentType extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private String label;
	
	public AssignmentType() {
		super();
	}
	
	public AssignmentType(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}

	@Override
	public String display() {
		return getLabel();
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
}
