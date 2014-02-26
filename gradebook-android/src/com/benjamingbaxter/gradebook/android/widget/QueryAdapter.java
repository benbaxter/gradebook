package com.benjamingbaxter.gradebook.android.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.ScreenModelObject;

public class QueryAdapter<T extends ScreenModelObject> extends BaseAdapter {
	private Query<T> mQuery;
	private int mResource;
    private LayoutInflater mInflater;
    private ViewBinding<View, ? super T> mViewBinding;
	
	public QueryAdapter(Context context, Query<T> query, int resource, ViewBinding<View, ? super T> viewBinding) {
		mQuery = query;
		mResource = resource;
		mViewBinding = viewBinding;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
    @Override
    public boolean hasStableIds() {
        return true;
    }
	
	@Override
	public int getCount() {
		return mQuery == null ? 0 : mQuery.count();
	}

	@Override
	public T getItem(int position) {
		return mQuery == null ? null : mQuery.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mQuery == null ? 0 : mQuery.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mQuery == null) {
			throw new IllegalStateException("No query is set");
		}
		
        View view;
        if (convertView == null) {
        	view = mInflater.inflate(mResource, parent, false);
        } else {
        	view = convertView;
        }

        T object = mQuery.get(position);
        mViewBinding.bindView(view, object);

        return view;
	}
	
	public Query<T> swapQuery(Query<T> newQuery) {
		if (newQuery == mQuery) {
			return null;
		} else {
			Query<T> oldQuery = mQuery;
			mQuery = newQuery;
			if (newQuery == null) {
				notifyDataSetInvalidated();
			} else {
				notifyDataSetChanged();
			}
			return oldQuery;
		}
	}
	
	public Query<T> getQuery() {
		return mQuery;
	}
}
