package com.phong413.ichat;

import android.content.Intent;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginController {

    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(String host, String username, String password) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {

                loginView.showLoading();

                OkHttpClient client = new OkHttpClient.Builder()
                        .build();

                RequestBody body = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request req = new Request.Builder()
                        .url("http://" + host + ":8080/ichat/login")
                        .post(body)
                        .build();
                Response resp = client.newCall(req).execute();
                int code = resp.code();
                if (code == 200) {

                    String resStr = resp.body().string();
                    JsonObject resJson = JsonParser.parseString(resStr).getAsJsonObject();
                    JsonObject accJson = resJson.getAsJsonObject("account");

                    Gson gson = new Gson();
                    Account a = gson.fromJson(accJson, Account.class);

                    ChatThread.initialize(host, 9001, a);
                    ChatThread.getInstance().connect(() -> {
                        loginView.goToMainView();
                        loginView.hideLoading();
                    });
                } else {
                    loginView.showError("Đăng nhập thật bại");
                    loginView.hideLoading();
                }
            } catch (Exception e) {
                e.printStackTrace();
                loginView.hideLoading();
                loginView.showError("Đăng nhập thật bại");
            }
        });
    }
}
