package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.EmployeeAdapter;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private SearchView searchViewEmployee;
    private RecyclerView employeeRecyclerView;
    private EmployeeAdapter employeeAdapter;
    private TextInputLayout tfFirstName, tfLastName,
            tfJobTitle, tfSalary, tfDateOfBirth, tfHireDate;

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

        employeeArrayList.add(new EmployeeModel(1292, "Abdirahman",
                "Mohamed Ali", "Operator", 650,
                "12-12-2020", "09-02-2017"));

        employeeArrayList.add(new EmployeeModel(3432, "Ahmed",
                "Hajji Omar", "Chief Officer", 950,
                "12-12-1998", "03-02-2013"));

        employeeArrayList.add(new EmployeeModel(1356, "Kamal",
                "Hassan Jamal", "General Manager", 700,
                "12-12-1987", "23-05-2009"));

        employeeArrayList.add(new EmployeeModel(1226, "Aisha",
                "Omar Hassan", "Receipt Officer", 850,
                "22-04-1999", "23-05-2019"));

        employeeArrayList.add(new EmployeeModel(2342, "Ismail",
                "Ahmed Hassan", "Marketing", 760,
                "22-04-1993", "21-02-2012"));

        employeeArrayList.add(new EmployeeModel(2156, "Nurdin",
                "Ali Hussein", "Salesman", 850,
                "12-08-1991", "12-11-2012"));

        employeeArrayList.add(new EmployeeModel(1987, "Jamal",
                "Husni Farah", "Marketing", 550,
                "05-08-1998", "21-10-2020"));

        employeeArrayList.add(new EmployeeModel(1987, "Farhan",
                "Ahmed Mohamed", "Reporter", 650,
                "01-03-1992", "15-08-2011"));

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
                Toast.makeText(
                                requireActivity(),
                                "Saved successfully",
                                Toast.LENGTH_SHORT
                        )
                        .show();

                dialog.dismiss();
            }

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
        searchViewEmployee = mainView.findViewById(R.id.search_view_employee);
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

                Toast.makeText(
                                requireActivity(),
                                "Updating data...",
                                Toast.LENGTH_SHORT
                        )
                        .show();

                updateDialog.dismiss();

            } else {

                Toast.makeText(
                                requireActivity(),
                                "Meelaha banaan buuxi",
                                Toast.LENGTH_SHORT
                        )
                        .show();

            }

        });


    }

    @Override
    public void onItemClick(int position) {
        createUpdateDialog(position);
    }

    @Override
    public void onItemLongClick(int position) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Delete from list?")
                .setMessage(
                        employeeArrayList.get(position).getFullName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    employeeArrayList.remove(position);
                    employeeAdapter.notifyItemRemoved(position);
                    Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}