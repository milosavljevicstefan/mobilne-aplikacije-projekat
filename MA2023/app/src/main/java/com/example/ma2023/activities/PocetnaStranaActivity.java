package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ma2023.ChatApplication;
import com.example.ma2023.Konekcija;
import com.example.ma2023.MainActivity;
import com.example.ma2023.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.URISyntaxException;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PocetnaStranaActivity extends AppCompatActivity {
    private Socket mSocket;
    private QueryDocumentSnapshot user;
    private String bname;
    private String rname;
    private int turn;
    private ChatApplication app;
    private PocetnaStranaActivity ps = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_strana);
        Konekcija app = (Konekcija) PocetnaStranaActivity.this.getApplication();
        this.mSocket = app.getSocket();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userF = auth.getCurrentUser();
        String playerName = userF.getDisplayName();
        Log.d("fireStoreGet", "Display Name: " + playerName + ", Email: " + userF.getEmail());

        this.rname = userF.getDisplayName();

        mSocket.on("pleyer1", (a) -> {
            Tost();
        });
        mSocket.on("pleyer2", (a) -> {
            try {
                Tost2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        mSocket.on("startMatch", (a) -> {

            StartMatch(a);
        });
        mSocket.on("podaci", (a) -> {


            Map<String, Object> mapa;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapa = mapper.readValue(a[0].toString(), Map.class);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            bname = mapa.get("a").toString();
            rname = mapa.get("b").toString();


        });


        // ZAPOCNI IGRU dugme
        final Button btn3n1 = findViewById(R.id.button3n1);
        btn3n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ButtonClickListener", "Start Game button clicked");

            }
        });

        //IZADJI dugme
        final Button btn3n3 = findViewById(R.id.button3n3);
        btn3n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Button btn3n2 = findViewById(R.id.button3n2);
        btn3n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, ProfilKorisnika.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void StartMatch( Object a){
        Log.d("Login", "start match");



    }
    public void Tost() {
        runOnUiThread(() -> Toast.makeText(ps, "weiting for other pleyer !", Toast.LENGTH_SHORT).show());
        this.turn = 1;
        mSocket.emit("Ime", rname);
    }

    public void Tost2() throws InterruptedException {
        runOnUiThread(() -> Toast.makeText(ps, "Match will start soon !", Toast.LENGTH_SHORT).show());
        this.turn = 2;
        mSocket.emit("Ime", rname);
        mSocket.emit("Imena");
        mSocket.emit("start");
    }

}