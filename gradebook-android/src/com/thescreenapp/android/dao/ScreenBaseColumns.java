package com.thescreenapp.android.dao;

import android.provider.BaseColumns;

public interface ScreenBaseColumns extends BaseColumns {
    public static final String COLUMN_NAME_UUID = "uuid";
    public static final String COLUMN_NAME_CREATED = "created";
    public static final String COLUMN_NAME_UPDATED = "updated";
    public static final String COLUMN_NAME_DELETED = "deleted";
    
    public static final String CREATE_SCREEN_BASE_COLUMNS_SQL =
    		_ID + " INTEGER PRIMARY KEY, "
    		+ COLUMN_NAME_UUID + " CHAR(36) NOT NULL, "
    		+ COLUMN_NAME_CREATED + " INTEGER NOT NULL, "
			+ COLUMN_NAME_UPDATED + " INTEGER NOT NULL, "
			+ COLUMN_NAME_DELETED + " INTEGER NOT NULL DEFAULT 0"; // added in v2
}
