package com.example.ma2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma2023.activities.PocetnaStranaActivity;
import com.example.ma2023.activities.RegisterActivity;
import com.example.ma2023.model.Asocijacija;
import com.example.ma2023.model.Kolona;
import com.example.ma2023.model.KorakPoKorak;
import com.example.ma2023.model.Par;
import com.example.ma2023.model.Spojnica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.mindrot.jbcrypt.BCrypt;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SocketHandler;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String serverName = "com.ftn.server";
    private int serverPort = 13;
    private  ChatApplication app;
    private Socket mSocket;


    private EditText emailEditText;
    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        final EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.button1n1);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        CollectionReference asocijacijeCollection = firestore.collection("asocijacije");
//
//        // Primer za jednu Asocijaciju sa četiri kolone
//        List<String> poljaKolone1 = Arrays.asList("Istina", "Podrum", "Mušica", "Bure");
//        List<String> poljaKolone2 = Arrays.asList("Skandinav.", "Kraljevina", "Nobel", "Oslo");
//        List<String> poljaKolone3 = Arrays.asList("Moljac", "Petrolej", "Aladin", "Signal");
//        List<String> poljaKolone4 = Arrays.asList("Tocak", "Lopta", "Obli", "Tanjir");
//
//        Kolona kolona1 = new Kolona(poljaKolone1, "Vino");
//        Kolona kolona2 = new Kolona(poljaKolone2, "Svedska");
//        Kolona kolona3 = new Kolona(poljaKolone3, "Lampa");
//        Kolona kolona4 = new Kolona(poljaKolone4, "Okrugli");
//
//        List<Kolona> kolone = Arrays.asList(kolona1, kolona2, kolona3, kolona4);
//        String konacnoResenje = "Sto";
//        Asocijacija asocijacije = new Asocijacija(kolone, konacnoResenje);
//        asocijacijeCollection.add(asocijacije);
//
//        List<String> poljaKolone1a = Arrays.asList("Noć", "Let", "Pevačica", "Perspektiva");
//        List<String> poljaKolone2a = Arrays.asList("Kći.", "Čizme", "Mreža", "Lola");
//        List<String> poljaKolone3a = Arrays.asList("Pauk", "KK Radnički", "Pohodi", "Ratovi");
//        List<String> poljaKolone4a = Arrays.asList("Lavovi", "Dvor", "Rum", "Luk");
//
//        Kolona kolona1a = new Kolona(poljaKolone1a, "Ptica");
//        Kolona kolona2a = new Kolona(poljaKolone2a, "Ribar");
//        Kolona kolona3a = new Kolona(poljaKolone3a, "Krstaš");
//        Kolona kolona4a = new Kolona(poljaKolone4a, "Beli");
//
//        List<Kolona> kolonea = Arrays.asList(kolona1a, kolona2a, kolona3a, kolona4a);
//        String konacnoResenjea = "Orao";
//        Asocijacija asocijacijea = new Asocijacija(kolonea, konacnoResenjea);
//        asocijacijeCollection.add(asocijacijea);
//
//
//
//        List<Par> parovi = new ArrayList<>();
//        parovi.add(new Par("key1", "value1"));
//        parovi.add(new Par("key2", "value2"));
//        parovi.add(new Par("key3", "value3"));
//        parovi.add(new Par("key4", "value4"));
//        parovi.add(new Par("key5", "value5"));
//
//        Spojnica spojnicaa = new Spojnica("Test 2", parovi);
//
//        firestore.collection("spojnice").add(spojnicaa);
//        firestore = FirebaseFirestore.getInstance()
//        Map<String, Object> pitanje10 = new HashMap<>();
//        pitanje10.put("tekst_pitanja", "Koji je srpski srednjevekovni manastir poznat po freskama koje prikazuju 'Bitku na Kosovu'?");
//        pitanje10.put("tacan_odgovor", "Sopoćani");
//        List<String> pogresniOdgovori10 = new ArrayList<>();
//        pogresniOdgovori10.add("Manasija");
//        pogresniOdgovori10.add("Studenica");
//        pogresniOdgovori10.add("Dečani");
//        pitanje10.put("pogresni_odgovori", pogresniOdgovori10);
//
//        firestore.collection("pitanja").add(pitanje10);



        // ULOGUJ SE dugme
        final Button btn1n1 = findViewById(R.id.button1n1);
        btn1n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUnos = emailEditText.getText().toString();
                String passwordUnos = passwordEditText.getText().toString();

                // Use Firebase Authentication to sign in with email and password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailUnos, passwordUnos)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid(); 
                                        Query query = db.collection("users").whereEqualTo("user_id", userId);
                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.d("LoginProcess", "onComplete triggered");
                                                if (task.isSuccessful()) {
                                                    app = new ChatApplication();
                                                    mSocket = app.getSocket();
                                                    Log.d("LoginProcess", "idSocketa: " + mSocket.id());
                                                    Konekcija appb = (Konekcija) MainActivity.this.getApplication();

                                                    Log.d("LoginProcess", task.getResult().toString());
                                                    Socket socket = appb.setSocket(mSocket);

                                                    Log.d("LoginProcess", socket.toString());
                                                    Toast.makeText(MainActivity.this, "Uspesno prijavljen!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, PocetnaStranaActivity.class);
                                                    intent.putExtra("userEmail", emailUnos);
                                                    intent.putExtra("userPassword", passwordUnos);

                                                    // Print the retrieved data to the console using Log
                                                    Log.d("UserEmail", "User Email: " + emailUnos);
                                                    Log.d("UserPassword", "User Password: " + passwordUnos);
                                                    startActivity(intent);
                                                } else {
                                                    Log.e("LoginProcces", "fail");
                                                    Toast.makeText(MainActivity.this, "Neuspesna prijava!", Toast.LENGTH_SHORT).show();
                                                    emailEditText.setText("");
                                                    passwordEditText.setText("");
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Log.e("LoginProcces", "User is null");
                                    Toast.makeText(MainActivity.this, "Neispravni podaci!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //REGISTRUJ SE dugme
        Button btn1n2 = findViewById(R.id.button1n2);
        btn1n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //IGRAJ KAO GOST dugme
        Button btn1n3 = findViewById(R.id.button1n3);
        btn1n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PocetnaStranaActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean validateEmail() {
        String val = emailEditText.getText().toString();
        if (val.isEmpty()) {
            emailEditText.setError("Morate uneti email");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = passwordEditText.getText().toString();
        if (val.isEmpty()) {
            passwordEditText.setError("Morate uneti sifru");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }
}


//        firestore = FirebaseFirestore.getInstance();
//        Map<String, Object> korisnik = new HashMap<>();
//        korisnik.put("Ime", "Stefan");
//        korisnik.put("Prezime", "Milosavljević");
//        korisnik.put("username", "milo");
//        korisnik.put("email", "milo@example.com");
//        String password = "milo";
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//        korisnik.put("password", hashedPassword);
//        korisnik.put("profilePicture", "url_to_profile_picture1");
//        korisnik.put("tokens", 5);
//        korisnik.put("stars", 0);
//
//        firestore.collection("korisnici").add(korisnik).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_LONG).show();
//            }
//        });




