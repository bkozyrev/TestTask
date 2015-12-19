package com.bkozyrev.testtask.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.bkozyrev.testtask.SQLite.CustomCursorLoader;
import com.bkozyrev.testtask.SQLite.DataBase;
import com.bkozyrev.testtask.activity.AddStaffActivity;
import com.bkozyrev.testtask.activity.StaffActivity;
import com.bkozyrev.testtask.adapter.DividerItemDecoration;
import com.bkozyrev.testtask.adapter.StaffRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StaffFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    public static final String ITEM_ID_TAG = "item_id";

    private DataBase mDataBase;

    @Bind(R.id.staff_list) RecyclerView mStaffList;

    @OnClick(R.id.fab) void addStaff() {
        Intent intent = new Intent(getContext(), AddStaffActivity.class);
        startActivity(intent);
    }

    public StaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mStaffList.setHasFixedSize(true);
        mStaffList.setItemAnimator(new DefaultItemAnimator());
        mStaffList.setLayoutManager(new LinearLayoutManager(getContext()));
        mStaffList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

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

        Intent intent = new Intent(getActivity(), StaffActivity.class);
        intent.putExtra(ITEM_ID_TAG, clickedPosition + 1);
        startActivity(intent);
    }
}
