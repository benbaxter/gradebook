package com.thescreenapp.android.dao;

/**
 * A contract for the Screen tables.
 * 
 * FIXME: This is so primitive.  All this should be determined based on the
 *        model classes and possibly annotations (ORM).
 */
public class ScreenContract {
	private ScreenContract() {}
	
	public static abstract class Candidate implements ScreenBaseColumns {
        public static final String TABLE_NAME = "candidate";
        
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_RATING = "rating";
		public static final String COLUMN_NAME_EMAIL = "email"; // added in v2
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_SCREEN_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_FIRST_NAME + " TEXT, "
        		+ COLUMN_NAME_LAST_NAME + " TEXT, "
        		+ COLUMN_NAME_PHONE_NUMBER + " TEXT, "
        		+ COLUMN_NAME_RATING + " INTEGER, "
        		+ COLUMN_NAME_EMAIL + " TEXT"
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] STATEMENTS_UPGRADE_TO_V2 = new String[] {
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT 0",
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_EMAIL + " TEXT"
        };
	}
	
	public static abstract class Interview implements ScreenBaseColumns {
        public static final String TABLE_NAME = "interview";
        
        public static final String COLUMN_NAME_CANDIDATE_ID = "candidate_id";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_INTERVIEW_DATE = "interview_date";
        public static final String COLUMN_NAME_DURATION_IN_MINUTES = "duration_in_minutes"; // added in v2
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_SCREEN_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_CANDIDATE_ID + " INTEGER NOT NULL, "
        		+ COLUMN_NAME_LOCATION + " TEXT, "
        		+ COLUMN_NAME_INTERVIEW_DATE + " INTEGER"
        		+ COLUMN_NAME_DURATION_IN_MINUTES + " INTEGER NOT NULL DEFAULT 60"
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] STATEMENTS_UPGRADE_TO_V2 = new String[] {
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT 0",
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_DURATION_IN_MINUTES + " INTEGER NOT NULL DEFAULT 60"
        };
	}
}
