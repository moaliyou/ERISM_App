package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.erismapp.R;
import com.example.erismapp.activities.Dashboard;
import com.example.erismapp.activities.LoginActivity;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.UserHelperClass;
import com.example.erismapp.models.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Objects;

public class UserFragment extends Fragment {

    private View mainView;
    private MaterialToolbar toolbar;
    private TextInputLayout tfUserFullName, tfUsername,
            tfNewPassword, tfConfirm;
    private Button btnSaveChanges, btnLogout;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;
    private UserModel userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_user, container, false);

        initViews();
        eventHandling();

        return mainView;
    }

    private void fillUserData(String fullName) {
        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(UserHelperClass.getUserDate(fullName));

        if (mCursor.moveToFirst()) {

            userModel.setUserId(Integer.parseInt(mCursor.getString(0)));
            Objects.requireNonNull(tfUserFullName.getEditText()).setText(mCursor.getString(2));
            Objects.requireNonNull(tfUsername.getEditText()).setText(mCursor.getString(1));

        }

    }

    private void eventHandling() {
        btnSaveChanges.setOnClickListener(view -> {

            if (!isFieldEmpty()) {
                if (!isPasswordsMatch()) {
                    MyHelperClass.showToastMessage(
                            requireActivity(),
                            "Passwords do not match"
                    );
                } else {
                    saveUserChanges();
                    clearFields();
                }
            } else {
                MyHelperClass.showToastMessage(
                        requireActivity(),
                        getResources().getString(R.string.warning_empty_fields_string)
                );
            }

        });

        btnLogout.setOnClickListener(view -> {
            Intent mIntent = new Intent(requireActivity(), LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(mIntent);
        });
    }

    private void clearFields() {
        Objects.requireNonNull(tfNewPassword.getEditText()).setText("");
        Objects.requireNonNull(tfConfirm.getEditText()).setText("");
    }

    private void saveUserChanges() {
        String userFullName = Objects
                .requireNonNull(tfUserFullName.getEditText())
                .getText().toString();

        String username = Objects
                .requireNonNull(tfUsername.getEditText())
                .getText().toString();

        String newPassword = Objects
                .requireNonNull(tfNewPassword.getEditText())
                .getText().toString();

        HashMap<String, String> dataList = new HashMap<>();
        dataList.put(UserHelperClass.COLUMN_USERNAME, username);
        dataList.put(UserHelperClass.COLUMN_FULL_NAME, userFullName);
        dataList.put(UserHelperClass.COLUMN_PASSWORD, newPassword);

        mEmployeeRetirementDatabase
                .updateData(
                        UserHelperClass.TABLE_NAME,
                        UserHelperClass.COLUMN_ID,
                        String.valueOf(userModel.getUserId()),
                        dataList
                );

        toolbar.setTitle(userFullName);

    }

    private boolean isPasswordsMatch() {
        return Objects.requireNonNull(tfNewPassword.getEditText()).getText().toString()
                .equals(Objects.requireNonNull(tfConfirm.getEditText()).getText().toString());
    }

    @SuppressLint("InflateParams")
    private void initViews() {
        tfUserFullName = mainView.findViewById(R.id.tf_user_full_name);
        tfUsername = mainView.findViewById(R.id.tf_username);
        tfNewPassword = mainView.findViewById(R.id.tf_new_password);
        tfConfirm = mainView.findViewById(R.id.tf_confirm_password);
        btnSaveChanges = mainView.findViewById(R.id.btn_save_changes);
        btnLogout = mainView.findViewById(R.id.btn_log_out);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());
        userModel = new UserModel();

        toolbar = ((Dashboard) requireActivity())
                .findViewById(R.id.topAppBar);

        fillUserData(toolbar.getTitle().toString());

    }

    private boolean isFieldEmpty() {
        return (
                Objects.requireNonNull(tfUserFullName.getEditText())
                        .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfUsername.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfNewPassword.getEditText())
                                .getText().toString().trim().isEmpty() ||

                        Objects.requireNonNull(tfConfirm.getEditText())
                                .getText().toString().trim().isEmpty()
        );
    }

}