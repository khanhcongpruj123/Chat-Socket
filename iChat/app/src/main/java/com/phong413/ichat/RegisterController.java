package com.phong413.ichat;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterController {

    private RegisterView registerView;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void register(String username, String password, String confirmPassword, ContentResolver cr, Uri imageUri) {

        if (username == null || username.isEmpty()) return;
        if (password == null || password.isEmpty()) return;
        if (confirmPassword == null || confirmPassword.isEmpty()) return;
        if (!confirmPassword.equals(password)) return;

        if (cr == null) return;
        if (imageUri == null) return;

        Executors.newSingleThreadExecutor().submit(() -> {
            try {

                registerView.showLoading();
                OkHttpClient client = new OkHttpClient.Builder()
                        .build();

                InputStreamRequestBody avatarBody = new InputStreamRequestBody(
                        MediaType.parse("image"),
                        cr,
                        imageUri
                );

                RequestBody body = new  MultipartBody.Builder()
                        .addFormDataPart("username", username)
                        .addFormDataPart("password", password)
                        .addFormDataPart("avatar", "IMG_" + System.currentTimeMillis() + ".jpeg", avatarBody)
                        .build();

                Request req = new Request.Builder()
                        .url("http://" + "192.168.1.56" + ":8080/ichat/register")
                        .post(body)
                        .build();
                Response resp = client.newCall(req).execute();
                int code = resp.code();
                registerView.hideLoading();
                if (code == 200) {
                    registerView.backLogin();
                } else {
                    registerView.showMessage("Đăng kí thất bại");
                }
            } catch (Exception e) {
                e.printStackTrace();
                registerView.hideLoading();
                registerView.showMessage("Đăng kí thất bại");
            }
        });
    }
}
