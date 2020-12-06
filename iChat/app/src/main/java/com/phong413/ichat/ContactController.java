package com.phong413.ichat;

import android.util.Log;

import com.messages.Message;
import com.messages.User;

import java.util.List;
import java.util.Timer;

public class ContactController implements ChatListener{

    private ContactView contactView;

    public ContactController(ContactView contactView) {
        this.contactView = contactView;
        ChatThread.getInstance().addChatUpdateListener(this);
    }

    @Override
    public void onUpdate(List<Message> listMessage) {
        Log.d("AppLog", "Cap nhat danh sach contact");
        if (listMessage != null && !listMessage.isEmpty()) {
            Message mess = listMessage.get(listMessage.size() - 1);
            Log.d("AppLog", "Danh sach contact: " + mess.getListUser().size());
            updateListContact(mess.getListUser());
        }
    }

    public void updateListContact(List<User> listContact) {
        contactView.updateListContact(listContact);
    }
}
