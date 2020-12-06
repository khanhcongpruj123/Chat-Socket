package com.phong413.ichat;

import android.graphics.Bitmap;
import android.util.Log;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatThread extends Thread {

    public final static String HAS_CONNECTED = "Đã kết nối";

    private Socket socket;
    private String host;
    private int port;
    private OutputStream outputStream;
    private ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;

    private User user;

    private ArrayList<ChatListener> listeners = new ArrayList<>();
    private ArrayList<Message> listMessage;

    private Account account;

    public static ChatThread instance;

    public static void initialize(String host, int port, Account a) {
        instance = new ChatThread(host, port, a);
    }

    public Account getAccount() {
        return account;
    }

    public synchronized static ChatThread getInstance() {
        return instance;
    }

    public String getHost() {
        return host;
    }

    private List<User> listContact;

    public List<User> getListContact() {
        return listContact;
    }

    public ArrayList<Message> getListMessage() {
        return listMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private ChatThread(String host, int port, Account a) {
        this.host = host;
        this.port = port;
        this.account = a;
        user = new User();
        user.setAvatarUrl(a.getAvatarUrl());
        user.setUsername(a.getUsername());

        listMessage = new ArrayList<>();
    }


//    public void setChatUpdateListener(ChatListener listener) {
//        this.chatListener = listener;
//    }

    public void addChatUpdateListener(ChatListener listener) {
        this.listeners.add(listener);
    }


    ChatConnectListener chatConnectListener;

    public void connect(ChatConnectListener listener) {
        chatConnectListener = listener;
        start();
    }

    synchronized void sendMessage(String mess) {
        try {
            Message message = new Message();
            message.setStatus(Status.ONLINE);
            message.setType(MessageType.USER);
            message.setUser(user);
            message.setMessage(mess);
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void sendMessage(Message message) {
        try {
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            Log.d("AppLog", "Dang ket noi");
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            Log.d("AppLog", "Socket da ket noi!");

            sendMessage(
                    new MessageBuilder()
                            .setMessageType(MessageType.USER)
                            .setStatus(Status.ONLINE)
                            .build()
            );

            while (socket.isConnected()) {
                Message mess = (Message) input.readObject();
                if (mess != null) {
                    switch (mess.getType()) {
                        case DISCONNECTED:
                        case IMAGE:
                        case VOICE:
                        case USER:
                        case NOTIFICATION:
                        case SERVER:
                            listContact = mess.getListUser();
                            listMessage.add(mess);
                            callback(listMessage);
                            break;
                        case CONNECTED:
                            listContact = mess.getListUser();
                            if (mess.getListMess() != null) listMessage.addAll(0, mess.getListMess());
                            listMessage.add(mess);
                            callback(listMessage);
                            chatConnectListener.onConnected();
                            Log.d("AppLog", "Chat da ket noi!");
                            break;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("AppLog", "Co loi xay ra!");
        }
    }

    public void callback(List<Message> listMessage) {
        Log.d("AppLog","Cap nhat tin nhan va danh sach contact");
        listeners.forEach(listener -> {
            listener.onUpdate(listMessage);
        });
    }

    public void disconnect() {
        try {
            if (socket != null) {
                outputStream.close();
                is.close();
                oos.close();
                input.close();
                socket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
