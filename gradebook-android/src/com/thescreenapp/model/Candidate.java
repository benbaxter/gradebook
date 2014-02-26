package com.thescreenapp.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Candidate extends BasicScreenModelObject {
	private String mFirstName;
	private String mLastName;
	private String mPhoneNumber;
	private int mRating;
	private String mEmail;
	private Set<Interview> mInterviews = new HashSet<Interview>();
	
	public Candidate() {
		super();
	}
	
	public Candidate(long id, String uuid, Date creationDate) {
		super(id, uuid, creationDate);
	}
	
	public String getFirstName() {
		return mFirstName;
	}
	
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}
	
	public String getLastName() {
		return mLastName;
	}
	
	public void setLastName(String lastName) {
		mLastName = lastName;
	}
	
	public String getFullName() {
		if (mFirstName != null) {
			if (mLastName != null) {
				return mFirstName + " " + mLastName;
			} else {
				return mFirstName;
			}
		} else {
			if (mLastName != null) {
				return mLastName;
			} else {
				return "";
			}
		}
	}
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	public int getRating() {
		return mRating;
	}
	
	public void setRating(int rating) {
		mRating = rating;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}
	
	public void addInterview(Interview interview) {
		interview.setCandidate(this);
		mInterviews.add(interview);
	}
	
	public void removeInterview(Interview interview) {
		if (mInterviews.contains(interview)) {
			interview.setCandidate(null);
			mInterviews.remove(interview);
		}
	}
	
	public Iterable<Interview> getInterviews() {
		return mInterviews;
	}
	
	public int getInterviewCount() {
		return mInterviews.size();
	}
}
