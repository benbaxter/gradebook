package com.thescreenapp.dao;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractQuery<T> implements Query<T> {
	private int mCurrentIndex;
	
	public AbstractQuery() {
		mCurrentIndex = -1;
	}

	@Override
	public boolean next() {
		if (mCurrentIndex >= count() - 1) {
			mCurrentIndex = count();
			return false;
		} else {
			mCurrentIndex++;
			return true;
		}
	}

	@Override
	public T current() throws IllegalStateException {
		if (mCurrentIndex < 0 || mCurrentIndex >= count()) {
			throw new IllegalStateException(
					"No object at the current position in the query ("
					+ mCurrentIndex + "); must be between 0 and "
					+ (count() - 1));
		}
		return get(mCurrentIndex);
	}

	@Override
	public List<T> all() {
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < count(); i++) {
			result.add(get(i));
		}
		return result;
	}
}
