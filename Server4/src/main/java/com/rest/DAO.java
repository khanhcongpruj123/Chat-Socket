package com.rest;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DAO {

    static final String USERNAME_DB = "root";
    static final String DB_NAME = "ichat";
    static final String PASSWORD_DB = "kmrdeveloper315";
    static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    private Logger logger = Logger.getLogger("AppLog");

    public Connection connect() {
        try {
            logger.info("Ket noi voi database");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, USERNAME_DB, PASSWORD_DB);
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
