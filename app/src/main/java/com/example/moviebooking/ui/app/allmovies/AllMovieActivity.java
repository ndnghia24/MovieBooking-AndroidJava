package com.example.moviebooking.ui.app.allmovies;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.data.HardcodingData;
import com.example.moviebooking.data.SharedReferenceController;
import com.example.moviebooking.dto.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllMovieActivity extends AppCompatActivity {
    DatabaseReference moviesReference;
    List<Movie> allMovieList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        moviesReference = database.getReference("MOVIES");
        moviesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allMovieList.clear();

                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    Movie movie = movieSnapshot.getValue(Movie.class);
                    if (movie != null) {
                        allMovieList.add(movie);
                        Log.d("MovieInfo", "Title: " + movie.getTitle());
                    } else {
                        Log.d("MovieInfo", "Movie is null");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Lỗi: " + error.getMessage());
            }
        });

        setDataForMoviesSlider();
    }

    private void setDataForMoviesSlider() {
        //allMovieList = SharedReferenceController.getListMovies(this);

        // convert to grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        RecyclerView allMoviesView = findViewById(R.id.rcv_search_all_movies);
        allMoviesView.setLayoutManager(gridLayoutManager);

        // set adapter
        allMoviesView.setAdapter(new MovieGridAdapter(this, allMovieList));
    }
}
