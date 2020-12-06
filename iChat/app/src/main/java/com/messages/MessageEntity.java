package com.messages;

import java.io.Serializable;
import java.util.List;

public class MessageEntity implements Serializable {

    public static final long serialVersionUID = 3L;

    private int id;
    private String imageUrl;
    private long time;
    private String username;
    private String voiceUrl;
    private String msg;

    public MessageEntity() {
    }

    public MessageEntity(int id, String imageUrl, long time, String username, String voiceUrl, String msg) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.time = time;
        this.username = username;
        this.voiceUrl = voiceUrl;
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
