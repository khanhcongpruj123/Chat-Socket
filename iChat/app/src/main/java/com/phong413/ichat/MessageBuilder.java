package com.phong413.ichat;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;

public class MessageBuilder {

    private String message;
    private String imgUrl;
    private MessageType type;
    private Status status = Status.ONLINE;

    MessageBuilder setMessage(String mess) {
        this.message = mess;
        return this;
    }

    MessageBuilder setImageUrl(String img) {
        this.imgUrl = img;
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
        mess.setUser(ChatThread.getInstance().getUser());
        mess.setMessage(this.message);
        mess.setType(this.type);
        mess.setStatus(this.status);
        mess.setImageUrl(this.imgUrl);
        return mess;
    }
}
