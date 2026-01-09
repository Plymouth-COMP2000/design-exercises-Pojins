package com.example.savoryrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MenuDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "savory_menu.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MENU = "menu_items";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image";

    public MenuDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_MENU + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        COL_PRICE + " REAL NOT NULL, " +
                        COL_IMAGE + " TEXT" +
                        ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    // ➕ ADD menu item
    public long addMenuItem(String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);

        long id = db.insert(TABLE_MENU, null, values);
        db.close();
        return id;
    }

    // 📄 READ all menu items (CursorAdapter requires "_id")
    public Cursor getAllMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " +
                COL_ID + " AS _id, " +
                COL_NAME + ", " +
                COL_PRICE + ", " +
                COL_IMAGE +
                " FROM " + TABLE_MENU +
                " ORDER BY " + COL_NAME + " ASC";

        return db.rawQuery(sql, null);
    }

    // 🔎 READ one menu item (for editing)
    public Cursor getMenuItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " +
                COL_ID + " AS _id, " +
                COL_NAME + ", " +
                COL_PRICE + ", " +
                COL_IMAGE +
                " FROM " + TABLE_MENU +
                " WHERE " + COL_ID + "=?";

        return db.rawQuery(sql, new String[]{String.valueOf(id)});
    }

    // ✏️ UPDATE menu item
    public int updateMenuItem(long id, String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);

        int rows = db.update(TABLE_MENU, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // ❌ DELETE menu item
    public int deleteMenuItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_MENU, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
}
