package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.RetirementBenefitAdapter;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.RetirementPlanHelperClass;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.RetirementBenefitModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RetirementBenefitFragment extends Fragment implements RecyclerViewInterface {

    ArrayAdapter<String> employeeNamesAdapter, retirementPlanAdapter;
    private View mainView;
    private FloatingActionButton fabAddRetirementBenefit;
    private SearchView searchViewRetirementBenefit;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;
    private AutoCompleteTextView drdEmployeeNames, drdContributionFrequency, drdRetirementPlans;
    private TextInputLayout tfContributionAmount, tfBenefitStartDate, tfBenefitEndDate;
    private RadioGroup rgBenefitType;
    private RadioButton rbSelectedBenefitType,
            rbPensionPayment, rbSocialSecurity, rbHealthInsurance, rbLifeInsurance;
    private RecyclerView retirementBenefitRecyclerView;
    private RetirementBenefitAdapter retirementBenefitAdapter;
    private ArrayList<RetirementBenefitModel> retirementBenefitList;
    private List<String> employeeNameList, contributionFrequencyList, retirementPlanList;
    private String planId, employeeId, contributionFrequency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_benefit, container, false);

        retirementBenefitDataView();
        initViews();
        eventHandling();

        return mainView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViews() {
        fabAddRetirementBenefit = mainView.findViewById(R.id.fab_retirement_benefit);
        retirementBenefitRecyclerView = mainView.findViewById(R.id.rv_retirement_benefit);
        searchViewRetirementBenefit = mainView.findViewById(R.id.search_view_retirement_benefit);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());

        retirementBenefitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retirementBenefitRecyclerView.setHasFixedSize(true);
        retirementBenefitAdapter = new RetirementBenefitAdapter(
                getContext(), retirementBenefitList, this
        );
        retirementBenefitRecyclerView.setAdapter(retirementBenefitAdapter);
        retirementBenefitAdapter.notifyDataSetChanged();
    }

    private void setContributionFrequency() {
        contributionFrequencyList = Arrays.asList(
                getResources().getStringArray(R.array.contributionFrequency)
        );

        ArrayAdapter<String> contributionFrequencyAdapter =
                new ArrayAdapter<>(
                        requireActivity(),
                        R.layout.dropdown_items,
                        contributionFrequencyList
                );

        drdContributionFrequency.setAdapter(contributionFrequencyAdapter);
    }

    private void listEmployeeNames(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            employeeNameList.add(mCursor.getString(0));
        }
    }

    private void setEmployeeNames() {
        employeeNameList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(EmployeeHelperClass.displayEmployeeNames());

        listEmployeeNames(mCursor);

        employeeNamesAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        employeeNameList
                );

        drdEmployeeNames.setAdapter(employeeNamesAdapter);
    }

    private void setRetirementPlans() {
        retirementPlanList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementPlanHelperClass.displayDataRetirementPlanTable());

        listPlanNames(mCursor);

        retirementPlanAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        retirementPlanList
                );

        drdRetirementPlans.setAdapter(retirementPlanAdapter);
    }

    private void listPlanNames(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            retirementPlanList.add(mCursor.getString(1));
        }
    }

    private void eventHandling() {

        fabAddRetirementBenefit.setOnClickListener(view -> {
            createRegistrationDialog();
            pickDateFor(tfBenefitStartDate);
            pickDateFor(tfBenefitEndDate);
            setEmployeeNames();
            setContributionFrequency();
            setRetirementPlans();
            findingSelectedEmployeeId();
            findingSelectedPlanId();
        });

        searchViewRetirementBenefit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRetirementBenefitList(newText);
                return true;
            }
        });

        retirementBenefitRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!(dy > 0)) {
                    fabAddRetirementBenefit.show();
                    return;
                }

                fabAddRetirementBenefit.hide();

            }
        });

    }

    private void findingSelectedEmployeeId() {
        drdEmployeeNames.setOnItemClickListener((parent, view, position, id) -> {
            String selectEmployee = parent.getItemAtPosition(position).toString();
            employeeId = "";

            Cursor mCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            EmployeeHelperClass.getEmployeeId(selectEmployee)
                    );

            if (mCursor.moveToFirst()) {
                employeeId = mCursor.getString(0);
            }

            MyHelperClass.showToastMessage(
                    view.getContext(),
                    employeeId
            );
        });
    }

    private void findingSelectedPlanId() {
        drdRetirementPlans.setOnItemClickListener((parent, view, position, id) -> {
            String selectPlan = parent.getItemAtPosition(position).toString();
            planId = "";

            Cursor mCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            RetirementPlanHelperClass.getPlanId(selectPlan)
                    );

            if (mCursor.moveToFirst()) {
                planId = mCursor.getString(0);
            }

            MyHelperClass.showToastMessage(
                    view.getContext(),
                    planId
            );
        });
    }

    private void filterRetirementBenefitList(String text) {
        ArrayList<RetirementBenefitModel> filteredRetirementBenefitList = new ArrayList<>();
        for (RetirementBenefitModel retirementBenefit :
                retirementBenefitList) {

            if (
                    retirementBenefit.getEmployeeName().toLowerCase()
                            .contains(text.toLowerCase()) ||
                            retirementBenefit.getBenefitType().toLowerCase()
                                    .contains(text.toLowerCase())
            ) {
                filteredRetirementBenefitList.add(retirementBenefit);
            }

        }

        retirementBenefitAdapter.setFilteredList(filteredRetirementBenefitList);

    }

    private void createRegistrationDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("New retirement benefit");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_retirement_benefit, null);
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

    private void initDialogViews(View view) {
        drdEmployeeNames = view.findViewById(R.id.drd_employee_names);
        drdContributionFrequency = view.findViewById(R.id.drd_contribution_frequency);
        drdRetirementPlans = view.findViewById(R.id.drd_retirement_plan);
        tfContributionAmount = view.findViewById(R.id.tf_contribution_amount);
        tfBenefitStartDate = view.findViewById(R.id.tf_benefit_start_date);
        tfBenefitEndDate = view.findViewById(R.id.tf_benefit_end_date);
        rgBenefitType = view.findViewById(R.id.rg_benefit_type);
        rbPensionPayment = view.findViewById(R.id.rb_pension_payments_type);
        rbSocialSecurity = view.findViewById(R.id.rb_social_security_type);
        rbHealthInsurance = view.findViewById(R.id.rb_health_insurance_type);
        rbLifeInsurance = view.findViewById(R.id.rb_life_insurance_type);

        int checkedRadioButtonId = rgBenefitType.getCheckedRadioButtonId();
        rbSelectedBenefitType = view.findViewById(checkedRadioButtonId);
    }

    private void retirementBenefitDataView() {
        retirementBenefitList = new ArrayList<>();
    }

    private void pickDateFor(TextInputLayout mTextInput) {
        Objects.requireNonNull(mTextInput.getEditText()).setOnClickListener(v -> {

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault()).format(new Date(selection));
                Objects.requireNonNull(mTextInput.getEditText()).setText(date);
            });

            datePicker.show(requireActivity().getSupportFragmentManager(), "tag");

        });
    }

    private void setUpdatableRetirementBenefitData(int position) {
        Objects.requireNonNull(drdEmployeeNames)
                .setText(retirementBenefitList.get(position).getEmployeeName());

        if (retirementBenefitList.get(position).getBenefitType().equalsIgnoreCase(rbPensionPayment.getText().toString())) {
            rbPensionPayment.setChecked(true);
        } else if (retirementBenefitList.get(position).getBenefitType().equalsIgnoreCase(rbSocialSecurity.getText().toString())) {
            rbSocialSecurity.setChecked(true);
        } else if (retirementBenefitList.get(position).getBenefitType().equalsIgnoreCase(rbHealthInsurance.getText().toString())) {
            rbHealthInsurance.setChecked(true);
        } else {
            rbLifeInsurance.setChecked(true);
        }

        Objects.requireNonNull(tfContributionAmount.getEditText())
                .setText(String.valueOf(retirementBenefitList.get(position).getContributionAmount()));

        Objects.requireNonNull(drdContributionFrequency)
                .setText(retirementBenefitList.get(position).getContributionFrequency());

        Objects.requireNonNull(tfBenefitStartDate.getEditText())
                .setText(String.valueOf(retirementBenefitList.get(position).getBenefitStartDate()));

        Objects.requireNonNull(tfBenefitEndDate.getEditText())
                .setText(retirementBenefitList.get(position).getBenefitEndDate());
    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update refinement benefit");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_retirement_benefit, null);
        initDialogViews(dialogView);

        setUpdatableRetirementBenefitData(position);

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
                        retirementBenefitList.get(position).getEmployeeName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    retirementBenefitList.remove(position);
                    retirementBenefitAdapter.notifyItemRemoved(position);
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