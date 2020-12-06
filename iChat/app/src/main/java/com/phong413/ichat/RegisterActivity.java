package com.phong413.ichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.phong413.ichat.databinding.ActivityRegisterBinding;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements RegisterView{

    private static final int PICK_IMAGE = 10;

    private ActivityRegisterBinding binding;
    private Uri imageUri;
    private RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerController = new RegisterController(this);

        binding.imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPass = binding.confirmPassword.getText().toString();
            registerController.register(username, password, confirmPass, this.getContentResolver(), imageUri);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                imageUri = data.getData();
                if (imageUri != null) {
                    binding.imgAvatar.setImageURI(imageUri);
                }
            }
        }
    }

    @Override
    public void showLoading() {
        binding.loadingView.post(() -> {
            binding.loadingView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void hideLoading() {
        binding.loadingView.post(() -> {
            binding.loadingView.setVisibility(View.GONE);
        });
    }

    @Override
    public void backLogin() {
        finish();
    }

    @Override
    public void showMessage(String mess) {
        binding.getRoot().post(() -> {
            Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
        });
    }
}