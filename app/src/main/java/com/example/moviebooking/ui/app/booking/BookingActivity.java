package com.example.moviebooking.ui.app.booking;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
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
import com.example.moviebooking.dto.Seat;
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
    private GridLayout gridSeatsView;
    private static ImageView[][] seatsImageViews;
    private static Seat[][] seatStatus;

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

        seatStatus = new Seat[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                seatStatus[i][j] = new Seat((char) (i + 'A')+ "" + j, false, false);
            }
        }

        setOnClickForFABButtonAndBackButton();
        setDataForFilmView();
        setDataToSeatsGrid();
    }

    private void setDataToSeatsGrid() {
        gridSeatsView = findViewById(R.id.grid_seats);
        gridSeatsView.removeAllViews();

        int totalColumns = seatStatus[0].length;
        int totalRows = seatStatus.length;

        gridSeatsView.setColumnCount(totalColumns);
        gridSeatsView.setRowCount(totalRows);

        gridSeatsView.post(new Runnable() {
            @Override
            public void run() {
                int imageViewSize = gridSeatsView.getWidth() / totalColumns;
                Log.d("imageViewSize", gridSeatsView.getWidth() + " " + imageViewSize + " " + totalColumns);

                for (int i = 0; i < totalRows; i++) {
                    for (int j = 0; j < totalColumns; j++) {
                        ImageView seatImageView = new ImageView(BookingActivity.this);
                        seatImageView.setPadding(10, 10, 10, 10);
                        seatImageView.setBackgroundResource(R.drawable.button_seats_unselected);
                        seatImageView.setTag(seatStatus[i][j].getSeatId());

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = imageViewSize;
                        params.height = imageViewSize;
                        GridLayout.Spec rowSpec = GridLayout.spec(i);
                        GridLayout.Spec colSpec = GridLayout.spec(j);
                        params.rowSpec = rowSpec;
                        params.columnSpec = colSpec;

                        seatImageView.setLayoutParams(params);

                        seatImageView.setOnClickListener(v -> {
                            ImageView seat = (ImageView) v;
                            String seatId = (String) seat.getTag();
                            int row = seatId.charAt(0) - 'A';
                            int col = seatId.charAt(1) - '0';
                            if (seatStatus[row][col].isBooked()) {
                                return;
                            }
                            if (seatStatus[row][col].isSelected()) {
                                seat.setBackgroundResource(R.drawable.button_seats_selected);
                                seatStatus[row][col].setSelected(false);
                            } else {
                                seat.setBackgroundResource(R.drawable.button_seats_unselected);
                                seatStatus[row][col].setSelected(true);
                            }
                        });

                        gridSeatsView.addView(seatImageView);
                    }
                }
            }
        });
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
