package com.bkozyrev.testtask.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bkozyrev.testtask.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolBar;
    protected Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceIdentifier());
        ButterKnife.bind(this);
        loadComponents();
        loadInfoView();
        initializeToolBar();
    }

    private void loadComponents() {
        mContext = getBaseContext();
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
    }

    private void loadInfoView() {
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    protected void initializeToolBar() {
        if (mToolBar != null) {
            mToolBar.setTitleTextColor(ContextCompat.getColor(mContext, R.color.white));
            getSupportActionBar().setDisplayHomeAsUpEnabled(getDisplayHomeAsUp());
            getSupportActionBar().setHomeButtonEnabled(getHomeButtonEnabled());
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    protected abstract int getLayoutResourceIdentifier();

    public Toolbar getToolBar() {
        return mToolBar;
    }

    protected abstract boolean getDisplayHomeAsUp();

    protected abstract boolean getHomeButtonEnabled();

}