package com.example.moviebooking.ui.app.moviepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebooking.R;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.dto.DateTime;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.example.moviebooking.ui.app.booking.BookingActivity;

import java.util.ArrayList;
import java.util.List;

public class MoviePageActivity extends AppCompatActivity {
    private Movie receivedMovie;
    private UserInfo userInfo = null;
    List<DateTime> dates = new ArrayList<>();
    private DateOfWeekAdapter dateOfWeekAdapter;
    List<DateTime> hours1 = new ArrayList<>();
    List<DateTime> hours2 = new ArrayList<>();
    private HoursAdapter hours1Adapter;
    private HoursAdapter hours2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);

        Intent intentHome = getIntent();
        receivedMovie = (Movie) intentHome.getSerializableExtra("movieIntent");
        if (receivedMovie == null) {
            return; // not found
        }
        userInfo = (UserInfo) intentHome.getSerializableExtra("userinfoIntent");
        Log.d("MoviePageActivity", "onCreate: " + receivedMovie.getTitle());

        setOnClickForFABButtonAndBackButton();
        bindDataToMovieInfo();
        bindDataToDateList();
        bindDataToHourList1List2();
    }

    private void setOnClickForFABButtonAndBackButton() {
        ImageView backButton = (ImageView) findViewById(R.id.iv_back_btn);
        TextView cinema1 = (TextView) findViewById(R.id.tv_cinema_1);
        TextView cinema2 = (TextView) findViewById(R.id.tv_cinema_2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        com.google.android.material.floatingactionbutton.FloatingActionButton
                fab = (com.google.android.material.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime selectedDate = dateOfWeekAdapter.getSelectedDate();
                DateTime selectedHour1 = hours1Adapter.getSelectedHour();
                DateTime selectedHour2 = hours2Adapter.getSelectedHour();

                if (selectedDate == null) {
                    Toast.makeText(MoviePageActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedHour1 == null && selectedHour2 == null) {
                    Toast.makeText(MoviePageActivity.this, "Please select hour", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedHour1 != null && selectedHour2 != null) {
                    Toast.makeText(MoviePageActivity.this, "Only 1 cinema per ticket", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MoviePageActivity.this, BookingActivity.class);
                intent.putExtra("userinfoIntent", userInfo);
                intent.putExtra("movie", receivedMovie);
                DateTime selectedHour;
                if (selectedHour1 != null) {
                    selectedHour = selectedHour1;
                    intent.putExtra("cinema", cinema1.getText());
                } else {
                    selectedHour = selectedHour2;
                    intent.putExtra("cinema", cinema2.getText());
                }
                intent.putExtra("datetime", selectedDate.setHoursFromDateTime(selectedHour));
                startActivity(intent);
            }
        });
    }

    private void bindDataToMovieInfo() {
        com.makeramen.roundedimageview.RoundedImageView
            movieImage = (com.makeramen.roundedimageview.RoundedImageView) findViewById(R.id.riv_movie_image);

        TextView movieTitle = (TextView) findViewById(R.id.tv_movie_title);
        TextView movieDescription = (TextView) findViewById(R.id.tv_movie_description);
        TextView movieDuration = (TextView) findViewById(R.id.tv_movie_duration);
        TextView movieRating = (TextView) findViewById(R.id.tv_movie_rate);
        TextView movieGenre = (TextView) findViewById(R.id.tv_movie_genre);

        Glide.with(this).load(receivedMovie.getThumbnail()).into(movieImage);
        movieTitle.setText(receivedMovie.getTitle());
        movieDescription.setText(receivedMovie.getDescription());
        movieDuration.setText(receivedMovie.getDuration() + " mins");
        movieRating.setText(receivedMovie.getRate());
        movieGenre.setText(receivedMovie.getMainGenre());
    }

    private void bindDataToDateList() {
        RecyclerView dateRCV = (RecyclerView) findViewById(R.id.rcv_dates);
        dates = HardcodingData.getNextDates();

        /*
        for (DateTime date : dates) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_calendar, null);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView tv_dayOfWeek = view.findViewById(R.id.tv_dayOfWeek);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView tv_day = view.findViewById(R.id.tv_day);

            tv_dayOfWeek.setText(date.getDayOfWeek());
            tv_day.setText(date.getDay());

            HorizontalScrollView.addView(view);
        }*/

        // set adapter for dates here
        dateOfWeekAdapter = new DateOfWeekAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        dateRCV.setLayoutManager(linearLayoutManager);

        dateOfWeekAdapter.setData(HardcodingData.getNextDates());

        Log.d("", ": " + dateOfWeekAdapter.getItemCount());

        dateRCV.setAdapter(dateOfWeekAdapter);
    }

    private void bindDataToHourList1List2() {
        RecyclerView hourRCV1 = (RecyclerView) findViewById(R.id.rcv_hours1);
        hours1 = HardcodingData.getHours();

        hours1Adapter = new HoursAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        hourRCV1.setLayoutManager(linearLayoutManager);
        hours1Adapter.setData(hours1);
        Log.d("", ": " + hours1Adapter.getItemCount());
        hourRCV1.setAdapter(hours1Adapter);

        //

        RecyclerView hourRCV2 = (RecyclerView) findViewById(R.id.rcv_hours2);
        hours2 = HardcodingData.getHours();

        hours2Adapter = new HoursAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        hourRCV2.setLayoutManager(linearLayoutManager);
        hours2Adapter.setData(hours2);
        Log.d("", ": " + hours2Adapter.getItemCount());
        hourRCV2.setAdapter(hours2Adapter);
    }

}
