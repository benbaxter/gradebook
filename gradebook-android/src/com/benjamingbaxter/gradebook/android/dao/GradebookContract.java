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
        
        public static final String COLUMN_NAME_STUDENT_ID = "student_id"; //v3
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
		public static final String COLUMN_NAME_EMAIL = "email";
		public static final String COLUMN_NAME_COURSE_ID = "course_id"; //v3
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_FIRST_NAME + " TEXT, "
        		+ COLUMN_NAME_LAST_NAME + " TEXT, "
        		+ COLUMN_NAME_EMAIL + " TEXT, "
            	+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0, "	
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0 "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] STATEMENTS_UPGRADE_TO_V3 = new String[] {
        	"ALTER TABLE " + TABLE_NAME + " ADD "
            		+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0 ",	
            "ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0 "
        };
	}
	
	public static abstract class Course implements GradebookBaseColumns {
        public static final String TABLE_NAME = "course";
        
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SECTION = "section"; //v2
        public static final String COLUMN_NAME_SEMESTER = "semester"; //v2
        public static final String COLUMN_NAME_YEAR = "year"; //v2
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_TITLE + " TEXT, "
        		+ COLUMN_NAME_SECTION + " TEXT, "
        		+ COLUMN_NAME_SEMESTER + " TEXT, "
        		+ COLUMN_NAME_YEAR + " TEXT "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] STATEMENTS_UPGRADE_TO_V2 = new String[] {
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_SECTION + " TEXT",
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_SEMESTER + " TEXT",
        		"ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_YEAR + " TEXT"
        };
	}
	
	public static abstract class Assignment implements GradebookBaseColumns {
        public static final String TABLE_NAME = "assignment";
        
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSSIBLE_POINTS = "possible_points";
        public static final String COLUMN_NAME_EARNED_POINTS = "earned_points";
        public static final String COLUMN_NAME_FEEDBACK = "feedback";
        public static final String COLUMN_NAME_COURSE_ID = "course_id"; //v3
        public static final String COLUMN_NAME_STUDENT_ID = "student_id";//v3
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_TITLE + " TEXT, "
        		+ COLUMN_NAME_POSSIBLE_POINTS + " REAL, "
        		+ COLUMN_NAME_EARNED_POINTS + " REAL, "
        		+ COLUMN_NAME_FEEDBACK + " REAL, "
        		+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0, "	
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0"
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] STATEMENTS_UPGRADE_TO_V3 = new String[] {
        	"ALTER TABLE " + TABLE_NAME + " ADD "
            		+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0",	
            "ALTER TABLE " + TABLE_NAME + " ADD "
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0"
        };
	}
}