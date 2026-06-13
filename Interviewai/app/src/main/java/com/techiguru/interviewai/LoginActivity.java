package com.techiguru.interviewai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister, tvForgotPassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        // Auto Login
        if (auth.getCurrentUser() != null) {

            startActivity(
                    new Intent(LoginActivity.this,
                            MainActivity.class));

            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {

                etEmail.setError("Enter email");
                return;
            }

            if (password.isEmpty()) {

                etPassword.setError("Enter password");
                return;
            }

            btnLogin.setEnabled(false);

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        btnLogin.setEnabled(true);

                        if (task.isSuccessful()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                            ).show();

                            startActivity(
                                    new Intent(
                                            LoginActivity.this,
                                            MainActivity.class));

                            finish();

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    });

        });

        tvRegister.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class));

        });

        tvForgotPassword.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            ForgotPasswordActivity.class));

        });

    }
}