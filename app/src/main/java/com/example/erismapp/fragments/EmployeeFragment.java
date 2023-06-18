package com.example.erismapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private View mView;
    private FloatingActionButton fabAddEmployee;
    private TextInputLayout tfDateOfBirth, tfHireDate;

    private AlertDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews();

        eventHandling();

        return mView;
    }

    private void eventHandling() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Register new employee");

        View innerView = getLayoutInflater().inflate(R.layout.employee_form_dialog, null);
        Button btnSaveEmployeeData = innerView.findViewById(R.id.btn_save_employee);
        Button btnCancelDialog = innerView.findViewById(R.id.btn_cancel);
        tfDateOfBirth = innerView.findViewById(R.id.tf_date_of_birth);
        tfHireDate = innerView.findViewById(R.id.tf_hire_date);

        btnSaveEmployeeData.setOnClickListener(
                view1 ->
                        Toast.makeText(getActivity(), "Saving data...", Toast.LENGTH_LONG).show()
        );

        btnCancelDialog.setOnClickListener(view -> {
            mDialog.dismiss();
            clearFields(innerView);
        });

        builder.setView(innerView);
        mDialog = builder.create();
        mDialog.setCancelable(false);

        fabAddEmployee.setOnClickListener(view -> mDialog.show());

        pickDateFor(tfDateOfBirth);
        pickDateFor(tfHireDate);

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

    private void initViews() {
        fabAddEmployee = mView.findViewById(R.id.fab_add_new_employee);
    }

    private void clearFields(View view) {

        TextInputLayout tfFirstName = view.findViewById(R.id.tf_first_name);
        TextInputLayout tfLastName = view.findViewById(R.id.tf_last_name);
        TextInputLayout tfDateOfBirth = view.findViewById(R.id.tf_date_of_birth);
        TextInputLayout tfJobTitle = view.findViewById(R.id.tf_job_title);
        TextInputLayout tfSalary = view.findViewById(R.id.tf_salary);
        TextInputLayout tfHireDate = view.findViewById(R.id.tf_hire_date);

        Objects.requireNonNull(tfFirstName.getEditText()).setText("");
        Objects.requireNonNull(tfLastName.getEditText()).setText("");
        Objects.requireNonNull(tfDateOfBirth.getEditText()).setText(R.string.string_date_format);
        Objects.requireNonNull(tfJobTitle.getEditText()).setText("");
        Objects.requireNonNull(tfSalary.getEditText()).setText("");
        Objects.requireNonNull(tfHireDate.getEditText()).setText(R.string.string_date_format);

        view.clearFocus();

    }
}