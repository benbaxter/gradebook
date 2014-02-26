package com.thescreenapp.android.dao;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.thescreenapp.dao.CandidateDao;
import com.thescreenapp.model.Interview;

public class SqliteInterviewDao extends AbstractSqliteRepository<Interview> {
	private CandidateDao mCandidateDao;
	
    private static final String[] ALL_COLUMN_NAMES = new String[] {
    	ScreenContract.Interview._ID,
    	ScreenContract.Interview.COLUMN_NAME_UUID,
    	ScreenContract.Interview.COLUMN_NAME_CREATED,
    	ScreenContract.Interview.COLUMN_NAME_UPDATED,
    	ScreenContract.Interview.COLUMN_NAME_DELETED,
    	ScreenContract.Interview.COLUMN_NAME_CANDIDATE_ID,
    	ScreenContract.Interview.COLUMN_NAME_LOCATION,
    	ScreenContract.Interview.COLUMN_NAME_INTERVIEW_DATE,
    	ScreenContract.Interview.COLUMN_NAME_DURATION_IN_MINUTES
    };
    
	public SqliteInterviewDao(ScreenDbHelper dbHelper, CandidateDao candidateDao) {
		super(dbHelper);
		mCandidateDao = candidateDao;
	}

	@Override
	protected String getTableName() {
		return ScreenContract.Interview.TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return ALL_COLUMN_NAMES;
	}

	@Override
	protected Interview readObject(Cursor cursor) {
		int index = 0;
		
		long id = cursor.getLong(index++);
		String uuid = cursor.getString(index++);
		Date created = new Date(cursor.getLong(index++));
		
		Interview interview = new Interview(id, uuid, created);
		
		interview.setUpdateDate(new Date(cursor.getLong(index++)));
		interview.setDeleted(intColumnToBoolean(cursor.getInt(index++)));
		interview.setCandidate(mCandidateDao.findById(cursor.getLong(index++)));
		interview.setLocation(cursor.getString(index++));
		interview.setInterviewDate(new Date(cursor.getLong(index++)));
		interview.setDurationInMinutes(cursor.getInt(index++));
		
		return interview;
	}

	@Override
	protected ContentValues getContentValues(Interview object) {
		ContentValues values = new ContentValues();
		values.put(ScreenContract.Interview.COLUMN_NAME_UUID, object.getUuid());
		values.put(ScreenContract.Interview.COLUMN_NAME_CREATED, object.getCreationDate().getTime());
		values.put(ScreenContract.Interview.COLUMN_NAME_UPDATED, object.getUpdateDate().getTime());
		values.put(ScreenContract.Interview.COLUMN_NAME_DELETED, booleanToIntColumn(object.isDeleted()));
		values.put(ScreenContract.Interview.COLUMN_NAME_CANDIDATE_ID, object.getCandidate() == null ? null : object.getCandidate().getId());
		values.put(ScreenContract.Interview.COLUMN_NAME_LOCATION, object.getLocation());
		values.put(ScreenContract.Interview.COLUMN_NAME_INTERVIEW_DATE, object.getInterviewDate().getTime());
		values.put(ScreenContract.Interview.COLUMN_NAME_DURATION_IN_MINUTES, object.getDurationInMinutes());
		
		return values;
	}
}
