package com.bkozyrev.testtask.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.database.CustomCursorLoader;
import com.bkozyrev.testtask.database.DataBase;
import com.bkozyrev.testtask.model.Staff;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoStaffFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG = InfoStaffFragment.class.getSimpleName();

    private DataBase mDataBase;
    private int mId;
    private String mFirstName, mSecondName, mLastName, mGender, mImagePath;
    private int mAge = -1;
    private Context mContext;

    @Bind(R.id.name_staff) TextView mNameStaffView;
    @Bind(R.id.age_staff) TextView mAgeStaffView;
    @Bind(R.id.gender_staff) TextView mGenderStaffView;
    @Bind(R.id.image_staff) ImageView mImageStaff;
    @Bind(R.id.linear_layout) LinearLayout mLayout;
    //@Bind(R.id.edit_staff_button) Button mEditButton;
    //@Bind(R.id.delete_staff_button) Button mDeleteButton;

    public InfoStaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();

        Bundle arg = getArguments();
        mId = arg.getInt(StaffFragment.ITEM_ID_TAG);

        View view = inflater.inflate(R.layout.fragment_info_staff, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        mDataBase = new DataBase(getContext());
        mDataBase.open();
        Log.d(TAG, "database open");

        getLoaderManager().initLoader(0, null, this);
    }

    @OnClick(R.id.edit_staff_button) void edit(){
        editStaff();
    }

    @OnClick(R.id.delete_staff_button) void delete(){
        deleteStaff();
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

            try {
                mFirstName = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_FIRST_NAME));
                mSecondName = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_SECOND_NAME));
                mLastName = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_LAST_NAME));
                mGender = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_GENDER));
                mImagePath = cursor.getString(cursor.getColumnIndex(DataBase.COLUMN_IMAGE_PATH));
                mAge = cursor.getInt(cursor.getColumnIndex(DataBase.COLUMN_AGE));
            }catch (NullPointerException exception){
                exception.printStackTrace();
            }

            cursor.close();

            //displayInfoFromDB(new Staff(firstName, secondName, lastName, age, gender, imagePath));
            displayInfoFromDB(mFirstName, mSecondName, mLastName, mAge, mGender, mImagePath);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void deleteStaff(){
        mDataBase.open();
        mDataBase.delRecord(mId);

        Snackbar.make(mLayout, "Deleted successfully", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataBase.addRecord(mFirstName, mSecondName, mLastName, mAge, mGender, mImagePath);
                    }
                })
                .show();
    }

    public void editStaff(){

        AddStaffFragment fragment = new AddStaffFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(TAG, new Staff(mId, mFirstName, mSecondName, mLastName, mAge, mGender, mImagePath));
        fragment.setArguments(arguments);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.staff_layout, fragment, "AddStaffFragmentTag");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDataBase != null) {
            mDataBase.close();
            Log.d(TAG, "close");
        }
    }
}
