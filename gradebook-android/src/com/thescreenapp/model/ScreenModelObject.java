package com.thescreenapp.model;

import java.io.Serializable;
import java.util.Date;

public interface ScreenModelObject extends Serializable {
	long getId();
	void setId(long id);
	String getUuid();
	Date getCreationDate();
	Date getUpdateDate();
	void setUpdateDate(Date updateDate);
	boolean isDeleted();
	void setDeleted(boolean deleted);
}
