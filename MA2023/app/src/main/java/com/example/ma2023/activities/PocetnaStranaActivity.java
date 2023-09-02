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

import java.util.Map;

import io.socket.client.Socket;

public class PocetnaStranaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private Socket mSocket;
    private QueryDocumentSnapshot user;
    private String aName;
    private String bName;

    //Podaci korisnika za prosledjivanje
    private String userEmail, userPassword;

    private int turn;
    private ChatApplication app;
    private PocetnaStranaActivity ps = this;

    private FirebaseAuth auth;

    //1.dodati prilikom logovanje proveru koliko bodovoa ima prijavljeni igrac
    //ako ima npr 0-10 rangI, 10-50 rangII, 50+ rangIII poslati  notifikaciju
    //ako je promeni rang. sve notf cuvati u bazi, prikazati pocetna->rang->dugme(sve notf)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_strana);

/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.bringToFront();

        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.otvoriteMeni,R.string.zetvoriteMeni);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Podaci korisnika poslati iz MainAc
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        userPassword = intent.getStringExtra("userPassword");

   /*     drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ActionBarDrawerToggle kresuje app
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.otvoriteMeni,R.string.zetvoriteMeni);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/








        Konekcija app = (Konekcija) PocetnaStranaActivity.this.getApplication();
        this.mSocket = app.getSocket();
        auth = FirebaseAuth.getInstance();
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

/*    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_profil,fragment);
        fragmentTransaction.commit();
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.pocetna:
                Toast.makeText(PocetnaStranaActivity.this, "Povratak na pocetnu stranu", Toast.LENGTH_SHORT).show();break;

            case R.id.profil:

     
                Intent profilIntent = new Intent(PocetnaStranaActivity.this, ProfilActivity.class);
                profilIntent.putExtra("userEmail", userEmail); // userEmail is from onCreate
                profilIntent.putExtra("userPassword", userPassword); // userPassword is from onCreate
                // Print the retrieved data to the console using Log
                Log.d("UserEmail", "User Email: " + userEmail);
                Log.d("UserPassword", "User Password: " + userPassword);
                startActivity(profilIntent);
                break;
            case R.id.rangLista:
                Intent intent2 = new Intent(PocetnaStranaActivity.this, RangActivity.class);
                startActivity(intent2);break;
            case R.id.prijatelji:
                Intent intent3 = new Intent(PocetnaStranaActivity.this, PrijateljiActivity.class);
                startActivity(intent3);break;
            case R.id.odjava:
                auth.signOut();
                signOutUser();
        
                Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void StartMatch( Object a){
        Log.d("Login", "start match");
//        mSocket.emit("Imena");
        Intent intent = new Intent(getApplicationContext(), AsocijacijeActivity.class);
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

    //otvara meni
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
        }        else
        {
            super.onBackPressed();

        }
    }

    private void signOutUser(){
        mSocket.close();
        Intent intent = new Intent(PocetnaStranaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}