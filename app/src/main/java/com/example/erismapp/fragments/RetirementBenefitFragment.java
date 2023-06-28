package com.example.erismapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.erismapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RetirementBenefitFragment extends Fragment {

    private View mainView;
    private FloatingActionButton fabAddRetirementBenefit;
    private AutoCompleteTextView drdEmployeeNames, drdContributionFrequency;
    private TextInputLayout tfContributionAmount, tfBenefitStartDate, tfBenefitEndDate;
    private RadioGroup rgBenefitType;
    private RadioButton rbSelectedBenefitType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_retirement_benefit, container, false);

        initViews();
        eventHandling();

        return mainView;
    }

    private void initViews() {
        fabAddRetirementBenefit = mainView.findViewById(R.id.fab_retirement_benefit);
    }

    private void setContributionFrequency() {
        String[] contributionFrequency = getResources().getStringArray(R.array.contributionFrequency);

        ArrayAdapter<String> contributionFrequencyAdapter =
                new ArrayAdapter<>(
                        requireActivity(),
                        R.layout.dropdown_items,
                        contributionFrequency
                );

        drdContributionFrequency.setAdapter(contributionFrequencyAdapter);
    }

    private void setEmployeeNames() {
        String[] employeeNames = getResources().getStringArray(R.array.employeeNames);

        ArrayAdapter<String> employeeNamesAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        employeeNames
                );

        drdEmployeeNames.setAdapter(employeeNamesAdapter);
    }

    private void eventHandling() {

        fabAddRetirementBenefit.setOnClickListener(view -> {
            createRegistrationDialog();
            pickDateFor(tfBenefitStartDate);
            pickDateFor(tfBenefitEndDate);
            setEmployeeNames();
            setContributionFrequency();
        });

    }

    private void createRegistrationDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("New retirement benefit");

        View dialogView = getLayoutInflater().inflate(R.layout.retirement_benefit_form_dialog, null);
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
        tfContributionAmount = view.findViewById(R.id.tf_contribution_amount);
        tfBenefitStartDate = view.findViewById(R.id.tf_benefit_start_date);
        tfBenefitEndDate = view.findViewById(R.id.tf_benefit_end_date);
        rgBenefitType = view.findViewById(R.id.rg_benefit_type);

        int checkedRadioButtonId = rgBenefitType.getCheckedRadioButtonId();
        rbSelectedBenefitType = view.findViewById(checkedRadioButtonId);
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

}