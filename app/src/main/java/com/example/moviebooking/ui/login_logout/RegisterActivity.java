package com.example.moviebooking.ui.login_logout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviebooking.R;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.UserInfo;
import com.example.moviebooking.ui.app.home.HomeActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText customerNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private TextView loginTextView;
    private Button registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.moviebooking.R.layout.activity_register);

        customerNameEditText = findViewById(R.id.et_customer_name);
        emailEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        loginTextView = findViewById(R.id.tv_next_to_login);
        registerButton = findViewById(R.id.btn_register);

        setOnCLickListenerForThoseViews();
    }

    private void setOnCLickListenerForThoseViews() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void registerUser() {
        String name = customerNameEditText.getText().toString();
        String username = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (SharedReferenceController.registerUser(this, name, username, password, confirmPassword)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("userinfo", new UserInfo(name, username, password));
            startActivity(intent);
        }
    }
}
