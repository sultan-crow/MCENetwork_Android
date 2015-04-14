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

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_YEAR = "year";
    private static final String KEY_ROLE = "role";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_ROLE + " INTEGER,"
                + KEY_YEAR + " TEXT " + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);

    }

    public void addUser(String name, String email, String year, int role) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_YEAR, year);
        values.put(KEY_ROLE, role);

        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    /*public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("year", cursor.getString(3));
            user.put("uid", cursor.getString(4));
        }
        cursor.close();
        db.close();

        return user;
    }*/

    public String getEmail() {

        SQLiteDatabase db = this.getReadableDatabase();
        String email = "";
        Cursor cursor = db.query(TABLE_LOGIN, new String[] { KEY_EMAIL }, null,
                null, null, null, null, null);;
        cursor.moveToFirst();
        if(cursor.getCount() > 0) {
            email = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return email;

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

    public void resetTable_Login(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}