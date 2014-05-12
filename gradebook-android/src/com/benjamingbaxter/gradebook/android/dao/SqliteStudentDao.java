package com.benjamingbaxter.gradebook.android.dao;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.dao.StudentDao;
import com.benjamingbaxter.gradebook.model.Course;
import com.benjamingbaxter.gradebook.model.Student;

public class SqliteStudentDao extends AbstractSqliteRepository<Student> implements StudentDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.Student._ID,
    	GradebookContract.Student.COLUMN_NAME_UUID,
    	GradebookContract.Student.COLUMN_NAME_CREATED,
    	GradebookContract.Student.COLUMN_NAME_UPDATED,
    	GradebookContract.Student.COLUMN_NAME_DELETED,
    	GradebookContract.Student.COLUMN_NAME_FIRST_NAME,
    	GradebookContract.Student.COLUMN_NAME_LAST_NAME,
    	GradebookContract.Student.COLUMN_NAME_EMAIL,
    	GradebookContract.Student.COLUMN_NAME_COURSE_ID
    };
    
	public SqliteStudentDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
	}

	@Override
	protected String getTableName() {
		return GradebookContract.Student.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected Student readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		Student student = new Student(id, uuid, created);
		
		student.setUpdateDate(new Date(cursor.getLong(index++)));
		student.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		student.setFirstName(cursor.getString(index++));
		student.setLastName(cursor.getString(index++));
		student.setEmail(cursor.getString(index++));
		
		return student;
	}

	@Override
	protected ContentValues getContentValues(Student object) {
		ContentValues values = new ContentValues();
		values.put(GradebookContract.Student.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.Student.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.Student.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.Student.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(GradebookContract.Student.COLUMN_NAME_FIRST_NAME, object.getFirstName());
		values.put(GradebookContract.Student.COLUMN_NAME_LAST_NAME, object.getLastName());
		values.put(GradebookContract.Student.COLUMN_NAME_EMAIL, object.getEmail());
		
		values.put(GradebookContract.Student.COLUMN_NAME_COURSE_ID, object.getCourse().getId());
		
		return values;
	}
	
	public Query<Student> findAllForCourse(Course course) {
		return find(GradebookContract.Student.COLUMN_NAME_COURSE_ID + " = ?", new String[]{ String.valueOf(course.getId()) });
	}
	
	@Override
	public Query<Student> findByDisplayCriteria(String searchText) {
		return find(GradebookContract.Student.COLUMN_NAME_FIRST_NAME + " like ? or "
				+ GradebookContract.Student.COLUMN_NAME_LAST_NAME + " like ? ",
				new String[]{"%"+searchText+"%"});
	}
}
