package com.phong413.ichat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Account implements Serializable  {

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("avatar_url")
    private String avatarUrl;

    public Account(String username, String password, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
