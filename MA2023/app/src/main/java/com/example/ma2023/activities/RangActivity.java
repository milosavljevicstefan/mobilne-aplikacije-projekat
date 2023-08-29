package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;
import com.example.ma2023.fragments.DodajPrijateljaFragment;
import com.example.ma2023.fragments.NotifikacijeFragment;

public class RangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rang);

        // Find the button by its ID
        Button button = findViewById(R.id.notifikacije);

        // Set an OnClickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(RangActivity.this, NotifikacijeActivity.class);
                startActivity(intent2);
            }
        });
    }


}
