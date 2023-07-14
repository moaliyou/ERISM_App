package com.example.erismapp.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.erismapp.R;
import com.example.erismapp.databinding.ActivityDashboardBinding;
import com.example.erismapp.fragments.EmployeeFragment;
import com.example.erismapp.fragments.HomeFragment;
import com.example.erismapp.fragments.PayoutsFragment;
import com.example.erismapp.fragments.RetirementBenefitFragment;
import com.example.erismapp.fragments.RetirementPlanFragment;
import com.example.erismapp.fragments.UserFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding dashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboardBinding.getRoot());

        replaceFragment(new HomeFragment());

        selectingNavigationItems();

        MaterialToolbar mTopAppBar = findViewById(R.id.topAppBar);

        mTopAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.user_profile_menu) {
                replaceFragment(new UserFragment());
            }

            return true;

        });

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