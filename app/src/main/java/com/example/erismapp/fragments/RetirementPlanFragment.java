package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.RetirementPlanAdapter;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.RetirementPlanModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class RetirementPlanFragment extends Fragment implements RecyclerViewInterface {

    private FloatingActionButton fabAddRetirementPlan;
    private View mainView;
    private RecyclerView retirementPlanRecyclerView;
    private RetirementPlanAdapter retirementPlanAdapter;
    private ArrayList<RetirementPlanModel> retirementPlanList;
    private TextInputLayout
            tfPlanName, tfPlanType, tfEmployerContributionRate,
            tfEmployeeContributionRate, tfVestingPeriod, tfMaxContributionLimit,
            tfMinContributionLimit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_plan, container, false);

        retirementPlanDataView();
        initViews();
        eventHandling();

        return mainView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViews() {
        fabAddRetirementPlan = mainView.findViewById(R.id.fab_retirement_benefit);
        retirementPlanRecyclerView = mainView.findViewById(R.id.rv_retirement_plan);

        retirementPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retirementPlanRecyclerView.setHasFixedSize(true);
        retirementPlanAdapter = new RetirementPlanAdapter(
                getContext(), retirementPlanList, this
        );
        retirementPlanRecyclerView.setAdapter(retirementPlanAdapter);
        retirementPlanAdapter.notifyDataSetChanged();
    }

    private void eventHandling() {

        fabAddRetirementPlan.setOnClickListener(view -> {
            createRegistrationDialog();
        });

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
            Toast.makeText(requireActivity(), "Saving data...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    private void retirementPlanDataView() {
        retirementPlanList = new ArrayList<>();

        retirementPlanList.add(
                new RetirementPlanModel(
                        getResources().getStringArray(R.array.retirementPlans)[0],
                        "Pension Payments",
                        1.75,
                        2.5,
                        3,
                        22500,
                        5000
                )
        );

        retirementPlanList.add(
                new RetirementPlanModel(
                        getResources().getStringArray(R.array.retirementPlans)[2],
                        "Defined Contribution",
                        4.75,
                        8.5,
                        6,
                        15000,
                        2000
                )
        );

        retirementPlanList.add(
                new RetirementPlanModel(
                        getResources().getStringArray(R.array.retirementPlans)[1],
                        "Pension Payments",
                        2,
                        5,
                        2,
                        10000,
                        900
                )
        );

        retirementPlanList.add(
                new RetirementPlanModel(
                        getResources().getStringArray(R.array.retirementPlans)[0],
                        "Defined Benefit",
                        4,
                        9,
                        4,
                        25000,
                        3000
                )
        );

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
                    retirementPlanList.remove(position);
                    retirementPlanAdapter.notifyItemRemoved(position);
                    Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show();
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