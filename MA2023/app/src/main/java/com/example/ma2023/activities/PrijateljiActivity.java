package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ma2023.R;
import com.example.ma2023.fragments.PrijateljiFragment;

public class PrijateljiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijatelji);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_prijatelji, new PrijateljiFragment()).commit();
        }
    }
}