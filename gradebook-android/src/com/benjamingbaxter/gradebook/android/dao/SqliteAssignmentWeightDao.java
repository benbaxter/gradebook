package com.benjamingbaxter.gradebook.android.dao;


import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.AssignmentWeightDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.AssignmentType;
import com.benjamingbaxter.gradebook.model.AssignmentWeight;

public class SqliteAssignmentWeightDao extends AbstractSqliteRepository<AssignmentWeight> implements AssignmentWeightDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.AssignmentWeight._ID,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_UUID,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_CREATED,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_UPDATED,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_DELETED,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_COURSE_ID,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_ASSIGNMENT_TYPE_ID,
    	GradebookContract.AssignmentWeight.COLUMN_NAME_WEIGHT,
    };
    
    private AssignmentTypeDao assignmentTypeDao;
    
	public SqliteAssignmentWeightDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
		assignmentTypeDao = new SqliteAssignmentTypeDao(dbHelper);
	}

	@Override
	protected String getTableName() {
		return GradebookContract.AssignmentWeight.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected AssignmentWeight readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		AssignmentWeight assignmentWeight = new AssignmentWeight(id, uuid, created);
		
		assignmentWeight.setUpdateDate(new Date(cursor.getLong(index++)));
		assignmentWeight.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		
		assignmentWeight.setCourseId(cursor.getLong(index++));
		
		AssignmentType type = assignmentTypeDao.findById(cursor.getLong(index++));
		assignmentWeight.setAssignmentType(type);
		
		assignmentWeight.setWeight(cursor.getDouble(index++));
		
		return assignmentWeight;
	}

	@Override
	protected ContentValues getContentValues(AssignmentWeight object) {
		ContentValues values = new ContentValues();

		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_COURSE_ID, object.getCourseId());
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_ASSIGNMENT_TYPE_ID, object.getAssignmentType().getId());
		values.put(GradebookContract.AssignmentWeight.COLUMN_NAME_WEIGHT, object.getWeight());
		
		return values;
	}
	
	@Override
	public Query<AssignmentWeight> findByCourseId(Long id) {
		return find(GradebookContract.AssignmentWeight.COLUMN_NAME_COURSE_ID + " = ?", 
				new String[]{ String.valueOf(id) });
	}

	@Override
	public void deleteByCourseId(Long id) {
		mDbHelper.getWritableDatabase().delete(getTableName(), GradebookContract.AssignmentWeight.COLUMN_NAME_COURSE_ID + " = ?", new String[] { String.valueOf(id) });
	}
	
}
