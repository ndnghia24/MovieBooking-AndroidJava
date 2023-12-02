package com.example.moviebooking.dto;

public class Seat {
    private String seatId;
    private boolean isBooked = false;
    private boolean isSelected = false;

    public Seat(String seatId, boolean isBooked, boolean isChosen) {
        this.seatId = seatId;
        this.isBooked = isBooked;
        this.isSelected = isChosen;
    }

    public Seat() {
    }

    public String getSeatId() {
        return seatId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
