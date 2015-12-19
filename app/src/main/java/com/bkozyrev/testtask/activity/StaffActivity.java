package com.bkozyrev.testtask.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.SQLite.CustomCursorLoader;
import com.bkozyrev.testtask.SQLite.DataBase;
import com.bkozyrev.testtask.fragment.StaffFragment;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StaffActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private DataBase mDataBase;
    private int mId;

    @Bind(R.id.name_staff) TextView mNameStaffView;
    @Bind(R.id.age_staff) TextView mAgeStaffView;
    @Bind(R.id.gender_staff) TextView mGenderStaffView;
    @Bind(R.id.image_staff) ImageView mImageStaff;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_staff;
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mId = getIntent().getIntExtra(StaffFragment.ITEM_ID_TAG, -1);

        mDataBase = new DataBase(this);
        mDataBase.open();
        Log.d("database", "open");

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void displayInfoFromDB(String firstName, String secondName, String lastName, int age, String gender, String imagePath){
        if(firstName != null && secondName != null && lastName != null){
            mNameStaffView.setText(firstName + " " + secondName + " " + lastName);
        }

        if(age != -1){
            mAgeStaffView.setText("Age: " + String.valueOf(age));
        }

        if(gender != null){
            mGenderStaffView.setText("Gender: " + gender);
        }

        if(imagePath != null){
            Glide.with(mContext).load(Uri.parse(imagePath)).into(mImageStaff);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CustomCursorLoader(mContext, mDataBase, DataBase.SEARCH_BY_ID, mId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null) {
            cursor.moveToFirst();

            String firstName = null, secondName = null, lastName = null, gender = null, imagePath = null;
            int age = -1;

            try {
                firstName = cursor.getString(cursor.getColumnIndex("firstName"));
                secondName = cursor.getString(cursor.getColumnIndex("secondName"));
                lastName = cursor.getString(cursor.getColumnIndex("lastName"));
                gender = cursor.getString(cursor.getColumnIndex("gender"));
                imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
                age = cursor.getInt(cursor.getColumnIndex("age"));
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }

            cursor.close();
            mDataBase.close();

            //displayInfoFromDB(new Staff(firstName, secondName, lastName, age, gender, imagePath));
            displayInfoFromDB(firstName, secondName, lastName, age, gender, imagePath);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDataBase != null) {
            mDataBase.close();
            Log.d("database", "close");
        }
    }
}
