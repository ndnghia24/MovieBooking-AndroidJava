package com.example.moviebooking.ui.app.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.moviebooking.R;
import com.example.moviebooking.data.FireBaseManager;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.example.moviebooking.ui.app.allmovies.AllMovieActivity;
import com.example.moviebooking.ui.app.booking.BookingHistoryActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int SLIDER_DELAY_MS = 3000;

    private UserInfo userInfo;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    private FireBaseManager firebaseManager = FireBaseManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUserInfo();
        setOnClickViewAll();
        setDataForMoviesBar(this);
        setDataForMoviesSlider(this);
    }

    private void setupUserInfo() {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userinfoIntent");
        if (userInfo == null) {
            return;
        }
        Log.d("HomeActivity", "onCreate: " + userInfo.getUsername());
    }

    private void setOnClickViewAll() {
        TextView viewAll = findViewById(R.id.viewAll);
        viewAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllMovieActivity.class);
            intent.putExtra("userinfoIntent", userInfo);
            startActivity(intent);
        });

        ImageView userAva = findViewById(R.id.imgUser);
        userAva.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BookingHistoryActivity.class);
            intent.putExtra("userinfoIntent", userInfo);
            startActivity(intent);
        });
    }

    private void setDataForMoviesSlider(Context context) {
        firebaseManager.fetchNowShowingMoviesData(new FireBaseManager.OnMoviesDataLoadedListener() {
            @Override
            public void onMoviesDataLoaded(List<Movie> nowShowingMoviesList) {
                initializeMoviesSlider(context, nowShowingMoviesList);
            }

            @Override
            public void onMoviesDataError(String errorMessage) {
                Log.d("HomeActivity", "onMoviesDataError: " + errorMessage);
            }
        });
    }

    private void initializeMoviesSlider(Context context, List<Movie> nowShowingMoviesList) {
        viewPager2 = findViewById(R.id.vp_images_slider);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setOffscreenPageLimit(4);
        viewPager2.setPageTransformer(new SliderTransformer(viewPager2));
        viewPager2.setAdapter(new MovieSliderAdapter(context, userInfo, nowShowingMoviesList, viewPager2));
        //configureViewPager();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, SLIDER_DELAY_MS);
            }
        });
    }

    private Runnable sliderRunnable = () -> viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

    private void setDataForMoviesBar(Context context) {
        firebaseManager.fetchNowShowingMoviesData(new FireBaseManager.OnMoviesDataLoadedListener() {
            @Override
            public void onMoviesDataLoaded(List<Movie> nowShowingMoviesList) {
                initializeMoviesBar(context, nowShowingMoviesList);
            }

            @Override
            public void onMoviesDataError(String errorMessage) {
                Log.d("HomeActivity", "onMoviesDataError: " + errorMessage);
            }
        });
    }

    private void initializeMoviesBar(Context context, List<Movie> nowShowingMoviesList) {
        RecyclerView moviesBarView = findViewById(R.id.rcv_all_movies);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        moviesBarView.setLayoutManager(linearLayoutManager);

        moviesBarView.setAdapter(new MovieScrollerAdapter(context, userInfo, nowShowingMoviesList));
    }
}
