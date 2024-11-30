package com.example.vindme.activity.profile;

public class UserProfile {
    private String username;
    private String email;

    public UserProfile() {}

    public UserProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}