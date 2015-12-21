package com.bkozyrev.testtask.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.database.CustomCursorLoader;
import com.bkozyrev.testtask.database.DataBase;
import com.bkozyrev.testtask.adapter.StaffRecyclerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StaffFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{
    public static final String TAG = StaffFragment.class.getSimpleName();
    public static final String ITEM_ID_TAG = "item_id";

    private DataBase mDataBase;
    private ArrayList<Integer> valuesIds;

    @Bind(R.id.staff_list) RecyclerView mStaffList;

    @OnClick(R.id.fab) void addStaff() {
        createFragment(new AddStaffFragment(), TAG);
    }

    public StaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        valuesIds = new ArrayList<>();

        mStaffList.setHasFixedSize(true);
        mStaffList.setItemAnimator(new DefaultItemAnimator());
        mStaffList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        initialiseDB();

        /*getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        initialiseDB();
                    }
                });*/
    }

    public void initialiseDB(){
        mDataBase = new DataBase(getActivity());
        mDataBase.open();

        Log.d("database", "open");
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        return new CustomCursorLoader(getContext(), mDataBase, DataBase.SEARCH_ALL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor != null) {
            mStaffList.setAdapter(new StaffRecyclerAdapter(getContext(), cursor, this));
            saveDatabaseValuesIds(cursor);
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

    @Override
    public void onClick(View view) {

        int clickedPosition = mStaffList.getChildLayoutPosition(view);

        Bundle arguments = new Bundle();
        arguments.putInt(ITEM_ID_TAG, valuesIds.get(clickedPosition));

        InfoStaffFragment fragment = new InfoStaffFragment();
        fragment.setArguments(arguments);

        createFragment(fragment, "InfoStaffFragmentTag");
    }

    public void saveDatabaseValuesIds(Cursor cursor){
        if(cursor != null){
            while(cursor.moveToNext()){
                valuesIds.add(cursor.getInt(cursor.getColumnIndex(DataBase.COLUMN_ID)));
            }
        }
    }

    public void createFragment(Fragment fragment, String tag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.staff_layout, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
