package com.example.moviebooking.ui.app.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebooking.R;
import com.example.moviebooking.dto.DateTime;
import com.example.moviebooking.dto.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatsGridAdapter extends RecyclerView.Adapter<SeatsGridAdapter.SeatViewHolder> {
    private Context mContext;
    private List<Seat> seatsList;
    public SeatsGridAdapter(Context mContext) {
        this.mContext = mContext;
        seatsList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 1; j <= 10; j++) {
                seatsList.add(new Seat((char) (i + 65) + "" + j, false, false));
            }
        }
    }
    public void setSeatsBooked(List<String> bookedList) {
        for (int i = 0; i < seatsList.size(); i++) {
            if (bookedList.contains(seatsList.get(i))) {
                seatsList.get(i).setBooked(true);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seats, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seats = seatsList.get(position);

        Log.d("SeatsGridAdapter", "onBindViewHolder: " + seats.getSeatId());

        if (seats == null) {
            return;
        }

        if (seats.isBooked()) {
            holder.seatsLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_seats_disabled));
        } else if (seats.isChosen()) {
            holder.seatsLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_seats_selected));
        } else {
            holder.seatsLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_seats_unselected));
        }

        holder.seatsLayout.setOnClickListener(v -> {
            if (seats.isBooked()) {
                return;
            }
            if (seats.isChosen()) {
                holder.seatsLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_seats_selected));
                seats.setChosen(false);
                notifyDataSetChanged();
            }
            else {
                holder.seatsLayout.setBackground(mContext.getResources().getDrawable(R.drawable.button_seats_unselected));
                seats.setChosen(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (seatsList != null) {
            return seatsList.size();
        }
        return 0;
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout seatsLayout;
        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatsLayout = itemView.findViewById(R.id.seat_layout);
        }
    }
}
