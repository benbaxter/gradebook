package com.benjamingbaxter.gradebook.android.dao;


/**
 * A contract for the gradebook tables.
 * 
 * FIXME: This is so primitive.  All this should be determined based on the
 *        model classes and possibly annotations (ORM).
 */
public class GradebookContract {
	private GradebookContract() {}
	
	public static abstract class Student implements GradebookBaseColumns {
        public static final String TABLE_NAME = "student";
        
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
		public static final String COLUMN_NAME_EMAIL = "email";
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_FIRST_NAME + " TEXT, "
        		+ COLUMN_NAME_LAST_NAME + " TEXT, "
        		+ COLUMN_NAME_EMAIL + " TEXT"
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
//        public static final String[] STATEMENTS_UPGRADE_TO_V2 = new String[] {
//        		"ALTER TABLE " + TABLE_NAME + " ADD "
//        		+ COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT 0",
//        		"ALTER TABLE " + TABLE_NAME + " ADD "
//        		+ COLUMN_NAME_EMAIL + " TEXT"
//        };
	}
	
	public static abstract class Course implements GradebookBaseColumns {
        public static final String TABLE_NAME = "course";
        
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_TITLE = "title";
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL, "
        		+ COLUMN_NAME_TITLE + " TEXT "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
//        public static final String[] STATEMENTS_UPGRADE_TO_V2 = new String[] {
//        		"ALTER TABLE " + TABLE_NAME + " ADD "
//        		+ COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT 0",
//        		"ALTER TABLE " + TABLE_NAME + " ADD "
//        		+ COLUMN_NAME_DURATION_IN_MINUTES + " INTEGER NOT NULL DEFAULT 60"
//        };
	}
}
