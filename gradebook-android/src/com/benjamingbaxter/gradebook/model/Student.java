package com.benjamingbaxter.gradebook.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Student extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private String firstName;
	private String lastName;
	private String email;
	private Course course;
	private Set<Assignment> assignments;
	
	public Student() {
		super();
		assignments = new HashSet<Assignment>();
	}
	
	public Student(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
		assignments = new HashSet<Assignment>();
	}
	
	@Override
	public String display() {
		return getFullName();
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFullName() {
		if (firstName != null) {
			if (lastName != null) {
				return firstName + " " + lastName;
			} else {
				return firstName;
			}
		} else {
			if (lastName != null) {
				return lastName;
			} else {
				return "";
			}
		}
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public Assignment getAssignment(Assignment assignment) {
		for (Assignment ass : assignments) {
			if( ass.equals(assignment) ) {
				return ass;
			}
		}
		return null;
	}
	
	public void addAssignment(Assignment assignment) {
		assignment.addStudent(this);
		assignments.add(assignment);
	}
	
	public void removeAssignment(Assignment assignment) {
		if (assignments.contains(assignment)) {
			assignment.removeStudent(this);
			assignments.remove(assignment);
		}
	}
	
	public Iterable<Assignment> getAssignments() {
		return assignments;
	}
	
	public int getAssignmentCount() {
		return assignments.size();
	}
	
}
