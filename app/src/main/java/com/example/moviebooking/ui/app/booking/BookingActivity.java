package com.example.moviebooking.ui.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviebooking.R;
import com.example.moviebooking.dto.DateTime;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;

public class BookingActivity extends AppCompatActivity {
    private UserInfo userInfo = null;
    private Movie receivedMovie;
    private DateTime selectedDateTime;
    private String cinemaName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent moviePageIntent = getIntent();
        userInfo = (UserInfo) moviePageIntent.getSerializableExtra("userinfoIntent");
        receivedMovie = (Movie) moviePageIntent.getSerializableExtra("movie");
        selectedDateTime = (DateTime) moviePageIntent.getSerializableExtra("datetime");
        cinemaName = moviePageIntent.getStringExtra("cinema");

        if (receivedMovie == null || selectedDateTime == null) {
            return; // not found
        }

        setOnClickForFABButtonAndBackButton();
        setDataForThoseView();
    }

    private void setOnClickForFABButtonAndBackButton() {
        ImageView backButton = findViewById(R.id.iv_back_btn);
        backButton.setOnClickListener(v -> finish());

        ImageView fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingStatusActivity.class);
            intent.putExtra("movie", receivedMovie);
            intent.putExtra("datetime", selectedDateTime);
            intent.putExtra("cinema", cinemaName);
            startActivity(intent);
            finish();
        });
    }

    private void setDataForThoseView() {
        TextView ticketDate = findViewById(R.id.tv_date);
        TextView ticketTime = findViewById(R.id.tv_hours);
        TextView ticketCinema = findViewById(R.id.tv_cinema_name);

        ticketDate.setText(selectedDateTime.getShortDate());
        ticketTime.setText(selectedDateTime.getTimeAMPM());
        ticketCinema.setText(cinemaName);
    }
}
