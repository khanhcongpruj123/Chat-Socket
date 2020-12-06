package com.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.logging.Logger;

@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class RegisterServlet extends HttpServlet {

    private Logger logger = Logger.getLogger("AppLog");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);

        String uploadPath = getServletContext().getRealPath("") + "image";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // lay anh dai dien
        Part avatarPart = req.getPart("avatar");
        avatarPart.write(uploadPath + File.separator + avatarPart.getSubmittedFileName());

        Part usernamePart = req.getPart("username");
        InputStream inputUsername = usernamePart.getInputStream();
        BufferedReader usernameReader = new BufferedReader(new InputStreamReader(inputUsername));
        String username = usernameReader.readLine();

        Part passwordPart = req.getPart("password");
        InputStream inputPassword = passwordPart.getInputStream();
        BufferedReader passwordReader = new BufferedReader(new InputStreamReader(inputPassword));
        String password = passwordReader.readLine();

        logger.info("Dang ki " + username + " " + password);

        AccountDAO dao = new AccountDAO();
        Boolean isValid = dao.register(username, password, "/image/" + avatarPart.getSubmittedFileName());
        logger.info("Dang ki thanh cong? " + isValid);

        if (!isValid) {
            JsonObject resJson = new JsonObject();
            resJson.addProperty("msg", "Dang ki that bai");
            resJson.add("account", null);

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getOutputStream().println(resJson.toString());
        } else {
            resp.setStatus(HttpServletResponse.SC_OK);
            Gson gson = new Gson();
            Account account = new Account(username, password, "/image/" + avatarPart.getSubmittedFileName());

            JsonObject resJson = new JsonObject();
            resJson.addProperty("msg", "Dang ki thanh cong");
            resJson.add("account", gson.toJsonTree(account));

            resp.getOutputStream().println(resJson.toString());
        }
    }
}
