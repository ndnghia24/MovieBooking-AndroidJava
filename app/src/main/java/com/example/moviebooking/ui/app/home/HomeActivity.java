package com.example.moviebooking.ui.app.home;

import com.example.moviebooking.R;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.example.moviebooking.ui.app.allmovies.AllMovieActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    UserInfo userInfo = null;
    List<Movie> moviesBarList = null;
    List<Movie> recommendedMoviesSlideList = null;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.moviebooking.R.layout.activity_home);

        setOnClickViewAll();
        setDataForMoviesBar();
        setDataForMoviesSlider();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedReferenceController.saveMoviesFromList(HomeActivity.this, HardcodingData.getAllMovies());
            }
        }).start();
    }

    private void setOnClickViewAll() {
        TextView viewAll = findViewById(com.example.moviebooking.R.id.viewAll);
        viewAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllMovieActivity.class);
            startActivity(intent);
        });
    }

    private void setDataForMoviesSlider() {
        recommendedMoviesSlideList = HardcodingData.getFewMovies();

        /*com.denzcoskun.imageslider.ImageSlider
                imageSlider = (com.denzcoskun.imageslider.ImageSlider) findViewById(com.example.moviebooking.R.id.image_slider);

        List<SlideModel> imageList = new ArrayList<>(); // set image resource from movie here

        for (Movie movie : recommendedMoviesSlideList) {
            imageList.add(new SlideModel(movie.getIdResource(), ScaleTypes.CENTER_INSIDE));
        }

        imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE);

        imageSlider.setItemClickListener(position -> {
            Intent intent = new Intent(HomeActivity.this, MoviePageActivity.class);
            intent.putExtra("movie", recommendedMoviesSlideList.get(position));
            startActivity(intent);
        });*/

        viewPager2 = findViewById(R.id.vp_images_slider);

        viewPager2.setAdapter(new MovieSliderAdapter(this, recommendedMoviesSlideList, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = 1 - Math.abs(position);
                page.setScaleY(0.85f + v * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private void setDataForMoviesBar() {
        RecyclerView moviesBarView = findViewById(R.id.rcv_all_movies);

        // set adapter for moviesBar here
        MovieScrollerAdapter moviesBarAdapter = new MovieScrollerAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        moviesBarView.setLayoutManager(linearLayoutManager);

        moviesBarAdapter.setData(getListMovies());

        Log.d("HomeActivity", "setDataForMoviesBar: " + moviesBarAdapter.getItemCount());

        moviesBarView.setAdapter(moviesBarAdapter);
    }

    private List<Movie> getListMovies() {
        moviesBarList = new ArrayList<>();

        moviesBarList = HardcodingData.getFewMovies();

        return moviesBarList;
    }
}