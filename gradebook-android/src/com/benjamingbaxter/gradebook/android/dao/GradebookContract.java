package com.benjamingbaxter.gradebook.android.dao;

import java.util.Date;


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
	}
	
	public static abstract class Assignment implements GradebookBaseColumns {
        public static final String TABLE_NAME = "assignment";
        
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSSIBLE_POINTS = "possible_points";
        public static final String COLUMN_NAME_COURSE_ID = "course_id"; //v3
        public static final String COLUMN_NAME_STUDENT_ID = "student_id";//v3
        public static final String COLUMN_NAME_ASSIGNMENT_TYPE_ID = "assignment_type_id";//v4
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_TITLE + " TEXT, "
        		+ COLUMN_NAME_POSSIBLE_POINTS + " REAL, "
        		+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0, "	
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_ASSIGNMENT_TYPE_ID + " INTEGER NOT NULL DEFAULT 0 "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class AssignmentType implements GradebookBaseColumns {
        public static final String TABLE_NAME = "assignment_type";
        
        public static final String COLUMN_NAME_LABEL = "label";
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_LABEL + " TEXT "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
        
        public static final String[] INITAL_LOAD_OF_DATA = new String[] {
        	"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
        			+ ") values ("
        			+ "0, 0, " + new Date().getTime() + ", " + new Date().getTime() + ", "
            		+ "'Final Exam'"
            		+ ") ",	
        	"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
        			+ ") values ("
        			+ "1, 1, " + new Date().getTime() + ", " + new Date().getTime() + ", "
            		+ "'Lab'"
            		+ ") ",
        	"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
        			+ ") values ("
        			+ "2, 2, " + new Date().getTime() + ", " + new Date().getTime() + ", "
            		+ "'Midterm'"
            		+ ") ",
			"insert into " + TABLE_NAME + " ("
	    			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
	    			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
	    			+ ") values ("
	    			+ "3, 3, " + new Date().getTime() + ", " + new Date().getTime() + ", "
	        		+ "'Paper'"
	        		+ ") ",
    		"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
        			+ ") values ("
        			+ "4, 4, " + new Date().getTime() + ", " + new Date().getTime() + ", "
            		+ "'Project'"
            		+ ") ",	  
    		"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL
        			+ ") values ("
        			+ "5, 5, " + new Date().getTime() + ", " + new Date().getTime() + ", "
            		+ "'Quiz'"
            		+ ") "	              		
        };
	}
	

	public static abstract class AssignmentWeight implements GradebookBaseColumns {
        public static final String TABLE_NAME = "assignment_weight";
        
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_ASSIGNMENT_TYPE_ID = "assignment_type_id";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_ASSIGNMENT_TYPE_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_WEIGHT + " REAL "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class GradedAssignment implements GradebookBaseColumns {
        public static final String TABLE_NAME = "student_assignments";
        
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
        public static final String COLUMN_NAME_STUDENT_ID = "student_id";
        public static final String COLUMN_NAME_ASSIGNMENT_ID = "assignment_id";
        public static final String COLUMN_NAME_EARNED_POINTS = "earned_points";
        public static final String COLUMN_NAME_FEEDBACK = "feedback";
        
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_COURSE_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_STUDENT_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_ASSIGNMENT_ID + " INTEGER NOT NULL DEFAULT 0, "
        		+ COLUMN_NAME_EARNED_POINTS + " REAL, "
        		+ COLUMN_NAME_FEEDBACK + " TEXT "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public static abstract class Semester implements GradebookBaseColumns {
        public static final String TABLE_NAME = "semester";
        
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_ORDER = "ordr";
        
        public static final String STATEMENT_CREATE_TABLE =
        		"CREATE TABLE " + TABLE_NAME + " ("
        		+ CREATE_GRADEBOOK_BASE_COLUMNS_SQL + ", "
        		+ COLUMN_NAME_LABEL + " TEXT, "
        		+ COLUMN_NAME_ORDER + " REAL "
        		+ ")";
        
        public static final String STATEMENT_DELETE_TABLE =
        		"DROP TABLE IF EXISTS " + TABLE_NAME;
 
        public static final String[] INITAL_LOAD_OF_DATA = new String[] {
        	"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL + ", " + COLUMN_NAME_ORDER
        			+ ") values ("
        			+ "0, 0, " + new Date().getTime() + ", " + new Date().getTime() + ", "
        			+ "'Fall', 0"
        			+ ") ",
			"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL + ", " + COLUMN_NAME_ORDER
        			+ ") values ("
        			+ "1, 1, " + new Date().getTime() + ", " + new Date().getTime() + ", "
        			+ "'Winter', 1"
        			+ ") ",
			"insert into " + TABLE_NAME + " ("
        			+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
        			+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL + ", " + COLUMN_NAME_ORDER
        			+ ") values ("
        			+ "2, 2, " + new Date().getTime() + ", " + new Date().getTime() + ", "
        			+ "'Spring', 2"
        			+ ") ",
			"insert into " + TABLE_NAME + " ("
					+ _ID + ", " + COLUMN_NAME_UUID + ", " + COLUMN_NAME_CREATED + ", "
					+ COLUMN_NAME_UPDATED + ", " + COLUMN_NAME_LABEL + ", " + COLUMN_NAME_ORDER
					+ ") values ("
					+ "3, 3, " + new Date().getTime() + ", " + new Date().getTime() + ", "
					+ "'Summer', 3"
					+ ") "
        };
	}
}