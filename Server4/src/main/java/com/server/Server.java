package com.server;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import com.rest.Account;
import com.rest.AccountDAO;
import com.rest.MessageDAO;
import com.messages.MessageEntity;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Server {

    /* Setting up variables */
    private static final int PORT = 9001;
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<User> listUser = new ArrayList<>();
    static Logger logger = Logger.getLogger(Server.class.getSimpleName());

    public static void main(String[] args) throws Exception {
        logger.info("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();

        try {
            while (true) {
                Socket s = listener.accept();
                if (s != null) {
                    new Handler(s, accountDAO, messageDAO).start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }


    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private Logger logger = Logger.getLogger(Handler.class.getSimpleName());
        private User user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;
        private AccountDAO accountDAO;
        private MessageDAO messageDAO;

        public Handler(Socket socket, AccountDAO accountDAO, MessageDAO messageDAO) throws IOException {
            this.socket = socket;
            this.accountDAO = accountDAO;
            this.messageDAO = messageDAO;
        }

        public void run() {
            logger.info("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                getUserAndAddToList(firstMessage.getUser().getUsername());
                writers.add(output);
                sendNotificationUserJoin();
                notificationIsConnected();
                logger.info(user.getUsername() + " da ket noi!");

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        logger.info(inputmsg.getType() + " - " + inputmsg.getUser().getUsername() + ": " + inputmsg.getMessage() + " " + inputmsg.getImageUrl() + " " + inputmsg.getVoiceUrl());
                        switch (inputmsg.getType()) {
                            case IMAGE:
                            case USER:
                                write(inputmsg);
                                break;
                            case VOICE:
                                write(inputmsg);
                                break;
                            case CONNECTED:
//                                sendNotificationUserJoin();
                                break;
                            case DISCONNECTED:
                                closeConnections();
                                break;
                            case STATUS:
                                changeStatus(inputmsg);
                                break;
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
                logger.info("Socket Exception for user " + name);
            } catch (Exception e){
                e.printStackTrace();
                logger.info("Exception in run() method for user: " + name);
            } finally {
                closeConnections();
            }
        }

        private Message changeStatus(Message inputmsg) throws IOException {
            logger.info(user.getUsername() + " đã đổi trạng thái " + inputmsg.getStatus());
            Message msg = new Message();
            msg.setType(MessageType.STATUS);
            user.setStatus(inputmsg.getStatus());
            write(msg);
            return msg;
        }

        private synchronized void getUserAndAddToList(String username) {
            logger.info("Dang thuc hien lay thong tin user da ket noi va add va danh sach");
            Account a = accountDAO.getAccountByUsername(username);
            if (a != null) {
                user = new User();
                user.setUsername(a.getUsername());
                user.setAvatarUrl(a.getAvatarUrl());
                user.setStatus(Status.ONLINE);
                listUser.add(user);
            }
        }

        private Message sendNotificationUserJoin() throws IOException {

            Message msg = new Message();
            msg.setMessage("Chào mừng " + user.getUsername() + " đã tham gia phòng chat");
            msg.setType(MessageType.NOTIFICATION);
            write(msg);
            return msg;
        }

        private Message notificationIsConnected() throws IOException {

            List<MessageEntity> list = messageDAO.getAllMessage();

            ArrayList<Message> listMess = new ArrayList<>();
            list.forEach(messageEntity -> {
                Account a = accountDAO.getAccountByUsername(messageEntity.getUsername());
                User u = new User();
                u.setUsername(a.getUsername());
                u.setAvatarUrl(a.getAvatarUrl());
                Message message = new Message();
                message.setUser(u);
                message.setImageUrl(messageEntity.getImageUrl());
                message.setVoiceUrl(messageEntity.getVoiceUrl());
                message.setMessage(messageEntity.getMsg());

                listMess.add(message);
            });

            Message msg = new Message();
            msg.setListMess(listMess);
            msg.setType(MessageType.CONNECTED);
            writeOnlyUser(msg);
            return msg;
        }


        private Message removeFromList() throws IOException {
            logger.info("Gui tin nhan thong bao " + user.getUsername() + " đã rời khỏi phòng chat") ;
            Message msg = new Message();
            msg.setMessage(user.getUsername() + "đã rời khỏi phòng chat");
            msg.setType(MessageType.SERVER);
            msg.setListUser(listUser);
            write(msg);
            return msg;
        }


        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            logger.info("Write " + msg.getType());
            for (ObjectOutputStream writer : writers) {
                msg.setListUser(listUser);
                writer.writeObject(msg);
                writer.reset();
            }
            if (msg.getUser() == null) return;
            messageDAO.saveMessage(
                    new MessageEntity(
                           0,
                            msg.getImageUrl(),
                            System.currentTimeMillis(),
                            msg.getUser().getUsername(),
                            msg.getVoiceUrl(),
                            msg.getMessage()
                    )
            );
        }

        private void writeOnlyUser(Message msg) throws IOException {
            logger.info("Write Only " + msg.getType());
            msg.setListUser(listUser);
            output.writeObject(msg);
            output.reset();
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove the writers
         */
        private synchronized void closeConnections()  {
            logger.info(user.getUsername() +" đã ngắt kết nôi");
            logger.info("Danh sach con lai: " + listUser.size());
            if (user != null){
                listUser.remove(user);
                logger.info(user.getUsername() + " đã loại khỏi danh sách");
            }
            if (output != null){
                writers.remove(output);
                logger.info("Luồng " + user.getUsername() + " đã loại khỏi danh sách");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
