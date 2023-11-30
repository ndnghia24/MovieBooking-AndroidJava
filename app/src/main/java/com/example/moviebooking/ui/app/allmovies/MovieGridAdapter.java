package com.example.moviebooking.ui.app.allmovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviebooking.R;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.ui.app.moviepage.MoviePageActivity;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {
    private Context mContext;
    private List<Movie> mListMovie;
    public MovieGridAdapter(Context mContext, List<Movie> mListMovie) {
        this.mContext = mContext;
        this.mListMovie = mListMovie;
    }

    public void setData(List<Movie> list) {
        this.mListMovie = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_movies, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mListMovie.get(position);
        if (movie == null) {
            return;
        }

        // set image resource by picasso and resize image to 27x41
        Glide.with(mContext).load(movie.getThumbnailUrl()).override(270, 410).into(holder.imgMovie);
        holder.tvTitle.setText(movie.getTitle());

        // String: min to hour min
        holder.tvNote.setText(movie.getDetailDuration());

        holder.imgMovie.setOnClickListener(v -> {
            // start new intent
            Intent intent = new Intent(this.mContext, MoviePageActivity.class);
            intent.putExtra("movie", movie);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mListMovie != null) {
            return mListMovie.size();
        }
        return 0;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMovie;
        private TextView tvTitle;
        private TextView tvNote;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.image_movie);
            tvTitle = itemView.findViewById(R.id.title);
            tvNote = itemView.findViewById(R.id.note_text);
        }
    }
}
