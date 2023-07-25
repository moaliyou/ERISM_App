package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.erismapp.R;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.RetirementPlanHelperClass;

public class HomeFragment extends Fragment {

    private View mainView;
    private TextView tvNumOfEmployees, tvMaxContributionLimit,
            tvMinContributionLimit, tvHighestVestingPeriod;

    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        eventHandling();

        return mainView;
    }

    @SuppressLint("DefaultLocale")
    private void eventHandling() {
        tvNumOfEmployees.setText(getNumOfEmployees());
        tvMaxContributionLimit.setText(String.format("$%,.0f", getMaxContribution()));
        tvMinContributionLimit.setText(String.format("$%,.0f", getMinContribution()));
        tvHighestVestingPeriod.setText(String.format("%d years", getHighestVestingPeriod()));
    }

    private void initView() {
        tvNumOfEmployees = mainView.findViewById(R.id.tv_employee_name);
        tvMaxContributionLimit = mainView.findViewById(R.id.tv_max_contribution);
        tvMinContributionLimit = mainView.findViewById(R.id.tv_min_contribution);
        tvHighestVestingPeriod = mainView.findViewById(R.id.tv_highest_vesting_period);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());
    }

    private String getNumOfEmployees() {
        String query = "SELECT COUNT(" + EmployeeHelperClass.COLUMN_ID + ")" +
                " FROM " + EmployeeHelperClass.TABLE_NAME + ";";

        Cursor mCursor = mEmployeeRetirementDatabase.readDataFrom(query);

        return mCursor.moveToFirst() ? mCursor.getString(0) : "0";

    }

    private Double getMaxContribution() {
        String query = "SELECT MAX(" + RetirementPlanHelperClass.COLUMN_MAX_CONTRIBUTION_LIMIT + ")" +
                " FROM " + RetirementPlanHelperClass.TABLE_NAME + ";";

        Cursor mCursor = mEmployeeRetirementDatabase.readDataFrom(query);

        if (mCursor.moveToFirst() && mCursor.getString(0) != null) {
            return Double.parseDouble(mCursor.getString(0));
        }

        return 0.0;
    }

    private Double getMinContribution() {
        String query = "SELECT MIN(" + RetirementPlanHelperClass.COLUMN_MIN_CONTRIBUTION_LIMIT + ")" +
                " FROM " + RetirementPlanHelperClass.TABLE_NAME + ";";

        Cursor mCursor = mEmployeeRetirementDatabase.readDataFrom(query);

        if (mCursor.moveToFirst() && mCursor.getString(0) != null) {
            return Double.parseDouble(mCursor.getString(0));
        }

        return 0.0;
    }

    private int getHighestVestingPeriod() {
        String query = "SELECT MAX(" + RetirementPlanHelperClass.COLUMN_VESTING_PERIOD + ")" +
                " FROM " + RetirementPlanHelperClass.TABLE_NAME + ";";

        Cursor mCursor = mEmployeeRetirementDatabase.readDataFrom(query);

        if (mCursor.moveToFirst() && mCursor.getString(0) != null) {
            return Integer.parseInt(mCursor.getString(0));
        }

        return 0;
    }

}