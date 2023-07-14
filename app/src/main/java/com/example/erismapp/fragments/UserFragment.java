package com.example.erismapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.erismapp.R;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.models.UserModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class UserFragment extends Fragment {

    ArrayList<UserModel> userArrayList;
    private View mainView;
    private TextInputLayout tfUserFullName, tfUsername,
            tfNewPassword, tfConfirm;
    private Button btnSaveChanges;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_user, container, false);

        initViews();
        eventHandling();

        return mainView;
    }

    private void eventHandling() {
        btnSaveChanges.setOnClickListener(view -> {

            if (!isFieldEmpty()) {
                Toast.makeText(requireActivity(), "Update user data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "fadlan buuxi meelaha banaan", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @SuppressLint("InflateParams")
    private void initViews() {
        tfUserFullName = mainView.findViewById(R.id.tf_user_full_name);
        tfUsername = mainView.findViewById(R.id.tf_username);
        tfNewPassword = mainView.findViewById(R.id.tf_new_password);
        tfConfirm = mainView.findViewById(R.id.tf_confirm_password);
        btnSaveChanges = mainView.findViewById(R.id.btn_save_changes);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(requireActivity());
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