package com.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message implements Serializable {

    public static final long serialVersionUID = 1L;

   private MessageType type = MessageType.USER;
   private Status status = Status.ONLINE;

   private User user;
   private String message;
   private String imageUrl;
   private String voiceUrl;

   private ArrayList<User> listUser;
    private List<Message> listMess;

    public Message(MessageType type, Status status, User user, String message, String imageUrl, String voiceUrl, ArrayList<User> listUser) {
        this.type = type;
        this.status = status;
        this.user = user;
        this.message = message;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
        this.listUser = listUser;
    }

    public Message() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public ArrayList<User> getListUser() {
        return listUser;
    }

    public void setListUser(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    public List<Message> getListMess() {
        return listMess;
    }

    public void setListMess(List<Message> listMess) {
        this.listMess = listMess;
    }
}
