package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.MainActivity;
import com.example.ma2023.R;

public class MojBrojActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moj_broj);

        final Button btn1n3 = findViewById(R.id.button);
        btn1n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MojBrojActivity.this, PocetnaStranaActivity.class);
                startActivity(intent);
            }
        });
    }
}