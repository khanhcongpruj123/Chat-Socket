package com.phong413.ichat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.phong413.ichat.databinding.FragmentBlankBinding;

public class ViewImageActivity extends AppCompatActivity {


    private FragmentBlankBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentBlankBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Glide.with(this)
                .load(getIntent().getData())
                .into(binding.image);

    }
}