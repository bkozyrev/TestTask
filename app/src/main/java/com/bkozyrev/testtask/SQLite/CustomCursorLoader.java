package com.bkozyrev.testtask.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class CustomCursorLoader extends CursorLoader {

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
                    cursor = mDataBase.getAllData();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;

            case DataBase.SEARCH_BY_ID:
                try {
                    cursor = mDataBase.getRecord(mSearchId);
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
