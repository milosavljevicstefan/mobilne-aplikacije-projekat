package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ma2023.R;

public class KoZnaZnaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ko_zna_zna);
        Intent intent = getIntent();
        TextView aName = findViewById(R.id.editTextAName);
        TextView bName = findViewById(R.id.editTextBName);
        TextView aScore = findViewById(R.id.editTextBName3);
        TextView bScore = findViewById(R.id.editTextBName4);
        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        //ovo ne radi
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));
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