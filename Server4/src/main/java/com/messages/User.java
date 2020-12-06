package com.messages;

import java.io.Serializable;

/**
 * Created by Dominic on 01-May-16.
 */
public class User implements Serializable {

    public static final long serialVersionUID = 2L;

    private String username;
    private Status status = Status.ONLINE;
    private String avatarUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
