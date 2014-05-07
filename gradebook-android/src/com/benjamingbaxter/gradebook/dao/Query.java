package com.benjamingbaxter.gradebook.dao;

import java.io.Closeable;
import java.util.List;

/**
 * A query over a number of objects.  Typical usage is as follows:
 * 
 * <pre>
 *     Query&lt;MyClass&gt; query = ...;
 *     try {
 *         while (query.next()) {
 *             MyClass myObject = query.current();
 *             // Do something with myObject here
 *         }
 *     } finally {
 *         query.close();
 *     }
 * </pre>
 * 
 * Alternatively, you may wish to call {@link #all()} to retrieve all results.
 * 
 * <p>FIXME: When compiling against Java 7, this should extend AutoCloseable.
 */
public interface Query<T> extends Closeable {
	/**
	 * Advances the query to the next object, if there is one.
	 * 
	 * @return <code>true</code> if the query is now pointing to a valid row
	 *         (so that {@link #current()} can be called), or <code>false</code>
	 *         if we are past the last result
	 */
	public boolean next();
	
	/**
	 * Returns the current object in the query.  {@link #next()} must have been
	 * called at least once prior to calling this method.
	 * 
	 * @return  The current object in this query
	 * 
	 * @throws IllegalStateException
	 *     If the query is pointing to before the first object or after the
	 *     last object
	 */
	public T current() throws IllegalStateException;
	
	/**
	 * Returns the number of objects in this query.
	 * 
	 * @return  The number of objects in this query
	 */
	public int count();
	
	/**
	 * Retrieves the object at a given position in this query.  If possible,
	 * multiple calls to {@link #get(int)} with the same index should return
	 * the same object.
	 * 
	 * @param index  The index of the object to retrieve (starting at 0)
	 * 
	 * @return  The object at the given index
	 * 
	 * @throws IndexOutOfBoundsException
	 *     If the index is less than 0 or greater than or equal to {@link #count()}
	 */
	public T get(int index) throws IndexOutOfBoundsException;
	
	/**
	 * Retrieves all the object in the query.  Even if {@link #next()} has been
	 * called a number of times, this method should still return a list of all
	 * entries in the query, not just the ones starting at the current point in
	 * the query.
	 * 
	 * @return
	 */
	public List<T> all();
	
	/**
	 * Move the cursor to the first row.
	 * 
	 * This method will return false if the cursor is empty.
	 * 
	 * Convenience method compared to having to re-query
	 * 
	 * @return: whether the move succeeded. 
	 */
	public boolean moveToFirst();
	
	/**
	 * Closes this query and releases any resources associated with it, after
	 * which point it will be unusable.  It should be possible to call this
	 * method multiple times without ill effect.
	 */
	public void close();
}
