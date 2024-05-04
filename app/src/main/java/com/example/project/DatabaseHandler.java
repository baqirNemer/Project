package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MediRecord.db";
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_USER_ID = "userID";
    private static final String COLUMN_PROFILE_PICTURE_PATH = "profilePicturePath";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY,"
                + COLUMN_PROFILE_PICTURE_PATH + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addOrUpdateUserProfilePicture(String userID, String profilePicturePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userID);
        values.put(COLUMN_PROFILE_PICTURE_PATH, profilePicturePath);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addUser(String userID) {
        addOrUpdateUserProfilePicture(userID, "");
    }

    public String getUserProfilePicturePath(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String profilePicturePath = "";

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_PROFILE_PICTURE_PATH},
                COLUMN_USER_ID + "=?", new String[]{userID}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_PROFILE_PICTURE_PATH);
                if (columnIndex != -1) {
                    profilePicturePath = cursor.getString(columnIndex);
                } else {
                    Log.w("Profile Picture Path", "Column index not found for COLUMN_PROFILE_PICTURE_PATH");
                }
            }
            cursor.close();
        }
        db.close();
        return profilePicturePath;
    }

    public boolean isUserIDExists(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists = false;

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_USER_ID},
                COLUMN_USER_ID + "=?", new String[]{userID}, null, null, null, null);
        if (cursor != null) {
            exists = cursor.moveToFirst();
            cursor.close();
        }
        db.close();
        return exists;
    }

    public void deleteUserProfilePicture(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_USER_ID + "=?", new String[]{userID});
        db.close();
    }
}
