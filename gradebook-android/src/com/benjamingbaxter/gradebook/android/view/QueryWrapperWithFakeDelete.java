package com.benjamingbaxter.gradebook.android.view;

import java.util.List;

import com.benjamingbaxter.gradebook.dao.Query;

public class QueryWrapperWithFakeDelete<T> implements Query<T> {
	
	protected Query<T> mDelegate;
	protected int mDeletedIndex;
	
	public QueryWrapperWithFakeDelete(Query<T> delegate, int deletedIndex) {
		mDelegate = delegate;
		mDeletedIndex = deletedIndex;
	}
	
	@Override
	public int count() {
		return mDelegate.count() - 1;
	}

	@Override
	public T get(int index) throws IndexOutOfBoundsException {
		if (index < mDeletedIndex) {
			return mDelegate.get(index);
		}
		return mDelegate.get(index + 1);
	}

	@Override
	public void close() {
		mDelegate.close();
	}

	@Override
	public boolean next() {
		throw new UnsupportedOperationException("Can't support next() on a QueryWrapperWithFakeDelete");
	}

	@Override
	public T current() throws IllegalStateException {
		throw new UnsupportedOperationException("Can't support current() on a QueryWrapperWithFakeDelete");
	}

	@Override
	public List<T> all() {
		List<T> result = mDelegate.all();
		result.remove(mDeletedIndex);
		return result;
	}
}
