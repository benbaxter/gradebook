package com.thescreenapp.model;

import java.util.Date;

public class Interview extends BasicScreenModelObject {
	private String mLocation;
	private Date mInterviewDate;
	private int mDurationInMinutes;
	private Candidate mCandidate;
	
	public Interview() {
		super();
	}
	
	public Interview(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}
	
	public String getLocation() {
		return mLocation;
	}
	
	public void setLocation(String location) {
		mLocation = location;
	}
	
	public Date getInterviewDate() {
		return mInterviewDate;
	}
	
	public void setInterviewDate(Date interviewDate) {
		mInterviewDate = interviewDate;
	}
	
	public int getDurationInMinutes() {
		return mDurationInMinutes;
	}

	public void setDurationInMinutes(int durationInMinutes) {
		mDurationInMinutes = durationInMinutes;
	}

	public Candidate getCandidate() {
		return mCandidate;
	}
	
	public void setCandidate(Candidate candidate) {
		mCandidate = candidate;
	}
}
