package com.benjamingbaxter.gradebook.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Assignment extends BasicModelObject {
	
	private static final long serialVersionUID = 6563904437523765926L;
	private String title;
	private double possiblePoints;
	private Course course;
	private Set<Student> students;
	private AssignmentType type;
	
	public Assignment() {
		super();
		students = new HashSet<Student>();
	}
	
	public Assignment(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
		students = new HashSet<Student>();
	}

	@Override
	public String display() {
		return getTitle();
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


	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void addStudent(Student student) {
		students.add(student);
	}
	
	public void removeStudent(Student student) {
		if (students.contains(student)) {
			students.remove(student);
		}
	}
	
	public Iterable<Student> getStudents() {
		if( students == null ) {
			students = new HashSet<Student>();
		}
		return students;
	}
	
	public int getStudentCount() {
		return students.size();
	}

	public AssignmentType getType() {
		return type;
	}
	public void setType(AssignmentType type) {
		this.type = type;
	}
	
}
