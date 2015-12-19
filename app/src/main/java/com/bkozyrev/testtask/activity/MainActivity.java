package com.bkozyrev.testtask.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.bkozyrev.testtask.R;
import com.bkozyrev.testtask.fragment.StaffFragment;

public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected int getLayoutResourceIdentifier() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean getDisplayHomeAsUp() {
        return true;
    }

    @Override
    protected boolean getHomeButtonEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        setupNavigationDrawerContent(navigationView);
        setSelectedItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_header_1:
                                setSelectedItem(0);
                                break;
                            case R.id.item_header_2:
                                setSelectedItem(1);
                                break;
                            case R.id.item_header_3:
                                setSelectedItem(2);
                                break;
                        }
                        menuItem.setChecked(true);

                        return true;
                    }
                });
    }

    public void setSelectedItem(int position){
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new StaffFragment();
                break;
            case 1:
                //fragment = new MyMeetingsFragment();
                break;
            case 2:
                //fragment = new CampusesFragment();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }
}