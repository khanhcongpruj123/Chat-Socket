package com.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {

    private Logger logger = Logger.getLogger("AppLog");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        logger.info("Dang nhap " + username + " " + password);

        AccountDAO dao = new AccountDAO();
        Account acc = dao.login(username, password);
        boolean isValid = acc != null;
        logger.info("Dang nhap thanh cong? " + isValid);

        if (acc == null) {
            JsonObject resJson = new JsonObject();
            resJson.addProperty("msg", "Dang nhap that bai");
            resJson.add("account", null);

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json");
            resp.getOutputStream().println(resJson.toString());
        } else {

            Gson gson = new Gson();

            resp.setStatus(HttpServletResponse.SC_OK);
            JsonObject resJson = new JsonObject();
            resJson.addProperty("msg", "Dang nhap thanh cong");
            resJson.add("account", gson.toJsonTree(acc));

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getOutputStream().println(resJson.toString());
        }
    }
}
