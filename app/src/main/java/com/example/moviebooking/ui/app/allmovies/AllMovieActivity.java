package com.example.moviebooking.ui.app.allmovies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.Movie;

import java.util.List;

public class AllMovieActivity extends AppCompatActivity {
    List<Movie> allMovieList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        setDataForMoviesSlider();
    }

    private void setDataForMoviesSlider() {
        allMovieList = SharedReferenceController.getListMovies(this);

        // convert to grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        RecyclerView allMoviesView = findViewById(R.id.rcv_search_all_movies);
        allMoviesView.setLayoutManager(gridLayoutManager);

        // set adapter
        allMoviesView.setAdapter(new MovieGridAdapter(this, allMovieList));
    }
}
