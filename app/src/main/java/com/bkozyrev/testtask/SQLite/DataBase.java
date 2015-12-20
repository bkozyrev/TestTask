package com.bkozyrev.testtask.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {

    private static final String DB_NAME = "staffDataBase.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "Staff";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FIRST_NAME = "firstName";
    public static final String COLUMN_SECOND_NAME = "secondName";
    public static final String COLUMN_LAST_NAME = "lastName";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_IMAGE_PATH = "imagePath";

    public static final int SEARCH_ALL = 1;
    public static final int SEARCH_BY_ID = 2;

    private static final String DB_CREATE =
            "create table if not exists " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key, " + // autoincrement
                    COLUMN_FIRST_NAME + " text not null, " +
                    COLUMN_SECOND_NAME + " text not null, " +
                    COLUMN_LAST_NAME + " text not null, " +
                    COLUMN_AGE + " integer, " +
                    COLUMN_GENDER + " text not null, " +
                    COLUMN_IMAGE_PATH + " text not null" +
                    ");";

    private final Context mContext;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBase(Context context) {
        mContext = context;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRecord(String firstName, String secondName, String lastName, int age, String gender, String imagePath) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRST_NAME, firstName);
        cv.put(COLUMN_SECOND_NAME, secondName);
        cv.put(COLUMN_LAST_NAME, lastName);
        cv.put(COLUMN_AGE, age);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        mDB.insert(DB_TABLE, null, cv);
    }

    public void updateRecord(int id, String firstName, String secondName, String lastName, int age, String gender, String imagePath){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRST_NAME, firstName);
        cv.put(COLUMN_SECOND_NAME, secondName);
        cv.put(COLUMN_LAST_NAME, lastName);
        cv.put(COLUMN_AGE, age);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_IMAGE_PATH, imagePath);
        mDB.update(DB_TABLE, cv, COLUMN_ID + " = " + id, null);
    }

    // удалить запись из DB_TABLE
    public void delRecord(int id) {
        mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }

    // удалить последнюю запись из DB_TABLE
    public void delLastRecord() {
        Cursor cursor = mDB.query(DB_TABLE, null, null, null, null, null, null);
        int lastPosition = cursor.getCount();
        cursor.close();

        mDB.delete(DB_TABLE, COLUMN_ID + " = " + lastPosition, null);
    }

    //забрать запись по id
    public Cursor getRecord(int id){
        String selectQuery = "SELECT * FROM " + DB_TABLE + " WHERE " + COLUMN_ID + " = " + id;
        Log.d("query", selectQuery);

        //mDB.query(DB_TABLE, )

        return mDB.rawQuery(selectQuery, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
