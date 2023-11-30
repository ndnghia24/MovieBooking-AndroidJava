package com.example.moviebooking.ui.app.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.moviebooking.R;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.ui.app.moviepage.MoviePageActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MovieSliderAdapter extends RecyclerView.Adapter<MovieSliderAdapter.SliderViewHolder> {
    private Context mContext;
    private List<Movie> mListMovie;
    private ViewPager2 viewPager2;

    public MovieSliderAdapter(Context context, List<Movie> Movies, ViewPager2 viewPager2) {
        this.mContext = context;
        this.mListMovie = Movies;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_container, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Movie movie = mListMovie.get(position);
        if (movie == null) {
            return;
        }
        holder.setImage(movie);
        if (position == mListMovie.size() - 2) {
            viewPager2.post(sliderRunnable);
        }
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MoviePageActivity.class);
            intent.putExtra("movie", movie);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mListMovie.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }
        private void setImage(Movie movie) {
            Glide.with(mContext).load(movie.getThumbnailUrl()).override(270, 410).into(imageView);
        }
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            mListMovie.addAll(mListMovie);
            notifyDataSetChanged();
        }
    };
}
