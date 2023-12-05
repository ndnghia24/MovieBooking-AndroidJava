package com.example.moviebooking.ui.app.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviebooking.data.FireBaseManager;
import com.example.moviebooking.dto.BookedTicketList;
import com.example.moviebooking.dto.DateTime;
import com.example.moviebooking.dto.Movie;
import com.example.moviebooking.dto.Seat;
import com.example.moviebooking.dto.Ticket;
import com.example.moviebooking.dto.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class BookingStatusActivity extends AppCompatActivity {
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.moviebooking.R.layout.activity_booking_status);

        Intent intent = getIntent();
        BookedTicketList bookedTicketList = (BookedTicketList) intent.getSerializableExtra("bookedTicketList");
        if (bookedTicketList == null) {
            return;
        }
        Movie movie = intent.getSerializableExtra("movie") != null ? (Movie) intent.getSerializableExtra("movie") : null;

        createTicketsCard(bookedTicketList, movie);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        findViewById(com.example.moviebooking.R.id.icon_home).setOnClickListener(v -> {
            finish();
        });

        findViewById(com.example.moviebooking.R.id.tv_history).setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingHistoryActivity.class);
            intent.putExtra("userinfoIntent", (UserInfo) getIntent().getSerializableExtra("userinfoIntent"));
            startActivity(intent);
        });
    }

    private void createTicketsCard(BookedTicketList bookedTicketList, Movie movie) {
        List<Ticket> ticketList = bookedTicketList.getBookedTicketList();

        ImageView movieImg = findViewById(com.example.moviebooking.R.id.movie_img);
        TextView movie_name = findViewById(com.example.moviebooking.R.id.movie_name);
        TextView ticket_cinema = findViewById(com.example.moviebooking.R.id.ticket_cinema);
        TextView ticket_date_value = findViewById(com.example.moviebooking.R.id.ticket_date_value);
        TextView ticket_hour_value = findViewById(com.example.moviebooking.R.id.ticket_hour_value);
        TextView ticket_seat_value = findViewById(com.example.moviebooking.R.id.ticket_seat_value);
        TextView booking_code_value = findViewById(com.example.moviebooking.R.id.booking_code_value);
        ImageView qr_code = findViewById(com.example.moviebooking.R.id.qr_code);

        Glide.with(this).load(movie.getThumbnail()).into(movieImg);
        movie_name.setText(movie.getTitle());
        ticket_cinema.setText(bookedTicketList.getCinemaName());
        ticket_date_value.setText(bookedTicketList.getDate());
        if (bookedTicketList.countTicket() > 3) {
            ticket_date_value.setTextSize(12);
        }
        ticket_hour_value.setText(bookedTicketList.getHour());

        String seatList = "";
        for (Ticket ticket : ticketList) {
            seatList += ticket.getSeatId();
            if (ticketList.indexOf(ticket) != ticketList.size() - 1) {
                seatList += ", ";
            }
        }
        ticket_seat_value.setText(seatList);
        booking_code_value.setText(bookedTicketList.getTicketID());
        qr_code.setImageBitmap(bookedTicketList.getQrCode(bookedTicketList.getTicketID()));
    }
}
