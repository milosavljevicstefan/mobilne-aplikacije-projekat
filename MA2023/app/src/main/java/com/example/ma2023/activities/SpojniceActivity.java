package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;

public class SpojniceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);

        //PLAVA POLJA
//        final Button btn5n1 = findViewById(R.id.button5n1);
//        btn5n1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });
//        final Button btn5n2 = findViewById(R.id.button5n2);
//        btn5n2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });
//          final Button btn5n3 = findViewById(R.id.button5n3);
//        btn5n3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });
//        final Button btn5n4 = findViewById(R.id.button5n4);
//        btn5n4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);
//        final Button btn5n5 = findViewById(R.id.button5n5);
//        btn5n5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                startActivity(intent);



        //CRVENA POLJA todo



        //SLEDECA IGRA dugme
        final Button btn5n11 = findViewById(R.id.button5n11);
        btn5n11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                startActivity(intent);
            }
        });
    }
}