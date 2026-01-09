package com.example.savoryrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReservationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "savory_reservations.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE = "reservations";
    public static final String COL_ID = "id";
    public static final String COL_OWNER = "owner_username";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_PARTY_SIZE = "party_size";
    public static final String COL_DATE_TIME = "date_time";

    public ReservationDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_OWNER + " TEXT NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT, " +
                COL_PARTY_SIZE + " INTEGER NOT NULL, " +
                COL_DATE_TIME + " TEXT NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // ✅ include ownerUsername
    public long addReservation(String ownerUsername, String name, String phone, int partySize, String dateTime) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_OWNER, ownerUsername);
        cv.put(COL_NAME, name);
        cv.put(COL_PHONE, phone);
        cv.put(COL_PARTY_SIZE, partySize);
        cv.put(COL_DATE_TIME, dateTime);
        return db.insert(TABLE, null, cv);
    }

    public Cursor getAllReservations() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT " + COL_ID + " AS _id, * FROM " + TABLE +
                        " ORDER BY " + COL_DATE_TIME + " ASC",
                null
        );
    }

    public int deleteReservationById(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getReservationById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT " + COL_ID + " AS _id, " +
                COL_OWNER + ", " +
                COL_NAME + ", " +
                COL_DATE_TIME + ", " +
                COL_PARTY_SIZE +
                " FROM " + TABLE +
                " WHERE " + COL_ID + "=?";
        return db.rawQuery(sql, new String[]{String.valueOf(id)});
    }

    public int updateReservation(long id, String name, String dateTime, int partySize) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DATE_TIME, dateTime);
        cv.put(COL_PARTY_SIZE, partySize);
        return db.update(TABLE, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
}
