package com.benjamingbaxter.gradebook.model;

import java.util.Date;

public class GradedAssignment extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private double earnedPoints;
	private String feedback;
	private Course course;
	private Student student;
	private Assignment assignment;
	
	public GradedAssignment() {
		super();
	}
	
	public GradedAssignment(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}

	@Override
	public String display() {
		return student.display() + " " + earnedPoints;
	}
	
	public double getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(double earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getFeedback() {
		return feedback;
	}
	
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
}
