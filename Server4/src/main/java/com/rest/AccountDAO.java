package com.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO extends DAO {

    final static String TABLE_ACCOUNT_NAME = "TBL_ACCOUNT";
    final static String LOGIN_SQL = "SELECT * FROM " + TABLE_ACCOUNT_NAME + " WHERE username=? AND password=?";
    final static String REGISTER_SQL = "INSERT INTO " + TABLE_ACCOUNT_NAME + "(username, password, avatar_url) VALUES(?, ?, ?)";
    final static String GET_USER_BY_NAME_SQL = "SELECT * FROM " + TABLE_ACCOUNT_NAME + " WHERE username=?";

    public Account login(String username, String password) {
       try {
           Connection con = connect();
           PreparedStatement stat = con.prepareStatement(LOGIN_SQL);
           stat.setString(1, username);
           stat.setString(2, password);
           ResultSet res = stat.executeQuery();
           Account acc = null;
           while(res.next()) {
               String _username = res.getString(1);
               String _password = res.getString(2);
               String _avatarUrl = res.getString(3);
               acc = new Account(_username, _password, _avatarUrl);
           }
           con.close();
           return acc;
       } catch (SQLException ex) {
           ex.printStackTrace();
           return null;
       }
    }

    public Boolean register(String username, String password, String avatarUrl) {
        try {
            Connection con = connect();
            PreparedStatement stat = con.prepareStatement(REGISTER_SQL);
            stat.setString(1, username);
            stat.setString(2, password);
            stat.setString(3, avatarUrl);
            int res = stat.executeUpdate();
            con.close();
            return res != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Account getAccountByUsername(String username) {
        try {
            Connection con = connect();
            PreparedStatement stat = con.prepareStatement(GET_USER_BY_NAME_SQL);
            stat.setString(1, username);
            ResultSet resSet = stat.executeQuery();
            while (resSet.next()) {
                String _username = resSet.getString(1);
                String password = resSet.getString(2);
                String avatarUrl = resSet.getString(3);
                Account a = new Account(_username, password, avatarUrl);
                return a;
            }
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
