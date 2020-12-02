package com.phong413.ichat;

import android.graphics.Bitmap;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;

public class MessageBuilder {

    private String message;
    private byte[] imgMsg;
    private MessageType type;
    private Status status = Status.ONLINE;

    MessageBuilder setMessage(String mess) {
        this.message = mess;
        return this;
    }

    MessageBuilder setImageMessage(byte[] img) {
        this.imgMsg = img;
        return this;
    }

    MessageBuilder setMessageType(MessageType type) {
        this.type = type;
        return this;
    }

    MessageBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    Message build() {
        Message mess = new Message();
        mess.setName(ChatThread.getInstance().getUsername());
        mess.setPicture(ChatThread.getInstance().getAvatar());
        mess.setMsg(this.message);
        mess.setType(this.type);
        mess.setStatus(this.status);
        mess.setImgMsg(this.imgMsg);
        return mess;
    }
}
