package com.benjamingbaxter.gradebook.android.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

public class ScreenDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Gradebook.db";
	public static final int DATABASE_VERSION = 2;
	protected static SparseArray<List<String>> versionsSql;
	static {
		versionsSql = new SparseArray<List<String>>();
		versionsSql.append(1, Collections.<String> emptyList());
		List<String> v2Sql = new ArrayList<String>();
		v2Sql.addAll(Arrays.asList(ScreenContract.Candidate.STATEMENTS_UPGRADE_TO_V2));
		v2Sql.addAll(Arrays.asList(ScreenContract.Interview.STATEMENTS_UPGRADE_TO_V2));
		versionsSql.append(2, v2Sql); 
	}
	
	public ScreenDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ScreenContract.Candidate.STATEMENT_CREATE_TABLE);
		db.execSQL(ScreenContract.Interview.STATEMENT_CREATE_TABLE);
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
