package com.example.moviebooking.ui.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebooking.R;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.dto.DateTime;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.example.moviebooking.ui.app.allmovies.AllMovieActivity;
import com.example.moviebooking.ui.app.allmovies.MovieGridAdapter;
import com.example.moviebooking.ui.app.moviepage.DateOfWeekAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private UserInfo userInfo = null;
    private Movie receivedMovie;
    private DateTime selectedDateTime;
    private String cinemaName;
    private RecyclerView gridSeatsView;
    private SeatsGridAdapter seatsGridAdapter;

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
        setDataForFilmView();
        setDataToSeatsGrid();
    }

    private void setDataToSeatsGrid() {
        RecyclerView gridSeatsView =  findViewById(R.id.grid_seats);

        // set adapter for dates here
        SeatsGridAdapter seatsAdapter = new SeatsGridAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(BookingActivity.this, 10);
        gridSeatsView.setLayoutManager(gridLayoutManager);

        Log.d("", ": " + seatsAdapter.getItemCount());

        gridSeatsView.setAdapter(seatsAdapter);

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

    private void setDataForFilmView() {
        com.makeramen.roundedimageview.RoundedImageView
                movieImage = (com.makeramen.roundedimageview.RoundedImageView) findViewById(R.id.riv_movie_image);

        TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        TextView ticketDate = findViewById(R.id.tv_date);
        TextView ticketTime = findViewById(R.id.tv_hours);
        TextView ticketCinema = findViewById(R.id.tv_cinema_name);

        Glide.with(this).load(receivedMovie.getThumbnail()).into(movieImage);
        movieTitle.setText(receivedMovie.getTitle());
        ticketDate.setText(selectedDateTime.getShortDate());
        ticketTime.setText(selectedDateTime.getTimeAMPM());
        ticketCinema.setText(cinemaName);

    }
}
