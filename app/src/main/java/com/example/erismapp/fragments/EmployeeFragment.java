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
import com.example.erismapp.adapters.EmployeeAdapter;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EmployeeFragment extends Fragment implements RecyclerViewInterface {

    ArrayList<EmployeeModel> employeeArrayList;
    private View mainView;
    private FloatingActionButton fabAddEmployee;
    private RecyclerView employeeRecyclerView;
    private TextInputLayout tfFirstName, tfLastName,
            tfJobTitle, tfSalary, tfDateOfBirth, tfHireDate;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews();

        eventHandling();

        employeeDataView();

        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeRecyclerView.setHasFixedSize(true);
        EmployeeAdapter employeeAdapter = new EmployeeAdapter(getContext(), employeeArrayList, this);
        employeeRecyclerView.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();

        return mainView;
    }

    private void employeeDataView() {
        employeeArrayList = new ArrayList<>();

        employeeArrayList.add(new EmployeeModel(1292, "Abdirahman",
                "Mohamed Ali", "Operator", 650,
                "12-12-2020", "09-02-2017"));

        employeeArrayList.add(new EmployeeModel(3432, "Ahmed",
                "Hajji Omar", "Chief Officer", 950,
                "12-12-1998", "03-02-2013"));

        employeeArrayList.add(new EmployeeModel(1356, "Kamal",
                "Hassan Jamal", "General Manager", 700,
                "12-12-1987", "23-05-2009"));

    }

    private void eventHandling() {

        fabAddEmployee.setOnClickListener(view -> {
            createRegistrationDialog();
            pickDateFor(tfDateOfBirth);
            pickDateFor(tfHireDate);
        });

    }

    private void initDialogViews(View view) {
        tfDateOfBirth = view.findViewById(R.id.tf_date_of_birth);
        tfHireDate = view.findViewById(R.id.tf_hire_date);
        tfFirstName = view.findViewById(R.id.tf_first_name);
        tfLastName = view.findViewById(R.id.tf_last_name);
        tfDateOfBirth = view.findViewById(R.id.tf_date_of_birth);
        tfJobTitle = view.findViewById(R.id.tf_job_title);
        tfSalary = view.findViewById(R.id.tf_salary);
        tfHireDate = view.findViewById(R.id.tf_hire_date);
    }

    private void createRegistrationDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Register employee");

        View dialogView = getLayoutInflater().inflate(R.layout.employee_form_dialog, null);
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
        employeeRecyclerView = mainView.findViewById(R.id.rv_employee_list_view);
    }

    private boolean isFieldsEmpty() {
        return !(
                Objects.requireNonNull(tfFirstName.getEditText()).getText().toString().trim().isEmpty() &&
                        Objects.requireNonNull(tfLastName.getEditText()).getText().toString().trim().isEmpty() &&
                        Objects.requireNonNull(tfJobTitle.getEditText()).getText().toString().trim().isEmpty() &&
                        Objects.requireNonNull(tfDateOfBirth.getEditText()).getText().toString().trim().isEmpty() &&
                        Objects.requireNonNull(tfSalary.getEditText()).getText().toString().trim().isEmpty() &&
                        Objects.requireNonNull(tfHireDate.getEditText()).getText().toString().trim().isEmpty()
        );
    }

    private void setUpdatableEmployeeData(int position) {
        Objects.requireNonNull(tfFirstName.getEditText())
                .setText(employeeArrayList.get(position).getFirstName());

        Objects.requireNonNull(tfLastName.getEditText())
                .setText(employeeArrayList.get(position).getLastName());

        Objects.requireNonNull(tfDateOfBirth.getEditText())
                .setText(employeeArrayList.get(position).getDateOfBirth());

        Objects.requireNonNull(tfJobTitle.getEditText())
                .setText(employeeArrayList.get(position).getJobTitle());

        Objects.requireNonNull(tfSalary.getEditText())
                .setText(String.valueOf(employeeArrayList.get(position).getSalary()));

        Objects.requireNonNull(tfHireDate.getEditText())
                .setText(employeeArrayList.get(position).getHireDate());
    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update employee");

        View dialogView = getLayoutInflater().inflate(R.layout.employee_form_dialog, null);
        initDialogViews(dialogView);

        setUpdatableEmployeeData(position);

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

    @Override
    public void onItemClick(int position) {
        createUpdateDialog(position);
    }
}