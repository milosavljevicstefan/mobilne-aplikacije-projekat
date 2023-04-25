package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;

public class KoZnaZnaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);

        //ODG1 dugme
//        final Button btn4n1 = findViewById(R.id.button4n1);
//        btn4n1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

        //ODG2 dugme
//        final Button btn4n2 = findViewById(R.id.button4n2);
//        btn4n2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

        //ODG3 dugme
//        final Button btn4n3 = findViewById(R.id.button4n3);
//        btn4n3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

        //ODG4 dugme
//        final Button btn4n4 = findViewById(R.id.button4n4);
//        btn4n4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

        //SLEDECA IGRA dugme
        final Button btn4n5 = findViewById(R.id.button4n5);
        btn4n5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(KoZnaZnaActivity.this, SpojniceActivity.class);
                startActivity(intent);
            }
        });
    }
}