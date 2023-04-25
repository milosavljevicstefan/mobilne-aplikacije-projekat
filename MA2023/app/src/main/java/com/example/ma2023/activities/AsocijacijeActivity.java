package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;

public class AsocijacijeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asocijacije);





        //SLEDECA IGRA dugme
        final Button btn6n1 = findViewById(R.id.button6n1);
        btn6n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AsocijacijeActivity.this, SkockoActivity.class);
                startActivity(intent);
            }
        });
    }


}