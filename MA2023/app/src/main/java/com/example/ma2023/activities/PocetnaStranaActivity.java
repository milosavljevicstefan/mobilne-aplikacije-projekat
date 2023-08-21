package com.example.ma2023.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ma2023.MainActivity;
import com.example.ma2023.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;

public class PocetnaStranaActivity extends AppCompatActivity {
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_strana);

        try {
            socket = IO.socket("http://192.168.56.1:4001");
            socket.connect();
            Log.d("SocketConnection", "Socket connected successfully"); // Debugging log
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("SocketConnection", "Socket connection failed: " + e.getMessage()); // Debugging log
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String playerName = user.getDisplayName();
        Log.d("fireStoreGet", "Display Name: " + playerName + ", Email: " + user.getEmail());

        // ZAPOCNI IGRU dugme
        final Button btn3n1 = findViewById(R.id.button3n1);
        btn3n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ButtonClickListener", "Start Game button clicked");
                socket.emit("playerReady", playerName);
            }
        });


        // Implement event handling here based on your requirements
        socket.on("startMatch", args -> {
            // Handle start match event
            // For example, start a new activity or update UI
            Log.d("SocketEvent", "Match started: " + args[0]);
            // Implement your logic here
        });

        // Implement more event listeners as needed

        // Close the socket when the activity is destroyed
        // (Uncomment this line if you want to close the socket)
        // socket.close();


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

        final Button btn3n2 = findViewById(R.id.button3n2);
        btn3n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, ProfilKorisnika.class);
                startActivity(intent);
                finish();
            }
        });

    }
}