package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import com.example.erismapp.helpers.RetirementBenefitHelperClass;
import com.example.erismapp.helpers.RetirementPlanHelperClass;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;
import com.example.erismapp.models.RetirementBenefitModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
    private String planId, employeeId, selectedContributionFrequency, selectedBenefitType;
    private TextView tvNoData;
    private ImageView ivInboxIcon;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_benefit, container, false);

        initViews();

        retirementBenefitDataView();

        retirementBenefitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        retirementBenefitRecyclerView.setHasFixedSize(true);
        retirementBenefitAdapter = new RetirementBenefitAdapter(
                getContext(), retirementBenefitList, this
        );
        retirementBenefitRecyclerView.setAdapter(retirementBenefitAdapter);
        retirementBenefitAdapter.notifyDataSetChanged();

        eventHandling();

        return mainView;
    }

    private void initViews() {
        fabAddRetirementBenefit = mainView.findViewById(R.id.fab_retirement_benefit);
        retirementBenefitRecyclerView = mainView.findViewById(R.id.rv_retirement_benefit);
        searchViewRetirementBenefit = mainView.findViewById(R.id.search_view_retirement_benefit);
        ivInboxIcon = mainView.findViewById(R.id.iv_inbox_icon);
        tvNoData = mainView.findViewById(R.id.tv_no_data);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());

        searchViewRetirementBenefit.clearFocus();
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
            findingSelectedContributionFrequency();
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

    private void findingSelectedContributionFrequency() {
        drdContributionFrequency.setOnItemClickListener((parent, view, position, id) -> {
            selectedContributionFrequency = parent.getItemAtPosition(position).toString();
            MyHelperClass.showToastMessage(
                    view.getContext(),
                    selectedContributionFrequency
            );
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

    private void findingSelectedBenefitType(View view) {
        rgBenefitType.setOnCheckedChangeListener((group, checkedId) -> {
            rbSelectedBenefitType = view.findViewById(checkedId);
            selectedBenefitType = rbSelectedBenefitType.getText().toString();
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

        findingSelectedBenefitType(dialogView);

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        buttonAction.setOnClickListener(view -> {

            if (!isFieldEmpty()) {
                insertNewRetirementBenefit();
                MyHelperClass.showToastMessage(
                        requireActivity(),
                        "Successfully inserted | " + selectedBenefitType
                );
                dialog.dismiss();
            } else {
                MyHelperClass.showToastMessage(
                        requireActivity(),
                        getResources().getString(R.string.warning_empty_fields_string)
                );
            }

        });

    }

    private void insertNewRetirementBenefit() {
        String contributionAmount = Objects.requireNonNull(tfContributionAmount.getEditText())
                .getText().toString().trim();
        String benefitStartDate = Objects.requireNonNull(tfBenefitStartDate.getEditText())
                .getText().toString().trim();
        String benefitEndDate = Objects.requireNonNull(tfBenefitEndDate.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(RetirementBenefitHelperClass.COLUMN_EMPLOYEE_ID, employeeId);
        dataList.put(RetirementBenefitHelperClass.COLUMN_BENEFIT_TYPE, selectedBenefitType);
        dataList.put(RetirementBenefitHelperClass.COLUMN_CONTRIBUTION_AMOUNT, contributionAmount);
        dataList.put(RetirementBenefitHelperClass.COLUMN_CONTRIBUTION_FREQUENCY, selectedContributionFrequency);
        dataList.put(RetirementBenefitHelperClass.COLUMN_PLAN_ID, planId);
        dataList.put(RetirementBenefitHelperClass.COLUMN_BENEFIT_START_DATE, benefitStartDate);
        dataList.put(RetirementBenefitHelperClass.COLUMN_BENEFIT_END_DATE, benefitEndDate);

        mEmployeeRetirementDatabase.insertData(RetirementBenefitHelperClass.TABLE_NAME, dataList);

        refreshRecyclerViewData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshRecyclerViewData() {

        retirementBenefitList.clear();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementBenefitHelperClass.displayDataRetirementBenefitTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listRetirementBenefits(mCursor);
            retirementBenefitAdapter.notifyDataSetChanged();
        }

    }

    private void listRetirementBenefits(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            String employeeName = "", planName = "";

            Cursor planCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            RetirementPlanHelperClass.getPlanName(mCursor.getString(3))
                    );

            Cursor employeeCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            EmployeeHelperClass.getEmployeeName(mCursor.getString(1))
                    );

            if (planCursor.moveToFirst() && employeeCursor.moveToFirst()) {
                employeeName = employeeCursor.getString(0);
                planName = planCursor.getString(0);
            }

            retirementBenefitList.add(
                    new RetirementBenefitModel(
                            Integer.parseInt(mCursor.getString(0)),
                            employeeName,
                            mCursor.getString(2),
                            Double.parseDouble(mCursor.getString(4)),
                            mCursor.getString(5),
                            planName,
                            mCursor.getString(6),
                            mCursor.getString(7)
                    )
            );
        }
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

    }

    private void retirementBenefitDataView() {
        retirementBenefitList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementBenefitHelperClass.displayDataRetirementBenefitTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listRetirementBenefits(mCursor);
            return;
        }

        ivInboxIcon.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);
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

    private boolean isFieldEmpty() {
        return (
                Objects.requireNonNull(tfContributionAmount.getEditText())
                        .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfBenefitStartDate.getEditText())
                                .getText().toString().equals(
                                        getResources().getString(R.string.string_date_format)) ||

                        Objects.requireNonNull(tfBenefitEndDate.getEditText())
                                .getText().toString().equals(
                                        getResources().getString(R.string.string_date_format)) ||

                        rgBenefitType.getCheckedRadioButtonId() == -1 ||
                        drdEmployeeNames.getText().toString().trim().isEmpty() ||
                        drdContributionFrequency.getText().toString().trim().isEmpty() ||
                        drdRetirementPlans.getText().toString().trim().isEmpty()
        );
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