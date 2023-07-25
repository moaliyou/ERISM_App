package com.example.erismapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.erismapp.R;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.databinding.ActivityDashboardBinding;
import com.example.erismapp.fragments.EmployeeFragment;
import com.example.erismapp.fragments.HomeFragment;
import com.example.erismapp.fragments.PayoutsFragment;
import com.example.erismapp.fragments.RetirementBenefitFragment;
import com.example.erismapp.fragments.RetirementPlanFragment;
import com.example.erismapp.fragments.UserFragment;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.google.android.material.appbar.MaterialToolbar;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding dashboardBinding;
    private MaterialToolbar mTopAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboardBinding.getRoot());

        initViews();

        replaceFragment(new HomeFragment());

        selectingNavigationItems();

        setUserFullNameToTopAppBar();

        eventHandling();

    }

    private void eventHandling() {
        mTopAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.user_profile_menu) {
                replaceFragment(new UserFragment());
            }

            return true;

        });
    }

    private void setUserFullNameToTopAppBar() {
        Intent mIntent = getIntent();

        mTopAppBar.setTitle(mIntent.getStringExtra(LoginActivity.EXTRA_FULL_NAME));
    }

    private void initViews() {
        mTopAppBar = findViewById(R.id.topAppBar);
    }

    private void selectingNavigationItems() {

        dashboardBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_menu_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.nav_menu_employee) {
                replaceFragment(new EmployeeFragment());
            } else if (itemId == R.id.nav_menu_retirement_benefit) {
                replaceFragment(new RetirementBenefitFragment());
            } else if (itemId == R.id.nav_menu_retirement_plan) {
                replaceFragment(new RetirementPlanFragment());
            } else if (itemId == R.id.nav_menu_payout) {
                replaceFragment(new PayoutsFragment());
            }

            return true;

        });

    }

    private void replaceFragment(Fragment mFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, mFragment);
        fragmentTransaction.commit();
    }
}