package com.benjamingbaxter.gradebook.android.dao;


import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.AssignmentTypeDao;
import com.benjamingbaxter.gradebook.dao.Query;
import com.benjamingbaxter.gradebook.model.AssignmentType;

public class SqliteAssignmentTypeDao extends AbstractSqliteRepository<AssignmentType> implements AssignmentTypeDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	GradebookContract.AssignmentType._ID,
    	GradebookContract.AssignmentType.COLUMN_NAME_UUID,
    	GradebookContract.AssignmentType.COLUMN_NAME_CREATED,
    	GradebookContract.AssignmentType.COLUMN_NAME_UPDATED,
    	GradebookContract.AssignmentType.COLUMN_NAME_DELETED,
    	GradebookContract.AssignmentType.COLUMN_NAME_LABEL,
    };
    
	public SqliteAssignmentTypeDao(GradebookDbHelper dbHelper) {
		super(dbHelper);
	}

	@Override
	protected String getTableName() {
		return GradebookContract.AssignmentType.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected AssignmentType readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		AssignmentType assignmentType = new AssignmentType(id, uuid, created);
		
		assignmentType.setUpdateDate(new Date(cursor.getLong(index++)));
		assignmentType.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		assignmentType.setLabel(cursor.getString(index++));
		
		return assignmentType;
	}

	@Override
	protected ContentValues getContentValues(AssignmentType object) {
		ContentValues values = new ContentValues();

		values.put(GradebookContract.AssignmentType.COLUMN_NAME_UUID, object.getUuid());
		values.put(GradebookContract.AssignmentType.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(GradebookContract.AssignmentType.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(GradebookContract.AssignmentType.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(GradebookContract.AssignmentType.COLUMN_NAME_LABEL, object.getLabel());
		
		return values;
	}
	
	@Override
	public Query<AssignmentType> findByDisplayCriteria(String searchText) {
		return find(GradebookContract.AssignmentType.COLUMN_NAME_LABEL + " like '%?%' ",
				new String[]{ searchText, searchText });
	}
}
