package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.R;

public class SkockoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skocko);

//        final Button btnSkocko = findViewById(R.id.imageButton7nSkocko);
//        btnSkocko.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        final Button btnHerc = findViewById(R.id.imageButton7nHerc);
//        btnHerc.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        final Button btnKaro = findViewById(R.id.imageButton7nKaro);
//        btnKaro.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        final Button btnZvezda = findViewById(R.id.imageButton7nZvezda);
//        btnZvezda.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        final Button btnPik = findViewById(R.id.imageButton7nPik);
//        btnPik.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });

//        final Button btnTref = findViewById(R.id.imageButton7nTref);
//        btnTref.setOnClickListener(new ?.OnClickListener() {
//            public void onClick() {
//                Intent intent = new Intent();
//                startActivity(intent);
//            }
//        });



        final Button btn7n1 = findViewById(R.id.button7n1);
        btn7n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SkockoActivity.this, KorakPoKorakActivity.class);
                startActivity(intent);
            }
        });
    }
}