package com.example.moviebooking.ui.login_logout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moviebooking.R;
import com.example.moviebooking.data.FireBaseManager;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.ui.app.home.HomeActivity;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    FireBaseManager firebaseManager = FireBaseManager.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FireBaseManager fireBaseManager = FireBaseManager.getInstance();
        setContentView(com.example.moviebooking.R.layout.activity_login);

        usernameEditText = findViewById(com.example.moviebooking.R.id.et_username);
        passwordEditText = findViewById(com.example.moviebooking.R.id.et_password);
        registerTextView = findViewById(com.example.moviebooking.R.id.tv_next_to_register);
        loginButton= findViewById(R.id.btn_login);

        setOnCLickListenerForThoseViews();
        preLoadData();
    }

    private void preLoadData() {
        firebaseManager.fetchNowShowingMoviesData(new FireBaseManager.OnMoviesDataLoadedListener() {
            @Override
            public void onMoviesDataLoaded(List<Movie> NowShowingMoviesList) {
            }

            @Override
            public void onMoviesDataError(String errorMessage) {
            }
        });
    }

    private void setOnCLickListenerForThoseViews() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (SharedReferenceController.loginUser(this, username, password)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
