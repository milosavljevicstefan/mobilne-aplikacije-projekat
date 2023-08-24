package com.example.ma2023.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ma2023.ChatApplication;
import com.example.ma2023.Konekcija;
import com.example.ma2023.MainActivity;
import com.example.ma2023.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.URISyntaxException;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class PocetnaStranaActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private Socket mSocket;
    private QueryDocumentSnapshot user;
    private String aName;
    private String bName;
    private int turn;
    private ChatApplication app;
    private PocetnaStranaActivity ps = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_strana);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.otvoriteMeni,R.string.zetvoriteMeni);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.pocetna:
                    {
                        Log.d("drawable", "upao u pocetnu");
                        Intent intent = new Intent(PocetnaStranaActivity.this, PocetnaStranaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    case R.id.profil:
                    {
                        Log.d("drawable", "upao u profil");
                        Intent intent = new Intent(PocetnaStranaActivity.this, ProfilKorisnika.class);
                        startActivity(intent);
                        finish();
                    }
                    case R.id.rangLista:
                    {
                        Log.d("drawable", "upao u rangListu");
                        Toast.makeText(PocetnaStranaActivity.this, "Izabrali ste rang listu", Toast.LENGTH_SHORT).show();
                    }
                    case R.id.prijatelji:
                    {
                        Log.d("drawable", "upao u prijatelje");
                        Toast.makeText(PocetnaStranaActivity.this, "Izabrali ste prijatelje", Toast.LENGTH_SHORT).show();
                    }
                    case R.id.odjava:
                    {
                        Log.d("drawable", "upao u odjavu");
                        mSocket.close();
                        Intent intent = new Intent(PocetnaStranaActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return false;
            }
        });


        Konekcija app = (Konekcija) PocetnaStranaActivity.this.getApplication();
        this.mSocket = app.getSocket();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userF = auth.getCurrentUser();
        this.bName = userF.getDisplayName();
        Log.d("displayName", "display ime" + bName);

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


        mSocket.on("podaci", (a) -> {


            Map<String, Object> mapa;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapa = mapper.readValue(a[0].toString(), Map.class);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            aName = mapa.get("a").toString();
            bName = mapa.get("b").toString();
            Log.i("login", bName + aName);


        });
        mSocket.on("startMatch", (a) -> {
            StartMatch(a);
        });

        // ZAPOCNI IGRU dugme
        final Button btn3n1 = findViewById(R.id.button3n1);
        btn3n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ButtonClickListener", "Start Game button clicked");
                mSocket.connect();
            }
        });

/*        //IZADJI dugme
        final Button btn3n3 = findViewById(R.id.button3n3);
        btn3n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSocket.close();
                Intent intent = new Intent(PocetnaStranaActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

/*
        final Button btn3n2 = findViewById(R.id.button3n2);
        btn3n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PocetnaStranaActivity.this, ProfilKorisnika.class);
                startActivity(intent);
                finish();
            }
        });
*/

    }
    public void StartMatch( Object a){
        Log.d("Login", "start match");
//        mSocket.emit("Imena");
        Intent intent = new Intent(getApplicationContext(), KoZnaZnaActivity.class);
        intent.putExtra("aName", aName);
        intent.putExtra("bName", bName);
        intent.putExtra("aScore", "0");
        intent.putExtra("bScore", "0");
        intent.putExtra("turn", turn);
        Log.d("login", "a: " + aName + " b: " + bName);
        startActivity(intent);



    }
    public void Tost() {
        runOnUiThread(() -> Toast.makeText(ps, "weiting for other pleyer !", Toast.LENGTH_SHORT).show());
        this.turn = 1;
        mSocket.emit("Ime", bName);
        mSocket.emit("register");
    }

    public void Tost2() throws InterruptedException {
        runOnUiThread(() -> Toast.makeText(ps, "Match will start soon !", Toast.LENGTH_SHORT).show());
        this.turn = 2;
        mSocket.emit("Ime", bName);
        mSocket.emit("Imena");
        mSocket.emit("register");
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();

        }
    }

}