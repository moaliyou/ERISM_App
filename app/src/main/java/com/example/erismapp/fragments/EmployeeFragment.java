package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EmployeeFragment extends Fragment {

    private View mainView, dialogView;
    private FloatingActionButton fabAddEmployee;
    private TextInputLayout tfFirstName, tfLastName,
            tfJobTitle, tfSalary, tfDateOfBirth, tfHireDate;

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews();

        eventHandling();

        return mainView;
    }

    private void eventHandling() {

        createRegistrationDialog();

        fabAddEmployee.setOnClickListener(view -> mDialog.show());

        pickDateFor(tfDateOfBirth);
        pickDateFor(tfHireDate);

    }

    private void createRegistrationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Register new employee");

        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            Toast.makeText(getActivity(), "Saving data...", Toast.LENGTH_LONG).show();
            clearFields();
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            mDialog.dismiss();
            clearFields();
        });

        builder.setView(dialogView);
        mDialog = builder.create();
        mDialog.setCancelable(false);
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

    @SuppressLint("InflateParams")
    private void initViews() {
        fabAddEmployee = mainView.findViewById(R.id.fab_add_new_employee);

        dialogView = getLayoutInflater().inflate(R.layout.employee_form_dialog, null);
        tfDateOfBirth = dialogView.findViewById(R.id.tf_date_of_birth);
        tfHireDate = dialogView.findViewById(R.id.tf_hire_date);
        tfFirstName = dialogView.findViewById(R.id.tf_first_name);
        tfLastName = dialogView.findViewById(R.id.tf_last_name);
        tfDateOfBirth = dialogView.findViewById(R.id.tf_date_of_birth);
        tfJobTitle = dialogView.findViewById(R.id.tf_job_title);
        tfSalary = dialogView.findViewById(R.id.tf_salary);
        tfHireDate = dialogView.findViewById(R.id.tf_hire_date);

    }

    private void clearFields() {
        Objects.requireNonNull(tfFirstName.getEditText()).setText("");
        Objects.requireNonNull(tfLastName.getEditText()).setText("");
        Objects.requireNonNull(tfDateOfBirth.getEditText()).setText(R.string.string_date_format);
        Objects.requireNonNull(tfJobTitle.getEditText()).setText("");
        Objects.requireNonNull(tfSalary.getEditText()).setText("");
        Objects.requireNonNull(tfHireDate.getEditText()).setText(R.string.string_date_format);
        dialogView.clearFocus();
    }
}