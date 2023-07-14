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
import com.example.erismapp.adapters.EmployeeAdapter;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EmployeeFragment extends Fragment implements RecyclerViewInterface {

    ArrayList<EmployeeModel> employeeArrayList;
    private View mainView;
    private FloatingActionButton fabAddEmployee;
    private SearchView searchViewEmployee;
    private RecyclerView employeeRecyclerView;
    private EmployeeAdapter employeeAdapter;
    private TextInputLayout tfFirstName, tfLastName,
            tfJobTitle, tfSalary, tfDateOfBirth, tfHireDate;
    private TextView tvNoData;
    private ImageView ivInboxIcon;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_employee, container, false);

        initViews();

        employeeDataView();

        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeRecyclerView.setHasFixedSize(true);
        employeeAdapter = new EmployeeAdapter(getContext(), employeeArrayList, this);
        employeeRecyclerView.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();

        eventHandling();

        return mainView;
    }

    private void employeeDataView() {
        employeeArrayList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(EmployeeHelperClass.displayDataEmployeeTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listEmployees(mCursor);
            return;
        }

        ivInboxIcon.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);

    }

    private void listEmployees(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            employeeArrayList.add(
                    new EmployeeModel(
                            Integer.parseInt(mCursor.getString(0)),
                            mCursor.getString(1),
                            mCursor.getString(2),
                            mCursor.getString(3),
                            mCursor.getString(4),
                            Double.parseDouble(mCursor.getString(5)),
                            mCursor.getString(6)
                    )
            );
        }
    }

    private void eventHandling() {

        fabAddEmployee.setOnClickListener(view -> {
            createRegistrationDialog();
            pickDateFor(tfDateOfBirth);
            pickDateFor(tfHireDate);
        });

        searchViewEmployee.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEmployeeList(newText);
                return true;
            }
        });

        employeeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!(dy > 0)) {
                    fabAddEmployee.show();
                    return;
                }

                fabAddEmployee.hide();

            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterEmployeeList(String text) {
        ArrayList<EmployeeModel> filteredEmployeeList = new ArrayList<>();
        for (EmployeeModel employee :
                employeeArrayList) {

            if (employee.getFullName().toLowerCase().contains(text.toLowerCase()))
                filteredEmployeeList.add(employee);

        }

        employeeAdapter.setFilteredList(filteredEmployeeList);

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

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_employee, null);
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

            if (isFieldEmpty()) {
                Toast.makeText(
                                requireActivity(),
                                "Fadlan buuxi meelaha banaan",
                                Toast.LENGTH_SHORT
                        )
                        .show();
            } else {

                insertNewEmployee();

                dialog.dismiss();
            }

        });

    }

    private void insertNewEmployee() {
        String firstName = Objects.requireNonNull(tfFirstName.getEditText())
                .getText().toString().trim();
        String lastName = Objects.requireNonNull(tfLastName.getEditText())
                .getText().toString().trim();
        String dateOfBirth = Objects.requireNonNull(tfDateOfBirth.getEditText())
                .getText().toString().trim();
        String jobTitle = Objects.requireNonNull(tfJobTitle.getEditText())
                .getText().toString().trim();
        String salary = Objects.requireNonNull(tfSalary.getEditText())
                .getText().toString().trim();
        String hireDate = Objects.requireNonNull(tfHireDate.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(EmployeeHelperClass.COLUMN_FIRST_NAME, firstName);
        dataList.put(EmployeeHelperClass.COLUMN_LAST_NAME, lastName);
        dataList.put(EmployeeHelperClass.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        dataList.put(EmployeeHelperClass.COLUMN_JOB_TITLE, jobTitle);
        dataList.put(EmployeeHelperClass.COLUMN_SALARY, salary);
        dataList.put(EmployeeHelperClass.COLUMN_HIRE_DATE, hireDate);

        mEmployeeRetirementDatabase.insertData(EmployeeHelperClass.TABLE_NAME, dataList);

        refreshRecyclerViewData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshRecyclerViewData() {

        employeeArrayList.clear();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(EmployeeHelperClass.displayDataEmployeeTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listEmployees(mCursor);
            employeeAdapter.notifyDataSetChanged();
        }

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
        searchViewEmployee = mainView.findViewById(R.id.search_view_employee);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());
        ivInboxIcon = mainView.findViewById(R.id.iv_inbox_icon);
        tvNoData = mainView.findViewById(R.id.tv_no_data);
        searchViewEmployee.clearFocus();
    }

    private boolean isFieldEmpty() {
        return (
                Objects.requireNonNull(tfFirstName.getEditText())
                        .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfLastName.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfJobTitle.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfDateOfBirth.getEditText())
                                .getText().toString().equals(
                                        getResources().getString(R.string.string_date_format)) ||

                        Objects.requireNonNull(tfSalary.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfHireDate.getEditText())
                                .getText().toString().equals(
                                        getResources().getString(R.string.string_date_format))
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

        pickDateFor(tfDateOfBirth);
        pickDateFor(tfHireDate);

    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update employee");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_employee, null);
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
            if (!isFieldEmpty()) {

                updateEmployeeData(position);
                updateDialog.dismiss();

            } else {

                Toast.makeText(
                                requireActivity(),
                                "Fadlan buuxi meelaha banaan",
                                Toast.LENGTH_SHORT
                        )
                        .show();

            }

        });


    }

    private void updateEmployeeData(int position) {
        String firstName = Objects.requireNonNull(tfFirstName.getEditText())
                .getText().toString().trim();
        String lastName = Objects.requireNonNull(tfLastName.getEditText())
                .getText().toString().trim();
        String dateOfBirth = Objects.requireNonNull(tfDateOfBirth.getEditText())
                .getText().toString().trim();
        String jobTitle = Objects.requireNonNull(tfJobTitle.getEditText())
                .getText().toString().trim();
        String salary = Objects.requireNonNull(tfSalary.getEditText())
                .getText().toString().trim();
        String hireDate = Objects.requireNonNull(tfHireDate.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(EmployeeHelperClass.COLUMN_FIRST_NAME, firstName);
        dataList.put(EmployeeHelperClass.COLUMN_LAST_NAME, lastName);
        dataList.put(EmployeeHelperClass.COLUMN_DATE_OF_BIRTH, dateOfBirth);
        dataList.put(EmployeeHelperClass.COLUMN_JOB_TITLE, jobTitle);
        dataList.put(EmployeeHelperClass.COLUMN_SALARY, salary);
        dataList.put(EmployeeHelperClass.COLUMN_HIRE_DATE, hireDate);

        mEmployeeRetirementDatabase
                .updateData(
                        EmployeeHelperClass.TABLE_NAME,
                        EmployeeHelperClass.COLUMN_ID,
                        String.valueOf(employeeArrayList.get(position).getEmployeeId()),
                        dataList
                );

        refreshRecyclerViewData();
    }

    @Override
    public void onItemClick(int position) {
        createUpdateDialog(position);
    }

    @Override
    public void onItemLongClick(int position) {
        deleteEmployee(position);
    }

    private void deleteEmployee(int position) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Delete from list?")
                .setMessage(
                        employeeArrayList.get(position).getFullName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {

                    mEmployeeRetirementDatabase.deleteById(
                            EmployeeHelperClass.TABLE_NAME,
                            EmployeeHelperClass.COLUMN_ID,
                            String.valueOf(employeeArrayList.get(position).getEmployeeId())
                    );

                    employeeArrayList.remove(position);

                    if (employeeArrayList.size() < 1) {
                        ivInboxIcon.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                    employeeAdapter.notifyItemRemoved(position);

                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}