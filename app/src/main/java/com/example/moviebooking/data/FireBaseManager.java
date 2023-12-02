package com.example.moviebooking.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FireBaseManager {
    private static FireBaseManager instance;
    private static FirebaseDatabase firebaseDatabase;
    private static List<Movie> nowShowing = null;
    private static List<Movie> comingSoon = null;
    private static List<Movie> allMovies = null;
    
    private static final String USERS_TABLE = "USERS_INFO";

    private FireBaseManager() {
        // Private constructor to prevent instantiation outside of this class
        firebaseDatabase = FirebaseDatabase.getInstance("https://moviebooking-65416-default-rtdb.asia-southeast1.firebasedatabase.app");
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

        DatabaseReference moviesReference = firebaseDatabase.getReference(node);
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
        if (allMovies != null && allMovies.size() > 0) {
            listener.onMoviesDataLoaded(allMovies);
        }
        fetchMoviesData("MOVIES", listener);
    }
    public void fetchNowShowingMoviesData(OnMoviesDataLoadedListener listener) {
        if (nowShowing != null && nowShowing.size() > 0) {
            listener.onMoviesDataLoaded(nowShowing);
        }
        fetchMoviesData("NOW_SHOWING", listener);
    }

    public static void registerUser(Context context, String name, String username, String password, String confirmPassword, RegistrationCallback callback) {
        DatabaseReference usersReference = firebaseDatabase.getReference(USERS_TABLE);

        if (username.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            callback.onRegistrationResult(false, "Please fill in all fields", null);
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "Password and confirm password must be the same", Toast.LENGTH_SHORT).show();
            callback.onRegistrationResult(false, "Password and confirm password must be the same", null);
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            callback.onRegistrationResult(false, "Password must be at least 6 characters", null);
            return;
        }

        usersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show();
                    callback.onRegistrationResult(false, "Username already exists", null);
                } else {
                    usersReference.child(username).setValue(password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // add user info to fire base
                                    DatabaseReference userInfoReference = firebaseDatabase.getReference(USERS_TABLE);
                                    userInfoReference.child(username).child("name").setValue(name);
                                    userInfoReference.child(username).child("username").setValue(username);
                                    userInfoReference.child(username).child("password").setValue(password);

                                    callback.onRegistrationResult(true, "Register successfully", null);
                                    Log.d("TAG", "Data added successfully.");
                                } else {
                                    Log.e("TAG", "Error: " + task.getException().getMessage());
                                    callback.onRegistrationResult(false, "Error: " + task.getException().getMessage(), null);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error: " + databaseError.getMessage());
                callback.onRegistrationResult(false, "Error: " + databaseError.getMessage(), null);
            }
        });
    }

    public interface RegistrationCallback {
        void onRegistrationResult(boolean isSuccess, String message, Object data);
    }

    public static void loginUser(Context context, String username, String password, RegistrationCallback callback) {
        DatabaseReference usersReference = firebaseDatabase.getReference(USERS_TABLE);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            callback.onRegistrationResult(false, "Please fill in all fields", null);
            return;
        }

        usersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passwordFromDB = dataSnapshot.child("password").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String user = dataSnapshot.child("username").getValue(String.class);

                    if (passwordFromDB.equals(password)) {
                        Log.d("TAG", "Login successfully");
                        callback.onRegistrationResult(true, "Login successfully", new UserInfo(name, user, passwordFromDB));
                    } else {
                        Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                        callback.onRegistrationResult(false, "Wrong password", null);
                    }
                } else {
                    Toast.makeText(context, "Username does not exist", Toast.LENGTH_SHORT).show();
                    callback.onRegistrationResult(false, "Username does not exist", null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error: " + databaseError.getMessage());
                callback.onRegistrationResult(false, "Error: " + databaseError.getMessage(), null);
            }
        });
    }
}