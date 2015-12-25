package com.bkozyrev.testtask.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DataBase extends ContentProvider {

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

    final String LOG_TAG = "myLogs";
    // // Uri
    // authority
    public static final String AUTHORITY = "com.bkozyrev.testtask.provider";

    // path
    public static final String STAFF_PATH = "staff";

    // Общий Uri
    public static final Uri STAFF_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + STAFF_PATH);

    // Типы данных
    // набор строк
    public static final String STAFF_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + STAFF_PATH;

    // одна строка
    public static final String STAFF_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + STAFF_PATH;

    //// UriMatcher
    // общий Uri
    public static final int URI_STAFF = 1;

    // Uri с указанным ID
    public static final int URI_STAFF_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, STAFF_PATH, URI_STAFF);
        uriMatcher.addURI(AUTHORITY, STAFF_PATH + "/#", URI_STAFF_ID);
    }

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(getContext(), DB_NAME, null, DB_VERSION);
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

    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        mDBHelper = new DBHelper(getContext(), DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return true;
    }

    // чтение
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query, " + uri.toString());
        // проверяем Uri
        switch (uriMatcher.match(uri)) {
            case URI_STAFF: // общий Uri
                Log.d(LOG_TAG, "URI_STAFF");
                // если сортировка не указана, ставим свою - по имени
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_LAST_NAME + " ASC";
                }
                break;
            case URI_STAFF_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                // добавляем ID к условию выборки
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        Cursor cursor = mDB.query(DB_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        // просим ContentResolver уведомлять этот курсор
        // об изменениях данных в STAFF_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), STAFF_CONTENT_URI);

        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());
        if (uriMatcher.match(uri) != URI_STAFF)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        long rowID = mDB.insert(DB_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(STAFF_CONTENT_URI, rowID);
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_STAFF:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_STAFF_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = mDB.delete(DB_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }


    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_STAFF:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_STAFF_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + id;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        int cnt = mDB.update(DB_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_STAFF:
                return STAFF_CONTENT_TYPE;
            case URI_STAFF_ID:
                return STAFF_CONTENT_ITEM_TYPE;
        }
        return null;
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
            ContentValues cv = new ContentValues();
            for (int i = 1; i <= 3; i++) {
                cv.put(COLUMN_FIRST_NAME, "i = " + i);
                cv.put(COLUMN_SECOND_NAME, "456");
                cv.put(COLUMN_LAST_NAME, "789");
                cv.put(COLUMN_AGE, 19);
                cv.put(COLUMN_GENDER, "M");
                cv.put(COLUMN_IMAGE_PATH, "content://media/external/images/1");
                db.insert(DB_TABLE, null, cv);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
