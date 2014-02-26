package com.benjamingbaxter.gradebook.model;

import java.util.Date;
import java.util.UUID;

public class BasicModelObject implements ScreenModelObject {
	private static final long serialVersionUID = 6166367202096070926L;
	private long mId;
	private String mUuid;
	private Date mCreationDate;
	private Date mUpdateDate;
	private boolean mDeleted;
	
	public BasicModelObject() {
		mUuid = UUID.randomUUID().toString();
		mCreationDate = new Date();
		mDeleted = false;
	}
	
	public BasicModelObject(long id, String uuid, Date creationDate) {
		mId = id;
		mUuid = uuid;
		mCreationDate = creationDate;
	}
	
	@Override
	public long getId() {
		return mId;
	}

	@Override
	public void setId(long id) {
		mId = id;
	}

	@Override
	public String getUuid() {
		return mUuid;
	}
	
	@Override
	public Date getCreationDate() {
		return mCreationDate;
	}

	@Override
	public Date getUpdateDate() {
		return mUpdateDate;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		mUpdateDate = updateDate;
	}

	@Override
	public boolean isDeleted() {
		return mDeleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		mDeleted = deleted;
	}
}
