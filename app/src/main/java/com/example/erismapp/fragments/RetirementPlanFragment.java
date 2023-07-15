package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.RetirementPlanAdapter;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.RetirementPlanHelperClass;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.RetirementPlanModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RetirementPlanFragment extends Fragment implements RecyclerViewInterface {

    private FloatingActionButton fabAddRetirementPlan;
    private View mainView;
    private RecyclerView retirementPlanRecyclerView;
    private RetirementPlanAdapter retirementPlanAdapter;
    private ArrayList<RetirementPlanModel> retirementPlanList;
    private SearchView searchViewRetirementPlan;
    private TextInputLayout
            tfPlanName, tfPlanType, tfEmployerContributionRate,
            tfEmployeeContributionRate, tfVestingPeriod, tfMaxContributionLimit,
            tfMinContributionLimit;
    private TextView tvNoData;
    private ImageView ivInboxIcon;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_plan, container, false);

        initViews();
        retirementPlanDataView();
        eventHandling();

        retirementPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retirementPlanRecyclerView.setHasFixedSize(true);
        retirementPlanAdapter = new RetirementPlanAdapter(
                getContext(), retirementPlanList, this
        );
        retirementPlanRecyclerView.setAdapter(retirementPlanAdapter);
        retirementPlanAdapter.notifyDataSetChanged();

        return mainView;
    }

    private void initViews() {
        fabAddRetirementPlan = mainView.findViewById(R.id.fab_retirement_benefit);
        retirementPlanRecyclerView = mainView.findViewById(R.id.rv_retirement_plan);
        ivInboxIcon = mainView.findViewById(R.id.iv_inbox_icon);
        tvNoData = mainView.findViewById(R.id.tv_no_data);
        searchViewRetirementPlan = mainView.findViewById(R.id.search_view_retirement_plan);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());
    }

    private void eventHandling() {

        fabAddRetirementPlan.setOnClickListener(view -> {
            createRegistrationDialog();
        });

        searchViewRetirementPlan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRetirementPlanList(newText);
                return true;
            }
        });

        retirementPlanRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!(dy > 0)) {
                    fabAddRetirementPlan.show();
                    return;
                }

                fabAddRetirementPlan.hide();

            }
        });

    }

    private boolean isFieldEmpty() {
        return (
                Objects.requireNonNull(tfPlanName.getEditText())
                        .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfPlanType.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfEmployerContributionRate.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfEmployeeContributionRate.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfVestingPeriod.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfMaxContributionLimit.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfMinContributionLimit.getEditText())
                                .getText().toString().trim().isEmpty()
        );
    }

    private void createRegistrationDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("New retirement plan");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_retirement_plan, null);
        initDialogViews(dialogView);

        Button buttonCancel, buttonAction;
        buttonCancel = dialogView.findViewById(R.id.btn_cancel);
        buttonAction = dialogView.findViewById(R.id.btn_action);

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        buttonAction.setOnClickListener(view -> {

            if (!isFieldEmpty()) {
                insertNewRetirementPlan();
                dialog.dismiss();
            } else {
                MyHelperClass.showToastMessage(
                        requireActivity(),
                        getString(R.string.warning_empty_fields_string)
                );
            }
        });

    }

    private void insertNewRetirementPlan() {
        String planName = Objects.requireNonNull(tfPlanName.getEditText())
                .getText().toString().trim();
        String planType = Objects.requireNonNull(tfPlanType.getEditText())
                .getText().toString().trim();
        String employerContributionRate = Objects.requireNonNull(tfEmployerContributionRate.getEditText())
                .getText().toString().trim();
        String employeeContributionRate = Objects.requireNonNull(tfEmployeeContributionRate.getEditText())
                .getText().toString().trim();
        String vestingPeriod = Objects.requireNonNull(tfVestingPeriod.getEditText())
                .getText().toString().trim();
        String maxContributionLimit = Objects.requireNonNull(tfMaxContributionLimit.getEditText())
                .getText().toString().trim();
        String minContributionLimit = Objects.requireNonNull(tfMinContributionLimit.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(RetirementPlanHelperClass.COLUMN_PLAN_NAME, planName);
        dataList.put(RetirementPlanHelperClass.COLUMN_PLAN_TYPE, planType);
        dataList.put(RetirementPlanHelperClass.COLUMN_EMPLOYER_CONTRIBUTION_RATE, employerContributionRate);
        dataList.put(RetirementPlanHelperClass.COLUMN_EMPLOYEE_CONTRIBUTION_RATE, employeeContributionRate);
        dataList.put(RetirementPlanHelperClass.COLUMN_VESTING_PERIOD, vestingPeriod);
        dataList.put(RetirementPlanHelperClass.COLUMN_MAX_CONTRIBUTION_LIMIT, maxContributionLimit);
        dataList.put(RetirementPlanHelperClass.COLUMN_MIN_CONTRIBUTION_LIMIT, minContributionLimit);

        mEmployeeRetirementDatabase.insertData(RetirementPlanHelperClass.TABLE_NAME, dataList);
        refreshRecyclerViewData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterRetirementPlanList(String text) {
        ArrayList<RetirementPlanModel> filteredRetirementPlanList = new ArrayList<>();
        for (RetirementPlanModel plans :
                retirementPlanList) {

            if (
                    plans.getPlanName().toLowerCase().contains(text.toLowerCase()) ||
                            plans.getPlanType().toLowerCase().contains(text.toLowerCase())
            ) {
                filteredRetirementPlanList.add(plans);
            }

        }

        retirementPlanAdapter.setFilteredList(filteredRetirementPlanList);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshRecyclerViewData() {

        retirementPlanList.clear();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementPlanHelperClass.displayDataRetirementPlanTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listRetirementPlans(mCursor);
            retirementPlanAdapter.notifyDataSetChanged();
        }

    }

    private void listRetirementPlans(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            retirementPlanList.add(
                    new RetirementPlanModel(
                            Integer.parseInt(mCursor.getString(0)),
                            mCursor.getString(1),
                            mCursor.getString(2),
                            Double.parseDouble(mCursor.getString(3)),
                            Double.parseDouble(mCursor.getString(4)),
                            Integer.parseInt(mCursor.getString(5)),
                            Double.parseDouble(mCursor.getString(6)),
                            Double.parseDouble(mCursor.getString(7))
                    )
            );
        }
    }

    private void retirementPlanDataView() {
        retirementPlanList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementPlanHelperClass.displayDataRetirementPlanTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listRetirementPlans(mCursor);
            return;
        }

        ivInboxIcon.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);

    }

    private void initDialogViews(View view) {
        tfPlanName = view.findViewById(R.id.tf_plan_name);
        tfPlanType = view.findViewById(R.id.tf_plan_type);
        tfEmployerContributionRate = view.findViewById(R.id.tf_employer_contribution_rate);
        tfEmployeeContributionRate = view.findViewById(R.id.tf_employee_contribution_rate);
        tfVestingPeriod = view.findViewById(R.id.tf_vesting_period);
        tfMaxContributionLimit = view.findViewById(R.id.tf_max_contribution_limit);
        tfMinContributionLimit = view.findViewById(R.id.tf_min_contribution_limit);
    }

    private void setUpdatableRetirementPlanData(int position) {

        Objects.requireNonNull(tfPlanName.getEditText())
                .setText(String.valueOf(retirementPlanList.get(position).getPlanName()));

        Objects.requireNonNull(tfPlanType.getEditText())
                .setText(String.valueOf(retirementPlanList.get(position).getPlanType()));

        Objects.requireNonNull(tfEmployerContributionRate.getEditText())
                .setText(
                        String.valueOf(
                                retirementPlanList.get(position).getEmployerContributionRate()
                        )
                );

        Objects.requireNonNull(tfEmployeeContributionRate.getEditText())
                .setText(
                        String.valueOf(
                                retirementPlanList.get(position).getEmployeeContributionRate()
                        )
                );

        Objects.requireNonNull(tfVestingPeriod.getEditText())
                .setText(
                        String.valueOf(
                                retirementPlanList.get(position).getVestingPeriod()
                        )
                );

        Objects.requireNonNull(tfMaxContributionLimit.getEditText())
                .setText(
                        String.valueOf(
                                retirementPlanList.get(position).getMaxContributionLimit()
                        )
                );

        Objects.requireNonNull(tfMinContributionLimit.getEditText())
                .setText(
                        String.valueOf(
                                retirementPlanList.get(position).getMinContributionLimit()
                        )
                );
    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update refinement plan");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_retirement_plan, null);
        initDialogViews(dialogView);

        setUpdatableRetirementPlanData(position);

        Button buttonCancel, buttonAction;
        buttonCancel = dialogView.findViewById(R.id.btn_cancel);
        buttonAction = dialogView.findViewById(R.id.btn_action);
        buttonAction.setText("Update");

        updateBuilder.setView(dialogView);
        updateDialog = updateBuilder.create();
        updateDialog.setCancelable(false);
        updateDialog.show();

        buttonCancel.setOnClickListener(view -> updateDialog.dismiss());

        buttonAction.setOnClickListener(view -> {
            Toast.makeText(requireActivity(), "Updating data...", Toast.LENGTH_SHORT).show();
            updateDialog.dismiss();
        });


    }

    private void deleteDataAt(int position) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Delete from list?")
                .setMessage(
                        retirementPlanList.get(position).getPlanName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {

                    mEmployeeRetirementDatabase.deleteById(
                            RetirementPlanHelperClass.TABLE_NAME,
                            RetirementPlanHelperClass.COLUMN_ID,
                            String.valueOf(retirementPlanList.get(position).getRetirementPlanId())
                    );

                    retirementPlanList.remove(position);

                    if (retirementPlanList.size() < 1) {
                        ivInboxIcon.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                    retirementPlanAdapter.notifyItemRemoved(position);

                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onItemClick(int position) {
        createUpdateDialog(position);
    }

    @Override
    public void onItemLongClick(int position) {
        deleteDataAt(position);
    }
}