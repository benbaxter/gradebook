package com.benjamingbaxter.gradebook.dao;

import com.benjamingbaxter.gradebook.model.ScreenModelObject;

/**
 * A repository that allows all the basic CRUD operations on the objects in it.
 *
 * @param <T>  The class of objects in this repository.
 */
public interface MutableRepository<T extends ScreenModelObject> extends Repository<T> {
	/**
	 * Adds the given object to this repository.  The existing ID in the object
	 * will be ignored.  The ID will be populated with the ID of the object
	 * after it is saved.  The last update date will be set to the current date.
	 * 
	 * @param object  The object to create
	 */
	void create(T object);
	
	/**
	 * Updates the given object in the repository.  The last update date will be
	 * set to the current date.
	 * 
	 * @param object  The object to save
	 */
	void update(T object);
	
	/**
	 * If not created, it will create the dude (unless it is a dudette), 
	 * otherwise it will update the given object in the repository.  
	 * The last update date will be set to the current date.
	 * 
	 * @param object  The object to save
	 */
	void createOrUpdate(T object);
	
	/**
	 * Marks the given object from the repository as deleted (or at least the
	 * object with the same ID).
	 * 
	 * @param object  The object to mark deleted
	 */
	void delete(T object);
	
	/**
	 * Un-sets the deleted flag from the object with the given ID, if one
	 * exists.
	 * 
	 * @param id  The ID of the object to restore
	 */
	void restore(long id);
	
	/**
	 * <strong>Do not call this</strong> unless you are writing code that
	 * synchronizes the local repository with a remote data source.  Normally
	 * you should call {@link #markDeleted(ScreenModelObject)} instead.
	 * 
	 * <p>This method completely purges the given object from the repository (or
	 * at least the object with the same ID).
	 * 
	 * @param object  The object to purge
	 */
	void purge(T object);
}
