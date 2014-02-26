package com.thescreenapp.dao;

import com.thescreenapp.model.ScreenModelObject;

/**
 * A repository of {@link ScreenModelObject}s that can be queried.  This is a
 * read-only repository; for methods that allow writing to the repository, see
 * {@link MutableRepository}.
 * 
 * @param <T>  The class of object in this repository
 */
public interface Repository<T extends ScreenModelObject> {
	
	/**
	 * Finds the single object with the given ID in this repository.
	 * 
	 * @param id  The ID of the object to find
	 * 
	 * @return  The object with the given ID, or <code>null</code> if there were
	 *          no such objects found
	 */
	T findById(long id);
	
	/**
	 * Finds the single object with the given UUID in this repository.
	 * 
	 * @param uuid  The UUID of the object to find, in the standard string
	 *              form
	 *
	 * @return  The object with the given UUID, or <code>null</code> if there
	 *          were no such objects found
	 */
	T findByUuid(String uuid);
	
	/**
	 * Finds all non-deleted objects in the repository.
	 * 
	 * @return  A {@link Query} over all the non-deleted objects in the
	 *          repository
	 */
	Query<T> findAll();
	
	/**
	 * Finds all objects in the repository, including those that have been
	 * deleted.
	 * 
	 * @return  A {@link Query} over all the objects in the repository
	 */
	Query<T> findAllIncludingDeleted();
}
