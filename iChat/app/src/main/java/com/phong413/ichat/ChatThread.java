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

    private String username;
    private byte[] avatar;

    private ArrayList<ChatListener> listeners = new ArrayList<>();
    private ArrayList<Message> listMessage;

    public static ChatThread instance;

    public static void initialize(String host, int port) {
        instance = new ChatThread(host, port);
    }

    public static ChatThread getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    private List<User> listContact;

    public List<User> getListContact() {
        return listContact;
    }

    public ArrayList<Message> getListMessage() {
        return listMessage;
    }

    private ChatThread(String host, int port) {
        this.host = host;
        this.port = port;

        listMessage = new ArrayList<>();
    }

    public void connect(String username, byte[] avatar) {
        this.username = username;
        this.avatar = avatar;

        start();
    }

//    public void setChatUpdateListener(ChatListener listener) {
//        this.chatListener = listener;
//    }

    public void addChatUpdateListener(ChatListener listener) {
        this.listeners.add(listener);
    }

    private void connect() {
        try {
            Message connectedMessage = new Message();
            connectedMessage.setName(username);
            connectedMessage.setPicture(avatar);
            connectedMessage.setType(MessageType.CONNECTED);
            connectedMessage.setMsg(username + " " + HAS_CONNECTED);
            connectedMessage.setStatus(Status.ONLINE);
            oos.writeObject(connectedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void sendMessage(String mess) {
        try {
            Message message = new Message();
            message.setStatus(Status.ONLINE);
            message.setType(MessageType.USER);
            message.setPicture(avatar);
            message.setMsg(mess);
            message.setName(username);
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
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            Log.d("AppLog", "Da ket noi!");

            connect();

            while (socket.isConnected()) {
                Message mess = (Message) input.readObject();
                if (mess != null) {
                    switch (mess.getType()) {
                        case DISCONNECTED:
                        case IMAGE:
                        case CONNECTED:
                        case USER:
                            listContact = mess.getUsers();
                            listMessage.add(mess);
                            callback(listMessage);
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
