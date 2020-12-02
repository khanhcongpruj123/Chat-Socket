package com.phong413.ichat;

import com.messages.Message;

import java.util.List;
import java.util.concurrent.Executors;

public class MessageController implements ChatListener {

    private MessageView messageView;

    public MessageController(MessageView messageView) {
        this.messageView = messageView;
        ChatThread.getInstance().setChatUpdateListener(this);
    }

    public void sendMessage(Message mess) {
        if (mess != null) {
            Executors.newSingleThreadExecutor().submit(() -> {
                ChatThread.getInstance().sendMessage(mess);
            });
        }
    }

    @Override
    public void onUpdate(List<Message> listMessage) {
        messageView.updateMessage(listMessage);
    }
}
