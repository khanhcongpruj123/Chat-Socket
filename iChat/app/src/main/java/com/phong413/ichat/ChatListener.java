package com.phong413.ichat;

import com.messages.Message;

import java.util.List;

public interface ChatListener {
    void onUpdate(List<Message> listMessage);
}
