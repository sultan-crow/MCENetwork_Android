package library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mcen";
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_JSON = "json_table";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_YEAR = "year";
    private static final String KEY_ROLE = "role";
    private static final String KEY_JSON = "json";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_ROLE + " INTEGER,"
                + KEY_YEAR + " TEXT " + ")";

        String CREATE_JSON_TABLE = "CREATE TABLE " + TABLE_JSON + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_JSON + " TEXT " + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_JSON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JSON);
        onCreate(db);

    }

    public void addUser(String name, String email, String username, String year, int role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_USERNAME, username);
        values.put(KEY_YEAR, year);
        values.put(KEY_ROLE, role);

        db.insert(TABLE_LOGIN, null, values);

        values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_JSON, "");

        db.insert(TABLE_JSON, null, values);

        db.close();
    }

    public void putJSON(String json) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_JSON, json);

        db.update(TABLE_JSON, values, KEY_ID + "=" + 1, null);
        db.close();
    }

    public String getJSON() {

        SQLiteDatabase db = this.getReadableDatabase();
        String json = "";
        Cursor cursor = db.query(TABLE_JSON, new String[] { KEY_JSON }, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            json = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return json;

    }

    public boolean checkJSON() {
        SQLiteDatabase db = this.getReadableDatabase();

        String json = "";
        Cursor cursor = db.query(TABLE_JSON, new String[] { KEY_JSON }, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            json = cursor.getString(0);
        }
        cursor.close();

        if(json.length() < 2) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public String getUsername() {

        SQLiteDatabase db = this.getReadableDatabase();
        String username = "";
        Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_USERNAME }, null,
                null, null, null, null, null);;
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            username = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return username;

    }

    public int getRowCount() {

        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        db.close();

        return rowCount;

    }

    public String getYear() {

        SQLiteDatabase db = this.getReadableDatabase();
        String year = "";
        Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_YEAR }, null,
                null, null, null, null, null);;
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            year = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return year;

    }

    public int getRole() {

        SQLiteDatabase db = this.getReadableDatabase();
        int role = 0;
        Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_ROLE }, null,
                null, null, null, null, null);;
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            role = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return role;

    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOGIN, null, null);
        db.delete(TABLE_JSON, null, null);
        db.close();
    }

}