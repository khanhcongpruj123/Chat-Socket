package com.phong413.ichat;

import com.messages.Message;
import com.messages.User;

import java.util.List;

public class ContactController implements ChatListener{

    private ContactView contactView;

    public ContactController(ContactView contactView) {
        this.contactView = contactView;
        ChatThread.getInstance().addChatUpdateListener(this);
    }

    @Override
    public void onUpdate(List<Message> listMessage) {

        if (listMessage != null && !listMessage.isEmpty()) {
            Message mess = listMessage.get(listMessage.size() - 1);
            updateListContact(mess.getUserlist());
        }
    }

    public void updateListContact(List<User> listContact) {
        contactView.updateListContact(listContact);
    }
}
