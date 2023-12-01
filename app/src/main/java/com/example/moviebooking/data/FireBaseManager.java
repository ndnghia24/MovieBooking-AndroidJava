package com.example.moviebooking.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.moviebooking.dto.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseManager {
    private static FireBaseManager instance;
    private FirebaseDatabase database;
    private static List<Movie> nowShowing = null;
    private static List<Movie> comingSoon = null;

    private FireBaseManager() {
        // Private constructor to prevent instantiation outside of this class
        database = FirebaseDatabase.getInstance("https://moviebooking-65416-default-rtdb.asia-southeast1.firebasedatabase.app");
    }

    public static synchronized FireBaseManager getInstance() {
        if (instance == null) {
            instance = new FireBaseManager();
        }
        return instance;
    }

    public interface OnMoviesDataLoadedListener {
        void onMoviesDataLoaded(List<Movie> allMovieList);
        void onMoviesDataError(String errorMessage);
    }

    private void fetchMoviesData(String node, OnMoviesDataLoadedListener listener) {
        List<Movie> allMovieList = new ArrayList<>();

        DatabaseReference moviesReference = database.getReference(node);
        Log.d("MovieInfo", "Connect to firebase");

        moviesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allMovieList.clear();

                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    Movie movie = movieSnapshot.getValue(Movie.class);
                    if (movie != null) {
                        List<String> genres = new ArrayList<>();
                        movieSnapshot.child("genres").getChildren().forEach(genreSnapshot -> {
                            genres.add(genreSnapshot.getValue(String.class));
                        });
                        movie.setMovieID(movieSnapshot.getKey());
                        movie.setGenres(genres);

                        allMovieList.add(movie);
                    }
                }
                listener.onMoviesDataLoaded(allMovieList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onMoviesDataError("Lỗi: " + error.getMessage());
            }
        });
    }

    public void fetchAllMoviesData(OnMoviesDataLoadedListener listener) {
        fetchMoviesData("MOVIES", listener);
    }

    public void fetchNowShowingMoviesData(OnMoviesDataLoadedListener listener) {
        if (nowShowing != null && nowShowing.size() > 0) {
            listener.onMoviesDataLoaded(nowShowing);
        }
        fetchMoviesData("NOW_SHOWING", listener);
    }
}