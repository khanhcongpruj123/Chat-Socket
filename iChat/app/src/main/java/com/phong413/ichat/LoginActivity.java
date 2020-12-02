package com.phong413.ichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.phong413.ichat.databinding.ActivityLoginBinding;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final int PICK_IMAGE = 10;
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
                int port = Integer.parseInt(binding.port.getText().toString());
                String host = binding.host.getText().toString();
                String username = binding.username.getText().toString();
                Bitmap avatar = ((BitmapDrawable) binding.imgAvatar.getDrawable()).getBitmap();

                loginController.login(host, port, avatar, username);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        binding.imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    binding.imgAvatar.setImageURI(imageUri);
                }
            }
        }
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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void showError(String message) {
        binding.getRoot().post(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}