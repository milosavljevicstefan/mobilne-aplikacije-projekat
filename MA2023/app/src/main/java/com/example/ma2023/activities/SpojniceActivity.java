package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ma2023.Konekcija;
import com.example.ma2023.R;
import com.example.ma2023.model.Spojnica;
import com.example.ma2023.service.SpojnicaService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.socket.client.Socket;

public class SpojniceActivity extends AppCompatActivity implements SpojnicaService.OnSpojniceLoadedListener {
    List<Spojnica> spojniceZaIgru;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Konekcija app = (Konekcija) SpojniceActivity.this.getApplication();
        this.mSocket = app.getSocket();
        SpojnicaService spojnicaService = new SpojnicaService(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userF = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spojnice);
        Intent intent = getIntent();
        TextView aName = findViewById(R.id.textViewAName);
        TextView bName = findViewById(R.id.textViewBName);
        TextView aScore = findViewById(R.id.editTextBName2);
        TextView bScore = findViewById(R.id.editTextBName5);
        aName.setText(intent.getStringExtra("aName"));
        bName.setText(intent.getStringExtra("bName"));
        aScore.setText(intent.getStringExtra("aScore"));
        bScore.setText(intent.getStringExtra("bScore"));




        Button a1 = findViewById(R.id.button5n1);
        Button a2 = findViewById(R.id.button5n2);
        Button a3 = findViewById(R.id.button5n3);
        Button a4 = findViewById(R.id.button5n4);
        Button a5 = findViewById(R.id.button5n5);

        Button b1 = findViewById(R.id.button5n6);
        Button b2 = findViewById(R.id.button5n7);
        Button b3 = findViewById(R.id.button5n8);
        Button b4 = findViewById(R.id.button5n9);
        Button b5 = findViewById(R.id.button5n10);



        //ovo zatvara igru jednom igracu
        if(userF.getDisplayName().toString() != aName.getText().toString()) {

            a1.setEnabled(false);
            a2.setEnabled(false);
            a3.setEnabled(false);
            a4.setEnabled(false);
            a5.setEnabled(false);

            b1.setEnabled(false);
            b2.setEnabled(false);
            b3.setEnabled(false);
            b4.setEnabled(false);
            b5.setEnabled(false);
        }

//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSocket.emit("potez");
//            }
//        });

        //SLEDECA IGRA dugme
        final Button btn5n11 = findViewById(R.id.button5n11);
        btn5n11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SpojniceActivity.this, AsocijacijeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSpojniceLoaded(List<Spojnica> spojnice) {
        spojniceZaIgru = spojnice;
        Log.d("spojnice", "Pitanja ucitana");
        if (mSocket != null) {
            Log.d("spojnice", "Ovde saljemo emit");
        }
    }
}