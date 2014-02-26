package com.benjamingbaxter.gradebook.dao;

import java.util.Date;

import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public abstract class AbstractMutableRepository<T extends ScreenModelObject> implements MutableRepository<T> {
	
	@Override
	public void create(T object) {
		object.setUpdateDate(new Date());
		long id = createFilledInObject(object);
		object.setId(id);
	}
	
	@Override
	public void update(T object) {
		object.setUpdateDate(new Date());
		updateFilledInObject(object);
	}
	
	protected abstract long createFilledInObject(T object);
	protected abstract void updateFilledInObject(T object);
}
