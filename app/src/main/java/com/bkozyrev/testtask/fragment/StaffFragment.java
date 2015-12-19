package com.bkozyrev.testtask.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.activity.AddStaffActivity;

public class StaffFragment extends Fragment {

    public StaffFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_staff, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStaffActivity.class);
                startActivity(intent);
            }
        });
    }
}
