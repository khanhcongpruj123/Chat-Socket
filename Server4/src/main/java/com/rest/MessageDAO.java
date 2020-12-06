package com.rest;

import com.messages.MessageEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO extends DAO {

    public final String TABLE_MESSAGE_NAME = "TBL_MESSAGE";
    public final String GET_ALL_MESSAGE_SQL = "SELECT * FROM " + TABLE_MESSAGE_NAME;
    public final String SAVE_MESS_SQL = "INSERT INTO " + TABLE_MESSAGE_NAME + "(id, time, username, message, image_url, voice_url) VALUE(?, ?, ?, ?, ?, ?)";

    public List<MessageEntity> getAllMessage() {
        try {

            ArrayList<MessageEntity> res = new ArrayList<>();

            Connection con = connect();
            Statement s = con.createStatement();
            ResultSet resSet = s.executeQuery(GET_ALL_MESSAGE_SQL);
            while (resSet.next()) {

                int id = resSet.getInt(1);
                long time = resSet.getLong(2);
                String username = resSet.getString(3);
                String msg = resSet.getString(4);
                String imageUrl = resSet.getString(5);
                String voiceUrl = resSet.getString(6);

                MessageEntity mess = new MessageEntity();
                mess.setId(id);
                mess.setMsg(msg);
                mess.setUsername(username);
                mess.setImageUrl(imageUrl);
                mess.setVoiceUrl(voiceUrl);
                mess.setTime(time);

                res.add(mess);
            }
            con.close();
            return res;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean saveMessage(MessageEntity mess) {
        try {

            Connection con = connect();

            PreparedStatement stat = con.prepareStatement(SAVE_MESS_SQL);
            stat.setInt(1, mess.getId());
            stat.setLong(2, mess.getTime());
            stat.setString(3, mess.getUsername());
            stat.setString(4, mess.getMsg());
            stat.setString(5, mess.getImageUrl());
            stat.setString(6, mess.getVoiceUrl());

            int res = stat.executeUpdate();

            con.close();

            return res != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
