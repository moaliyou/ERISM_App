package com.example.erismapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erismapp.R;
import com.example.erismapp.database.EmployeeRetirementDatabase;
import com.example.erismapp.helpers.UserHelperClass;
import com.example.erismapp.models.UserModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_FULL_NAME = "full_name";

    private UserModel defaultUser;
    private TextInputLayout tfUsername, tfPassword;
    private EmployeeRetirementDatabase mEmployeeRetirementDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        tfUsername = findViewById(R.id.tf_username_layout);
        tfPassword = findViewById(R.id.tf_password_layout);
        mEmployeeRetirementDatabase = new EmployeeRetirementDatabase(this);
        defaultUser = new UserModel();
    }

    public void signup(View view) {
        Intent mIntent = new Intent(LoginActivity.this, SignupActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }

    private boolean isFieldEmpty() {
        return (
                (Objects.requireNonNull(tfUsername.getEditText())
                        .getText().toString().trim().isEmpty()) ||

                        (Objects.requireNonNull(tfPassword.getEditText())
                                .getText().toString().trim().isEmpty())
        );
    }

    public void startSystem(View view) {
        if (!isFieldEmpty()) {
            checkUser();
        } else {
            Toast.makeText(this, "Fadlan buuxi meelaha banaan", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUser() {
        String username = Objects.requireNonNull(tfUsername.getEditText())
                .getText().toString().trim();

        String password = Objects.requireNonNull(tfPassword.getEditText())
                .getText().toString().trim();

        if (isUserExist(username, password)) {
            Intent mIntent = new Intent(LoginActivity.this, Dashboard.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtra(EXTRA_USERNAME, username);
            mIntent.putExtra(EXTRA_FULL_NAME, defaultUser.getFullName());
            startActivity(mIntent);
        } else {
            Toast.makeText(
                            this,
                            "Incorrect username or password",
                            Toast.LENGTH_SHORT
                    )
                    .show();
        }
    }

    private boolean isUserExist(String username, String password) {

        Cursor mCursor = mEmployeeRetirementDatabase
                .readDataFrom(UserHelperClass.checkUserInfo(username, password));

        if (mCursor.moveToFirst()) {
            defaultUser.setFullName(mCursor.getString(2));
            return true;
        }

        return false;
    }
}