package com.benjamingbaxter.gradebook.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListQuery<T> extends AbstractQuery<T> {
	private List<T> mList;
	
	public ListQuery(List<T> list) {
		mList = list;
	}
	
	public ListQuery(Iterator<T> iterator) {
		mList = new ArrayList<T>();
		while (iterator.hasNext()) {
			mList.add(iterator.next());
		}
	}

	@Override
	public int count() {
		return mList.size();
	}

	@Override
	public T get(int index) throws IndexOutOfBoundsException {
		return mList.get(index);
	}

	@Override
	protected boolean doMoveToFirst() {
		// No need to do anything
		return true;
	}
	
	@Override
	public void close() {
		// No need to do anything
	}
}
