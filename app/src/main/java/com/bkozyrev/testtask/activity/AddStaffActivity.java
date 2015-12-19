package com.bkozyrev.testtask.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.SQLite.DataBase;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.OnClick;

public class AddStaffActivity extends BaseActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private DataBase mDataBase;
    private Uri mImagePath;

    @Bind(R.id.image_staff) ImageView mImageStaffView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.input_first_name) EditText mFirstNameView;
    @Bind(R.id.input_second_name) EditText mSecondNameView;
    @Bind(R.id.input_last_name) EditText mLastNameView;
    @Bind(R.id.input_gender) EditText mGenderView;
    @Bind(R.id.input_age) EditText mAge;
    @Bind(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_add_staff;
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

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.fab) void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @OnClick(R.id.add_staff_button) void addStaff() {
        attemptAddStaff();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            mImagePath = data.getData();

            try {
                Glide.with(mContext).load(mImagePath).into(mImageStaffView);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void attemptAddStaff() {

        mFirstNameView.setError(null);
        mSecondNameView.setError(null);
        mLastNameView.setError(null);
        mAge.setError(null);
        mGenderView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String secondName = mSecondNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String age = mAge.getText().toString();
        String gender = mGenderView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName) || !isNameValid(firstName)) {
            mFirstNameView.setError(mContext.getString(R.string.error_invalid_first_name));
            focusView = mFirstNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(secondName) || !isNameValid(secondName)) {
            mSecondNameView.setError(mContext.getString(R.string.error_invalid_second_name));
            focusView = mSecondNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName) || !isNameValid(lastName)) {
            mLastNameView.setError(mContext.getString(R.string.error_invalid_last_name));
            focusView = mLastNameView;
            cancel = true;
        }

        if (!isValidAge(age)) {
            mAge.setError(mContext.getString(R.string.error_invalid_age));
            focusView = mAge;
            cancel = true;
        }

        if (TextUtils.isEmpty(gender) || !isGenderValid(gender)) {
            mGenderView.setError(mContext.getString(R.string.error_invalid_gender));
            focusView = mGenderView;
            cancel = true;
        }

        if (mImagePath == null) {
            Snackbar.make(coordinatorLayout, "Choose a photo!", Snackbar.LENGTH_SHORT).show();
            cancel = true;
        }

        if (cancel) {
            if (focusView != null){
                focusView.requestFocus();
            }
        }
        else{
            mDataBase = new DataBase(this);
            mDataBase.open();
            Log.d("database", "open");
            mDataBase.addRecord(firstName, secondName, lastName, Integer.parseInt(age), gender, mImagePath.toString());

            Snackbar
                    .make(coordinatorLayout, "Added successfully!", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDataBase.delLastRecord();
                        }
                    })
                    .show();
        }
    }

    private boolean isNameValid(String name) {
        //TODO add logic
        return true;
    }

    private boolean isValidAge(String age){
        if(TextUtils.isEmpty(age))
            return false;
        else {
            if (Integer.parseInt(age) < 18 || Integer.parseInt(age) > 70)
                return false;
        }
        return true;
    }

    private boolean isGenderValid(String gender){
        return gender.equals("M") || gender.equals("F");
    }

    protected void onDestroy() {
        super.onDestroy();
        if(mDataBase != null) {
            mDataBase.close();
            Log.d("database", "close");
        }
    }
}
