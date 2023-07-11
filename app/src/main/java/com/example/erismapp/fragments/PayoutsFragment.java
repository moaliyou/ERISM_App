package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.erismapp.R;
import com.example.erismapp.adapters.PayoutAdapter;
import com.example.erismapp.adapters.RetirementBenefitAdapter;
import com.example.erismapp.interfaces.RecyclerViewInterface;
import com.example.erismapp.models.PayoutModel;
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

public class PayoutsFragment extends Fragment implements RecyclerViewInterface {

    private View mainView;
    private FloatingActionButton fabAddPayout;
    private AutoCompleteTextView drdEmployeeNames, drdBenefitType;
    private TextInputLayout tfPayoutAmount, tfPayoutDate;
    private RecyclerView payoutRecyclerView;
    private PayoutAdapter payoutAdapter;
    private ArrayList<PayoutModel> payoutList;
    private List<String> employeeNameList, benefitTypeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_payouts, container, false);

        payoutDataView();
        initViews();
        eventHandling();

        return mainView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initViews() {
        fabAddPayout = mainView.findViewById(R.id.fab_add_new_payout);
        payoutRecyclerView = mainView.findViewById(R.id.rv_payout_list_view);

        payoutRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        payoutRecyclerView.setHasFixedSize(true);
        payoutAdapter = new PayoutAdapter(
                getContext(), payoutList, this
        );
        payoutRecyclerView.setAdapter(payoutAdapter);
        payoutAdapter.notifyDataSetChanged();
    }

    private void payoutDataView() {
        payoutList = new ArrayList<>();

        payoutList.add(
                new PayoutModel(
                        "Abdirahman Mohamed Ali",
                        "Pension Payments",
                        650,
                        "12-12-2002"
                )
        );

        payoutList.add(
                new PayoutModel(
                        "Ahmed Hajji Omar",
                        "Life Insurance",
                        70,
                        "01-11-2024"
                )
        );

        payoutList.add(
                new PayoutModel(
                        "Kamal Hassan Jamal",
                        "Social Security",
                        90,
                        "09-09-2030"
                )
        );

        payoutList.add(
                new PayoutModel(
                        "Hassan Ali Hussein",
                        "Health Insurance",
                        290,
                        "11-05-2025"
                )
        );

        payoutList.add(
                new PayoutModel(
                        "Ahmed Mohamed Abdi",
                        "Social Security",
                        110,
                        "15-08-2029"
                )
        );

    }

    private void setEmployeeNames() {
        employeeNameList = Arrays.asList(getResources().getStringArray(R.array.employeeNames));

        ArrayAdapter<String> employeeNamesAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        employeeNameList
                );

        drdEmployeeNames.setAdapter(employeeNamesAdapter);
    }

    private void setBenefitTypes() {
        benefitTypeList = Arrays.asList(getResources().getStringArray(R.array.retirementPlans));

        ArrayAdapter<String> retirementPlanAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.dropdown_items,
                        benefitTypeList
                );

        drdBenefitType.setAdapter(retirementPlanAdapter);
    }

    private void eventHandling() {

        fabAddPayout.setOnClickListener(view -> {
            createRegistrationDialog();
            setEmployeeNames();
            setBenefitTypes();
            pickDateFor(tfPayoutDate);
        });

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
            Toast.makeText(requireActivity(), "Saving data...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
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

    private void setUpdatablePayoutData(int position) {
        Objects.requireNonNull(drdEmployeeNames)
                .setText(payoutList.get(position).getEmployeeName());

        Objects.requireNonNull(drdBenefitType)
                .setText(payoutList.get(position).getBenefitType());

        Objects.requireNonNull(tfPayoutAmount.getEditText())
                .setText(String.valueOf(payoutList.get(position).getPayoutAmount()));

        Objects.requireNonNull(tfPayoutDate.getEditText())
                .setText(payoutList.get(position).getPayoutDate());
    }

    private void createUpdateDialog(int position) {
        AlertDialog updateDialog;
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(requireActivity());
        updateBuilder.setTitle("Update refinement benefit");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payout, null);
        initDialogViews(dialogView);

        setUpdatablePayoutData(position);

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
                        payoutList.get(position).getEmployeeName() +
                                " will no longer be in the list."
                )
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    payoutList.remove(position);
                    payoutAdapter.notifyItemRemoved(position);
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