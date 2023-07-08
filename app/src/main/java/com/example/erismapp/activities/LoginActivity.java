package com.example.erismapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.erismapp.R;
import com.example.erismapp.models.UserModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private UserModel defaultUser;

    private TextInputLayout tfUsername, tfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        defaultUser = new UserModel(5566, "admin", "default user", "1234");

        tfUsername = findViewById(R.id.tf_username_layout);
        tfPassword = findViewById(R.id.tf_password_layout);
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

        if (username.equalsIgnoreCase(defaultUser.getUsername()) &&
                password.equals(defaultUser.getPassword())) {
            Intent mIntent = new Intent(LoginActivity.this, Dashboard.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
}