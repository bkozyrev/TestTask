package com.bkozyrev.testtask.database;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class CustomCursorLoader extends CursorLoader {

    public static final Uri STAFF_URI = Uri.parse("content://com.bkozyrev.testtask.provider/staff");
    final Uri STAFF_URI_ID = Uri.parse("content://com.bkozyrev.testtask.provider/staff/#");

    private DataBase mDataBase;
    private int mSearchType, mSearchId;

    public CustomCursorLoader(Context context, DataBase dataBase, int searchType) {
        super(context);
        mDataBase = dataBase;
        mSearchType = searchType;
    }

    public CustomCursorLoader(Context context, DataBase dataBase, int searchType, int searchId) {
        super(context);
        mDataBase = dataBase;
        mSearchType = searchType;
        mSearchId = searchId;
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;

        switch (mSearchType) {

            case DataBase.SEARCH_ALL:
                try {
                    cursor = getContext().getContentResolver().query(STAFF_URI, null, null, null, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;

            case DataBase.SEARCH_BY_ID:
                try {
                    cursor = getContext().getContentResolver().query(Uri.withAppendedPath(STAFF_URI, "" + mSearchId), null, null, null, null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
