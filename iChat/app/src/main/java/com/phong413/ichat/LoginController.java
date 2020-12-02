package com.phong413.ichat;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.concurrent.Executors;

public class LoginController {

    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login(String host, int port, Bitmap avatar, String username) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {

                loginView.showLoading();

                ChatThread.initialize(host, port);
                Bitmap resize = BitmapUtils.resizeBitmap(avatar);
                byte[] _avatar = BitmapUtils.bitmapToByte(resize);
                ChatThread.getInstance().connect(username, _avatar);

                loginView.hideLoading();
                loginView.goToMainView();
            } catch (Exception e) {

                loginView.hideLoading();
                loginView.showError("Kiểm tra lại host, port, username và chọn 1 ảnh đại diện");

                e.printStackTrace();
            }
        });
    }
}
