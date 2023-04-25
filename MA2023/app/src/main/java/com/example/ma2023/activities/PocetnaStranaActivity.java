package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.MainActivity;
import com.example.ma2023.R;

public class PocetnaStranaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_strana);

        //ZAPOCNI IGRU dugme
        final Button btn3n1 = findViewById(R.id.button3n1);
       btn3n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, KoZnaZnaActivity.class);
                startActivity(intent);
            }
        });

        //OPCIJE dugme
//        final Button btn3n2 = findViewById(R.id.button3n2);
//        btn3n2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(PocetnaStranaActivity.this, KoZnaZnaActivity.class);
//                startActivity(intent);
//            }
//        });

        //IZADJI dugme
        final Button btn3n3 = findViewById(R.id.button3n3);
        btn3n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}