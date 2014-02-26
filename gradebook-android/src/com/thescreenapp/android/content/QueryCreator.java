package com.thescreenapp.android.content;

import com.thescreenapp.dao.Query;

/**
 * A class that can create a query.  Typically intended to be used via an
 * anonymous subclass, e.g.:
 * 
 * <pre>
 * public class MyFragment extends Fragment implements LoaderManager<Query<MyDomainObject>> {
 *     private MyDao mDao;
 *     
 *     @Override
 *     public Loader<Query<MyDomainObject>> onCreateLoader(int id, Bundle args) {
 *         return new QueryLoader<MyDomainObject>(this, new QueryCreator<MyDomainObject>() {
 *             public Query<MyDomainObject> createQuery() {
 *                 return mDao.performSomeQuery();
 *             }
 *         });
 *     }
 * }
 * </pre>
 * 
 * @author eric
 *
 * @param <T>
 */
public interface QueryCreator<T> {
	Query<T> createQuery();
}
