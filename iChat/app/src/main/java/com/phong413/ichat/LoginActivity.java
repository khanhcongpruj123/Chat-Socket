package com.phong413.ichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.phong413.ichat.databinding.ActivityLoginBinding;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private ActivityLoginBinding binding;

    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginController = new LoginController(this);

        binding.btnLogin.setOnClickListener(v -> {
            try {

                String host = binding.host.getText().toString();
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();

                loginController.login(host, username, password);

            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Đã có lỗi xảy ra!");
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

    }



    @Override
    public void showLoading() {
        binding.getRoot().post(() -> {
           binding.loadingView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void hideLoading() {
        binding.getRoot().post(() -> {
            binding.loadingView.setVisibility(View.GONE);
        });
    }

    @Override
    public void goToMainView() {
        Log.d("AppLog", "Chuyen sang main view");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void showError(String message) {
        binding.getRoot().post(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}