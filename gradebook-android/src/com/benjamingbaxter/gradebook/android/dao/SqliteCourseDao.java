package com.benjamingbaxter.gradebook.android.dao;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.model.Course;

public class SqliteCourseDao extends AbstractSqliteRepository<Course> implements CourseDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.Course._ID,
    	GradebookContract.Course.COLUMN_NAME_UUID,
    	GradebookContract.Course.COLUMN_NAME_CREATED,
    	GradebookContract.Course.COLUMN_NAME_UPDATED,
    	GradebookContract.Course.COLUMN_NAME_DELETED,
    	GradebookContract.Course.COLUMN_NAME_TITLE,
    	GradebookContract.Course.COLUMN_NAME_SECTION,
    	GradebookContract.Course.COLUMN_NAME_SEMESTER,
    	GradebookContract.Course.COLUMN_NAME_YEAR
    };
    
	public SqliteCourseDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
	}

	@Override
	protected String getTableName() {
		return GradebookContract.Course.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected Course readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		Course course = new Course(id, uuid, created);
		
		course.setUpdateDate(new Date(cursor.getLong(index++)));
		course.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		course.setTitle(cursor.getString(index++));
		course.setSection(cursor.getString(index++));
		course.setSemester(cursor.getString(index++));
		course.setYear(cursor.getString(index++));
		
		return course;
	}

	@Override
	protected ContentValues getContentValues(Course object) {
		ContentValues values = new ContentValues();

		values.put(GradebookContract.Course.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.Course.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.Course.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.Course.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(GradebookContract.Course.COLUMN_NAME_TITLE, object.getTitle());
		values.put(GradebookContract.Course.COLUMN_NAME_SECTION, object.getSection());
		values.put(GradebookContract.Course.COLUMN_NAME_SEMESTER, object.getSemester());
		values.put(GradebookContract.Course.COLUMN_NAME_YEAR, object.getYear());
		
		return values;
	}
}
