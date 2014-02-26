package com.benjamingbaxter.gradebook.android.dao;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.benjamingbaxter.gradebook.dao.CandidateDao;
import com.benjamingbaxter.gradebook.model.Student;

public class SqliteCandidateDao extends AbstractSqliteRepository<Student> implements CandidateDao {
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	ScreenContract.Candidate._ID,
    	ScreenContract.Candidate.COLUMN_NAME_UUID,
    	ScreenContract.Candidate.COLUMN_NAME_CREATED,
    	ScreenContract.Candidate.COLUMN_NAME_UPDATED,
    	ScreenContract.Candidate.COLUMN_NAME_DELETED,
    	ScreenContract.Candidate.COLUMN_NAME_FIRST_NAME,
    	ScreenContract.Candidate.COLUMN_NAME_LAST_NAME,
    	ScreenContract.Candidate.COLUMN_NAME_PHONE_NUMBER,
    	ScreenContract.Candidate.COLUMN_NAME_RATING,
    	ScreenContract.Candidate.COLUMN_NAME_EMAIL
    };
    
	public SqliteCandidateDao(ScreenDbHelper dbHelper) {
		super(dbHelper);
	}

	@Override
	protected String getTableName() {
		return ScreenContract.Candidate.TABLE_NAME;
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
		
		Student candidate = new Student(id, uuid, created);
		
		candidate.setUpdateDate(new Date(cursor.getLong(index++)));
		candidate.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		candidate.setFirstName(cursor.getString(index++));
		candidate.setLastName(cursor.getString(index++));
//		candidate.setPhoneNumber(cursor.getString(index++));
//		candidate.setRating(cursor.getInt(index++));
		candidate.setEmail(cursor.getString(index++));
		
		return candidate;
	}

	@Override
	protected ContentValues getContentValues(Student object) {
		ContentValues values = new ContentValues();
		values.put(ScreenContract.Candidate.COLUMN_NAME_UUID, object.getUuid());
		values.put(ScreenContract.Candidate.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(ScreenContract.Candidate.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(ScreenContract.Candidate.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(ScreenContract.Candidate.COLUMN_NAME_FIRST_NAME, object.getFirstName());
		values.put(ScreenContract.Candidate.COLUMN_NAME_LAST_NAME, object.getLastName());
//		values.put(ScreenContract.Candidate.COLUMN_NAME_PHONE_NUMBER, object.getPhoneNumber());
//		values.put(ScreenContract.Candidate.COLUMN_NAME_RATING, object.getRating());
		values.put(ScreenContract.Candidate.COLUMN_NAME_EMAIL, object.getEmail());
		
		return values;
	}
}
