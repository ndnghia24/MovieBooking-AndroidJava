package com.example.moviebooking.dto;

import java.io.Serializable;

public class DateTime implements Serializable {
    String dayOfWeek;
    int day;
    int month;
    int year;
    int hour;
    int minute;

    boolean isSelected = false;
    boolean isDisable = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public String getDay() {
        if (day < 10) {
            return "0" + day;
        }
        return "" + day;
    }

    public String getMonth() {
        if (month < 10) {
            return "0" + month;
        }
        return "" + month;
    }

    public String getYear() {
        return "" + year;
    }

    public String getHour() {
        if (hour < 10) {
            return "0" + hour;
        }
        return "" + hour;
    }

    public String getMinute() {
        if (minute < 10) {
            return "0" + minute;
        }
        return "" + minute;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTimeAMPM() {
        if (hour < 12) {
            return getHour() + ":" + getMinute() + " AM";
        }
        return getHour() + ":" + getMinute() + " PM";
    }

    public DateTime(String dayOfWeek, int day, int month, int year, int hour, int minute) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public String toString() {
        return dayOfWeek + "-" + day + "/" + month + "/" + year + "-" + hour + ":" + minute;
    }
}
