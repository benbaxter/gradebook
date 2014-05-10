package com.benjamingbaxter.gradebook.model;

import java.util.Date;
import java.util.Set;

public class Course extends BasicModelObject {

	private static final long serialVersionUID = -167518089111992778L;
	
	private String title;
	private String section;
	private String semester; //fall, spring, summer, winter?
	private String year;
	private Set<Student> students;
	private Set<Assignment> assignments;
	private Set<AssignmentWeight> assignmentWeights;
	
	public Course() {
		super();
	}
	
	public Course(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}
	
	public String getCourseName() {
		StringBuilder sb = new StringBuilder(title);
		if( section != null && ! section.trim().isEmpty() ) {
			sb.append(" \u2014 ");
			sb.append(section);
		}
		return sb.toString();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void addStudent(Student student) {
		student.setCourse(this);
		students.add(student);
	}
	
	public void removeStudent(Student student) {
		if (students.contains(student)) {
			student.setCourse(null);
			students.remove(student);
		}
	}
	
	public Iterable<Student> getStudents() {
		return students;
	}
	
	public int getStudentCount() {
		return students.size();
	}

	public void addAssignment(Assignment assignment) {
		assignment.setCourse(this);
		assignments.add(assignment);
	}
	
	public void removeAssignment(Assignment assignment) {
		if (assignments.contains(assignment)) {
			assignment.setCourse(null);
			for (Student student : students) {
				student.removeAssignment(assignment);
			}
			assignments.remove(assignment);
		}
	}
	
	public Iterable<Assignment> getAssignments() {
		return assignments;
	}
	
	public int getAssignmentCount() {
		return assignments.size();
	}
	
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public Set<AssignmentWeight> getAssignmentWeights() {
		return assignmentWeights;
	}
	public void setAssignmentWeights(Set<AssignmentWeight> assignmentWeights) {
		this.assignmentWeights = assignmentWeights;
	}
	
}
