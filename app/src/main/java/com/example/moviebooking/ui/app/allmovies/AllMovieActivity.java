package com.example.moviebooking.ui.app.allmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.data.FireBaseManager;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllMovieActivity extends AppCompatActivity {
    UserInfo userInfo = null;
    List<Movie> moviesList = null;
    RecyclerView allMoviesView;
    SearchView searchView;
    MovieGridAdapter movieGridAdapter;
    FireBaseManager firebaseManager = FireBaseManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);
        allMoviesView = findViewById(R.id.rcv_search_all_movies);
        searchView = findViewById(R.id.search_bar);
        searchView.clearFocus();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("userinfoIntent");

        setSeachViewListener();
        setDataForMoviesSlider(this);
    }

    private void setSeachViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Movie> filteredList = new ArrayList<>();
                for (Movie movie : moviesList) {
                    if (movie.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
                movieGridAdapter.setFilterList(filteredList);
                return true;
            }
        });
    }

    private void setDataForMoviesSlider(Context context) {
        firebaseManager.fetchAllMoviesData(new FireBaseManager.OnMoviesDataLoadedListener() {
            @Override
            public void onMoviesDataLoaded(List<Movie> movies) {
                moviesList = movies;
                Log.d("TAG", "allMovieList: " + moviesList.size());

                GridLayoutManager gridLayoutManager = new GridLayoutManager(AllMovieActivity.this, 3);
                allMoviesView.setLayoutManager(gridLayoutManager);

                movieGridAdapter = new MovieGridAdapter(context, userInfo, moviesList);
                allMoviesView.setAdapter(movieGridAdapter);
            }

            @Override
            public void onMoviesDataError(String errorMessage) {
                Log.d("TAG", errorMessage);
            }
        });
    }
}
