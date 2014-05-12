package com.benjamingbaxter.gradebook.android.dao;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

public class GradebookDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Gradebook.db";
	public static final int DATABASE_VERSION = 1;
	protected static SparseArray<List<String>> versionsSql;
	static {
		versionsSql = new SparseArray<List<String>>();
		versionsSql.append(1, Collections.<String> emptyList());
	}
	
	public GradebookDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		int version = getWritableDatabase().getVersion();
		Log.d("GradebookDbHelper", "database version: " + version);
		
		//if having troubles, reset back to one, run it, then reinstall
		//getWritableDatabase().setVersion(1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GradebookContract.Course.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Student.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Assignment.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.AssignmentType.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.AssignmentWeight.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.GradedAssignment.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Semester.STATEMENT_CREATE_TABLE);
		
		for( String sql : GradebookContract.Semester.INITAL_LOAD_OF_DATA) {
			db.execSQL(sql);
		}

		for( String sql : GradebookContract.AssignmentType.INITAL_LOAD_OF_DATA) {
			db.execSQL(sql);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int version = oldVersion + 1; version <= newVersion; version++) {
			List<String> statements = versionsSql.get(version);
			if( statements != null ) {
				for (String statement : statements) {
					Log.d(DATABASE_NAME, statement);
					db.execSQL(statement);
				}
			} else {
				throw new IllegalArgumentException("Cannot upgrade to unknown database version " + version);
			}
		}
	}
}
