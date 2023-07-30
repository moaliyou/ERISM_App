package com.example.erismapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erismapp.R;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.EmployeeHelperClass;
import com.example.erismapp.helpers.MyHelperClass;
import com.example.erismapp.helpers.UserHelperClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout tfUsername;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
    }

    private void initViews() {
        tfUsername = findViewById(R.id.tf_username_layout);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(this);
    }

    private boolean isFieldEmpty() {
        return Objects.requireNonNull(tfUsername.getEditText())
                        .getText().toString().trim().isEmpty();

    }

    public void login(View view) {
        Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }

    public void registerUser(View view) {

        String username = Objects.requireNonNull(tfUsername.getEditText())
                .getText().toString().trim();

        if (!isFieldEmpty()) {

            displayPasswordFor(username);

        } else {
            MyHelperClass.showToastMessage(
                    this,
                    getResources().getString(R.string.warning_empty_fields_string)
            );
        }

    }

    private void displayPasswordFor(String username) {
        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(UserHelperClass.getUserPassword(username));

        if (mCursor.moveToFirst()) {
            tfUsername.setErrorEnabled(false);
            showPassword(mCursor.getString(0));
        } else {
            tfUsername.setError("Couldn't find your account");
        }
    }

    private void showPassword(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete from list?")
                .setMessage(
                        "Your password: " + message
                )
                .setNegativeButton("Close", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}