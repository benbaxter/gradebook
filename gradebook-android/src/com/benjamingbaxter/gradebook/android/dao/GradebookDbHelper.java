package com.benjamingbaxter.gradebook.android.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

public class GradebookDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Gradebook.db";
	public static final int DATABASE_VERSION = 3;
	protected static SparseArray<List<String>> versionsSql;
	static {
		versionsSql = new SparseArray<List<String>>();
		versionsSql.append(1, Collections.<String> emptyList());
		List<String> v2Sql = new ArrayList<String>();
		v2Sql.addAll(Arrays.asList(GradebookContract.Course.STATEMENTS_UPGRADE_TO_V2));
		versionsSql.append(2, v2Sql);
		
		List<String> v3Sql = new ArrayList<String>();
		v3Sql.addAll(Arrays.asList(GradebookContract.Student.STATEMENTS_UPGRADE_TO_V3));
		v3Sql.addAll(Arrays.asList(GradebookContract.Assignment.STATEMENTS_UPGRADE_TO_V3));
		versionsSql.append(3, v3Sql);
	}
	
	public GradebookDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//if having troubles, reset back to one, run it, then reinstall
		//getWritableDatabase().setVersion(1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GradebookContract.Course.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Student.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Assignment.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.AssignmentType.STATEMENT_CREATE_TABLE);
		
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
