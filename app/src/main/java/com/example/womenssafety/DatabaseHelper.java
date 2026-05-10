package com.example.womenssafety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "womens_safety.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_NAME = "user_info";
    private static final String COL_ID = "id";
    private static final String COL_MOBILE1 = "mobile1";
    private static final String COL_MOBILE2 = "mobile2";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MOBILE1 + " TEXT, " +
                COL_MOBILE2 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert user info (mobile numbers)
    public void insertUser(String mobile1, String mobile2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MOBILE1, mobile1);
        contentValues.put(COL_MOBILE2, mobile2);
        db.insert(TABLE_NAME, null, contentValues);
    }

    // Retrieve user info with improved error handling
    public String[] getUserInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_MOBILE1, COL_MOBILE2},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Retrieve mobile numbers
            int mobile1Index = cursor.getColumnIndex(COL_MOBILE1);
            int mobile2Index = cursor.getColumnIndex(COL_MOBILE2);

            // Check if the column indexes are valid
            if (mobile1Index != -1 && mobile2Index != -1) {
                String mobile1 = cursor.getString(mobile1Index);
                String mobile2 = cursor.getString(mobile2Index);
                cursor.close();
                return new String[]{mobile1, mobile2};
            } else {
                cursor.close();
                return null;  // Columns not found
            }
        }
        assert cursor != null;
        cursor.close();
        return null;  // No data found
    }
}
