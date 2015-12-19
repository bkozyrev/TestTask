package com.bkozyrev.testtask.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkozyrev.testtask.R;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.OnClick;

public class AddStaffActivity extends BaseActivity {

    public static final int RESULT_LOAD_IMAGE = 1;
    private Context mContext;

    @Bind(R.id.image_staff) ImageView mImageStaffView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.input_first_name) EditText mFirstNameView;
    @Bind(R.id.input_second_name) EditText mSecondNameView;
    @Bind(R.id.input_last_name) EditText mLastNameView;
    @Bind(R.id.input_gender) EditText mGenderView;
    @Bind(R.id.add_staff_button) Button mAddStaffButton;

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

        mContext = getBaseContext();
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

            Uri selectedImage = data.getData();

            try {
                Glide.with(mContext).load(selectedImage).into(mImageStaffView);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void attemptAddStaff() {

        mFirstNameView.setError(null);
        mSecondNameView.setError(null);
        mLastNameView.setError(null);
        mGenderView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String secondName = mSecondNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
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

        if (TextUtils.isEmpty(gender) || !isGenderValid(gender)) {
            mGenderView.setError(mContext.getString(R.string.error_invalid_gender));
            focusView = mGenderView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
    }

    private boolean isNameValid(String name) {
        //TODO add logic
        return true;
    }

    private boolean isGenderValid(String gender){
        return gender.equals("M") || gender.equals("F");
    }
}
