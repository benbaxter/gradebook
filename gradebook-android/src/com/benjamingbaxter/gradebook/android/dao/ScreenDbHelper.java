package com.benjamingbaxter.gradebook.android.dao;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

public class ScreenDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Gradebook.db";
	public static final int DATABASE_VERSION = 1;
	protected static SparseArray<List<String>> versionsSql;
	static {
		versionsSql = new SparseArray<List<String>>();
		versionsSql.append(1, Collections.<String> emptyList());
		versionsSql.append(2, Collections.<String> emptyList()); 
	}
	
	public ScreenDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GradebookContract.Course.STATEMENT_CREATE_TABLE);
		db.execSQL(GradebookContract.Student.STATEMENT_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int version = oldVersion + 1; version <= newVersion; version++) {
			List<String> statements = versionsSql.get(version);
			if( statements != null ) {
				for (String statement : statements) {
					db.execSQL(statement);
				}
			} else {
				throw new IllegalArgumentException("Cannot upgrade to unknown database version " + version);
			}
		}
	}
}
