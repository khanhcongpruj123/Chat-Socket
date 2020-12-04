package com.phong413.ichat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.phong413.ichat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.pager);
    }
}