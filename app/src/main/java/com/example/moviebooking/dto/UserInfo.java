package com.example.moviebooking.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfo implements Serializable {
    private String username;
    private String password;
    private String name;
    private String email;

    private static final String wrongPassword = "WP";
    private static final String wrongID = "WI";
    private static final String loginSuccess = "LS";

    private UserInfo() {
    }
    public UserInfo(String name, String username, String password) {
        this.username = username;
        this.email = username;
        this.password = password;
        this.name = name;
    }

    public UserInfo(String UserInfoID, String password) {
        this.username = UserInfoID;
        this.password = password;
    }

    public String isValidLogin(String UserInfoID, String password) {
        if (!this.username.equals(UserInfoID)) {
            return wrongID;
        }
        if (!this.password.equals(password)) {
            return wrongPassword;
        }
        return loginSuccess;
    }

    public void parseDataFromString(ArrayList<String> data) {
        this.username = data.get(0);
        this.password = data.get(1);
        this.name = data.get(2);
        this.email = data.get(3);
    }
}