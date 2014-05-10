package com.benjamingbaxter.gradebook.android.dao;

import java.util.Date;
import java.util.HashSet;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.AssignmentWeightDao;
import com.benjamingbaxter.gradebook.dao.CourseDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.AssignmentWeight;
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
    
    private AssignmentWeightDao assignmentWeightDao;
    
	public SqliteCourseDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
		assignmentWeightDao = new SqliteAssignmentWeightDao(dbHelper);
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
		
		Query<AssignmentWeight> query = assignmentWeightDao.findByCourseId(id);
		course.setAssignmentWeights(new HashSet<AssignmentWeight>(query.all()));
		
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
	
	@Override
	protected long createFilledInObject(Course object) {
		long id = super.createFilledInObject(object);
		
		createWeights(object);
		
		return id;
	}
	
	@Override
	protected void updateFilledInObject(Course object) {
		super.updateFilledInObject(object);
		
		assignmentWeightDao.deleteByCourseId(object.getId());
		
		createWeights(object);
		
	}

	private void createWeights(Course object) {
		for(AssignmentWeight weight : object.getAssignmentWeights()) {
			weight.setCourseId(object.getId());
			assignmentWeightDao.create(weight);
		}
	}
}
