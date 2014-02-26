package com.thescreenapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thescreenapp.model.ScreenModelObject;

public class CachedMutableRepository<T extends ScreenModelObject> implements MutableRepository<T> {
	protected MutableRepository<T> mDelegate;
	private Map<Long, T> mIdToObjectMap = new HashMap<Long, T>();
	private Map<String, T> mUuidToObjectMap = new HashMap<String, T>();
	
	public CachedMutableRepository(MutableRepository<T> delegate) {
		mDelegate = delegate;
		List<T> objects = delegate.findAll().all();
		for (T object : objects) {
			mIdToObjectMap.put(object.getId(), object);
			mUuidToObjectMap.put(object.getUuid(), object);
		}
	}

	@Override
	public T findById(long id) {
		T object = mIdToObjectMap.get(id);
		if (object == null) {
			object = mDelegate.findById(id);
			if (object != null) {
				mIdToObjectMap.put(id, object);
			}
		}
		return object;
	}

	@Override
	public T findByUuid(String uuid) {
		T object = mUuidToObjectMap.get(uuid);
		if (object == null) {
			object = mDelegate.findByUuid(uuid);
			if (object != null) {
				mUuidToObjectMap.put(uuid, object);
			}
		}
		return object;
	}

	@Override
	public Query<T> findAll() {
		List<T> nonDeletedObjects = new ArrayList<T>();
		for (T object : mIdToObjectMap.values()) {
			if (!object.isDeleted()) {
				nonDeletedObjects.add(object);
			}
		}
		return new ListQuery<T>(nonDeletedObjects);
	}

	@Override
	public Query<T> findAllIncludingDeleted() {
		return new ListQuery<T>(mIdToObjectMap.values().iterator());
	}
	
	@Override
	public void create(T object) {
		mDelegate.create(object);
		mIdToObjectMap.put(object.getId(), object);
		mUuidToObjectMap.put(object.getUuid(), object);
	}
	
	@Override
	public void update(T object) {
		mDelegate.update(object);
		mIdToObjectMap.put(object.getId(), object);
		mUuidToObjectMap.put(object.getUuid(), object);
	}
	
	@Override
	public void delete(T object) {
		object.setDeleted(true);
	}
	
	@Override
	public void restore(long id) {
		T object = findById(id);
		if (object != null) {
			object.setDeleted(false);
		}
	}

	@Override
	public void purge(T object) {
		mDelegate.delete(object);
		mIdToObjectMap.remove(object.getId());
		mUuidToObjectMap.remove(object.getUuid());
	}
}
