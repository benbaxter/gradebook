package com.benjamingbaxter.gradebook.model;

import java.util.Date;

public class Assignment extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private String title;
	private double possiblePoints;
	private double earnedPoints;
	private String feedback;
	private Course course;
	private Student student;
	
	public Assignment() {
		super();
	}
	
	public Assignment(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPossiblePoints() {
		return possiblePoints;
	}

	public void setPossiblePoints(double possiblePoints) {
		this.possiblePoints = possiblePoints;
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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	public String getFeedback() {
		return feedback;
	}
	
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
