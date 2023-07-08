package com.example.erismapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erismapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout tfUsername, tfPassword, tfConfirm, tfFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
    }

    private void initViews() {
        tfFullName = findViewById(R.id.tf_full_name_layout);
        tfUsername = findViewById(R.id.tf_username_layout);
        tfPassword = findViewById(R.id.tf_password_layout);
        tfConfirm = findViewById(R.id.tf_confirm_layout);
    }

    private boolean isFieldEmpty() {
        return Objects.requireNonNull(tfFullName.getEditText())
                .getText().toString().trim().isEmpty() ||

                Objects.requireNonNull(tfUsername.getEditText())
                        .getText().toString().trim().isEmpty() ||

                Objects.requireNonNull(tfPassword.getEditText())
                        .getText().toString().trim().isEmpty() ||

                Objects.requireNonNull(tfConfirm.getEditText())
                        .getText().toString().trim().isEmpty();

    }

    public void login(View view) {
        Intent mIntent = new Intent(SignupActivity.this, LoginActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);
    }

    public void registerUser(View view) {

        String password = Objects.requireNonNull(tfPassword.getEditText())
                .getText().toString().trim();

        String confirmPassword = Objects.requireNonNull(tfConfirm.getEditText())
                .getText().toString().trim();

        if (!isFieldEmpty()) {

            if (confirmPassword.equals(password)) {
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Fadlan buuxi meelaha banaan", Toast.LENGTH_SHORT).show();
        }

    }
}