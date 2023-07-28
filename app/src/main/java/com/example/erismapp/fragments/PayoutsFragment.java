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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.PayoutAdapter;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.PayoutHelperClass;
import com.example.erismapp.helpers.RetirementBenefitHelperClass;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.EmployeeModel;
import com.example.erismapp.models.PayoutModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PayoutsFragment extends Fragment implements RecyclerViewInterface {

    private View mainView;
    private FloatingActionButton fabAddPayout;
    private AutoCompleteTextView drdEmployeeNames, drdBenefitType;
    private TextInputLayout tfPayoutAmount, tfPayoutDate;
    private RecyclerView payoutRecyclerView;
    private PayoutAdapter payoutAdapter;
    private ArrayList<PayoutModel> payoutList;
    private List<String> employeeNameList, benefitTypeList;
    private SearchView searchViewPayout;
    private TextView tvNoData;
    private ImageView ivInboxIcon;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;
    private ArrayAdapter<String> employeeNamesAdapter, benefitTypeAdapter;
    private String employeeId, benefitId;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_payouts, container, false);


        initViews();
        payoutDataView();

        payoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        payoutRecyclerView.setHasFixedSize(true);
        payoutAdapter = new PayoutAdapter(
                getContext(), payoutList, this
        );
        payoutRecyclerView.setAdapter(payoutAdapter);
        payoutAdapter.notifyDataSetChanged();

        eventHandling();

        return mainView;
    }

    private void initViews() {
        fabAddPayout = mainView.findViewById(R.id.fab_add_new_payout);
        payoutRecyclerView = mainView.findViewById(R.id.rv_payout_list_view);
        ivInboxIcon = mainView.findViewById(R.id.iv_inbox_icon);
        searchViewPayout = mainView.findViewById(R.id.search_view_payout);
        tvNoData = mainView.findViewById(R.id.tv_no_data);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());

        searchViewPayout.clearFocus();
    }

    private void payoutDataView() {
        payoutList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(
                        PayoutHelperClass.displayDataPayoutTable()
                );

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listPayouts(mCursor);
            return;
        }

        ivInboxIcon.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.VISIBLE);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterPayoutList(String text) {
        ArrayList<PayoutModel> filteredPayoutList = new ArrayList<>();
        for (PayoutModel payout :
                payoutList) {

            if (
                    payout.getEmployeeName().toLowerCase().contains(text.toLowerCase()) ||
                            String.valueOf(payout.getPayoutDate()).contains(text.toLowerCase())
            )
                filteredPayoutList.add(payout);

        }

        payoutAdapter.setFilteredList(filteredPayoutList);

    }

    private void listPayouts(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            String employeeName = "", benefitType = "";

            Cursor employeeCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            EmployeeHelperClass.getEmployeeName(mCursor.getString(1))
                    );

            Cursor benefitCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            RetirementBenefitHelperClass.getBenefitTypes(mCursor.getString(2))
                    );

            if (employeeCursor.moveToFirst() && benefitCursor.moveToFirst()) {
                employeeName = employeeCursor.getString(0);
                benefitType = benefitCursor.getString(0);
            }

            payoutList.add(
                    new PayoutModel(
                            Integer.parseInt(mCursor.getString(0)),
                            employeeName,
                            benefitType,
                            Double.parseDouble(mCursor.getString(3)),
                            mCursor.getString(4)
                    )
            );
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

    private void setBenefitTypes(String pEmployeeId) {
        benefitTypeList = new ArrayList<>();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(RetirementBenefitHelperClass.displayBenefitTypesFor(pEmployeeId));

        listBenefitTypes(mCursor);

        benefitTypeAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        benefitTypeList
                );

        drdBenefitType.setAdapter(benefitTypeAdapter);
    }

    private void listEmployeeNames(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            employeeNameList.add(mCursor.getString(0));
        }
    }

    private void listBenefitTypes(Cursor mCursor) {
        while (mCursor.moveToNext()) {
            benefitTypeList.add(mCursor.getString(0));
        }
    }

    private void findingSelectedEmployeeId() {
        drdEmployeeNames.setOnItemClickListener((parent, view, position, id) -> {
            String selectEmployee = parent.getItemAtPosition(position).toString();
            employeeId = getSelectedEmployeeId(selectEmployee);
            setBenefitTypes(employeeId);
        });
    }

    private void findingSelectedBenefitTypeId() {
        drdBenefitType.setOnItemClickListener((parent, view, position, id) -> {
            String selectBenefitType = parent.getItemAtPosition(position).toString();
            benefitId = "";

            Cursor mCursor = mEmployeeRetirementDatabase
                    .readDataFrom(
                            RetirementBenefitHelperClass
                                    .getBenefitId(selectBenefitType, employeeId)
                    );

            if (mCursor.moveToFirst()) {
                benefitId = mCursor.getString(0);
            }

        });
    }

    private String getSelectedEmployeeId(String selectEmployee) {

        String selectedEmployeeId = "";

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(
                        EmployeeHelperClass.getEmployeeId(selectEmployee)
                );

        if (mCursor.moveToFirst()) {
            selectedEmployeeId = mCursor.getString(0);
        }

        return selectedEmployeeId;
    }

    private String getSelectedBenefitTypeId(String pSelectedPlan) {

        String selectedPlan = "";

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(
                        RetirementBenefitHelperClass.getBenefitTypes(pSelectedPlan)
                );

        if (mCursor.moveToFirst()) {
            selectedPlan = mCursor.getString(0);
        }

        return selectedPlan;
    }

    private void eventHandling() {

        fabAddPayout.setOnClickListener(view -> {
            createRegistrationDialog();
            setEmployeeNames();
            findingSelectedEmployeeId();
            findingSelectedBenefitTypeId();
            pickDateFor(tfPayoutDate);
        });

        searchViewPayout.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPayoutList(newText);
                return true;
            }
        });

        payoutRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!(dy > 0)) {
                    fabAddPayout.show();
                    return;
                }

                fabAddPayout.hide();

            }
        });

    }

    private void insertNewPayout() {
        String amount = Objects.requireNonNull(tfPayoutAmount.getEditText())
                .getText().toString().trim();
        String payoutDate = Objects.requireNonNull(tfPayoutDate.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(PayoutHelperClass.COLUMN_EMPLOYEE_ID, employeeId);
        dataList.put(PayoutHelperClass.COLUMN_BENEFIT_ID, benefitId);
        dataList.put(PayoutHelperClass.COLUMN_AMOUNT, amount);
        dataList.put(PayoutHelperClass.COLUMN_PAYOUT_DATE, payoutDate);

        mEmployeeRetirementDatabase.insertData(PayoutHelperClass.TABLE_NAME, dataList);

        refreshRecyclerViewData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshRecyclerViewData() {

        payoutList.clear();

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(PayoutHelperClass.displayDataPayoutTable());

        if (mCursor.getCount() != 0) {
            ivInboxIcon.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            listPayouts(mCursor);
            payoutAdapter.notifyDataSetChanged();
        }

    }

    private void createRegistrationDialog() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("New payment");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payout, null);
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
                insertNewPayout();
                dialog.dismiss();
            } else {
                MyHelperClass.showToastMessage(
                        requireContext(),
                        getResources().getString(R.string.warning_empty_fields_string)
                );
            }
        });

    }

    private void initDialogViews(View view) {
        drdEmployeeNames = view.findViewById(R.id.drd_employee_names);
        drdBenefitType = view.findViewById(R.id.drd_benefit_type);
        tfPayoutAmount = view.findViewById(R.id.tf_payout_amount);
        tfPayoutDate = view.findViewById(R.id.tf_payout_date);
    }

    private void pickDateFor(TextInputLayout mTextInput) {
        Objects.requireNonNull(mTextInput.getEditText()).setOnClickListener(v -> {

            CalendarConstraints.Builder constraintsBuilder =
                    new CalendarConstraints.Builder()
                            .setEnd(Calendar.getInstance().getTimeInMillis());

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Date")
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = new SimpleDateFormat("MM-dd-yyy", Locale.getDefault()).format(new Date(selection));
                Objects.requireNonNull(mTextInput.getEditText()).setText(date);
            });

            datePicker.show(requireActivity().getSupportFragmentManager(), "tag");

        });
    }

    private void setUpdatablePayoutData(int position) {
        Objects.requireNonNull(drdEmployeeNames)
                .setText(payoutList.get(position).getEmployeeName());

        Objects.requireNonNull(drdBenefitType)
                .setText(payoutList.get(position).getBenefitType());

        Objects.requireNonNull(tfPayoutAmount.getEditText())
                .setText(String.valueOf(payoutList.get(position).getPayoutAmount()));

        Objects.requireNonNull(tfPayoutDate.getEditText())
                .setText(payoutList.get(position).getPayoutDate());

        pickDateFor(tfPayoutDate);
    }

    private boolean isFieldEmpty() {
        return (
                Objects.requireNonNull(tfPayoutAmount.getEditText())
                        .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfPayoutDate.getEditText())
                                .getText().toString().equals(
                                        getResources().getString(R.string.string_date_format)) ||
                        drdEmployeeNames.getText().toString().trim().isEmpty() ||
                        drdBenefitType.getText().toString().trim().isEmpty()
        );
    }

    private void updatePayoutData(int position) {
        String amount = Objects.requireNonNull(tfPayoutAmount.getEditText())
                .getText().toString().trim();
        String payoutDate = Objects.requireNonNull(tfPayoutDate.getEditText())
                .getText().toString().trim();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(PayoutHelperClass.COLUMN_EMPLOYEE_ID, employeeId);
        dataList.put(PayoutHelperClass.COLUMN_BENEFIT_ID, benefitId);
        dataList.put(PayoutHelperClass.COLUMN_AMOUNT, amount);
        dataList.put(PayoutHelperClass.COLUMN_PAYOUT_DATE, payoutDate);

        mEmployeeRetirementDatabase
                .updateData(
                        PayoutHelperClass.TABLE_NAME,
                        PayoutHelperClass.COLUMN_ID,
                        String.valueOf(payoutList.get(position).getPayoutId()),
                        dataList
                );

        refreshRecyclerViewData();
    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update payout");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payout, null);
        initDialogViews(dialogView);

        setUpdatablePayoutData(position);

        setEmployeeNames();

        findingSelectedEmployeeId();
        findingSelectedBenefitTypeId();

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
                updatePayoutData(position);
                updateDialog.dismiss();
            } else {
                MyHelperClass.showToastMessage(
                        requireContext(),
                        getResources().getString(R.string.warning_empty_fields_string)
                );
            }
        });


    }

    private void deleteDataAt(int position) {
        new MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Delete from list?")
                .setMessage(
                        payoutList.get(position).getEmployeeName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {

                    mEmployeeRetirementDatabase.deleteById(
                            PayoutHelperClass.TABLE_NAME,
                            PayoutHelperClass.COLUMN_ID,
                            String.valueOf(
                                    payoutList
                                            .get(position)
                                            .getPayoutId()
                            )
                    );

                    payoutList.remove(position);
                    payoutAdapter.notifyItemRemoved(position);

                    if (payoutList.size() < 1) {
                        ivInboxIcon.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }

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