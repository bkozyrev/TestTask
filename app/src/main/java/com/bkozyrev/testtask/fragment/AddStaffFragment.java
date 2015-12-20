package com.bkozyrev.testtask.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.SQLite.DataBase;
import com.bkozyrev.testtask.activity.MainActivity;
import com.bkozyrev.testtask.model.Staff;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddStaffFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    private DataBase mDataBase;
    private Uri mImagePath;
    private Staff mStaff;
    private boolean mUpdate;
    private Context mContext;

    @Bind(R.id.image_staff) ImageView mImageStaffView;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.input_first_name) EditText mFirstNameView;
    @Bind(R.id.input_second_name) EditText mSecondNameView;
    @Bind(R.id.input_last_name) EditText mLastNameView;
    @Bind(R.id.input_gender) EditText mGenderView;
    @Bind(R.id.input_age) EditText mAge;
    @Bind(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

    public AddStaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_staff, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();

        Bundle arg = getArguments();

        if(arg != null){
            mUpdate = true;
            mStaff = arg.getParcelable(InfoStaffFragment.STAFF_TAG);
            mImagePath = Uri.parse(mStaff.getImagePath());
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if(mUpdate) {
            displayStaffInfo();
        }
    }

    @OnClick(R.id.fab) void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @OnClick(R.id.add_staff_button) void addStaff() {
        attemptAddStaff();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

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
            mDataBase = new DataBase(mContext);
            mDataBase.open();
            Log.d("database", "open");
            if(mUpdate) {
                mDataBase.updateRecord(mStaff.getId(), firstName, secondName, lastName, Integer.parseInt(age), gender, mImagePath.toString());
            }
            else{
                mDataBase.addRecord(firstName, secondName, lastName, Integer.parseInt(age), gender, mImagePath.toString());
            }

            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
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

    private void displayStaffInfo(){
        mFirstNameView.setText(mStaff.getFirstName());
        mSecondNameView.setText(mStaff.getSecondName());
        mLastNameView.setText(mStaff.getLastName());
        mAge.setText(String.valueOf(mStaff.getAge()));
        mGenderView.setText(mStaff.getGender());
        Glide.with(mContext).load(Uri.parse(mStaff.getImagePath())).into(mImageStaffView);
    }

    public void onDestroy() {
        super.onDestroy();
        if(mDataBase != null) {
            mDataBase.close();
            Log.d("database", "close");
        }
    }
}
