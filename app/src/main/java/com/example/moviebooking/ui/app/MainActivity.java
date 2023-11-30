package com.example.moviebooking.ui.app;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.moviebooking.R;
import com.example.moviebooking.ui.app.home.HomeActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);

        changeStatusBarColor(R.color.white);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void changeStatusBarColor(int colorResId) {
        // Check if the Android version is Lollipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Set the status bar color using the provided color resource ID
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorResId, getTheme()));
        }
    }
}
