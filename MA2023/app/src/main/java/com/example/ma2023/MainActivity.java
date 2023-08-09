package com.example.ma2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ma2023.activities.PocetnaStranaActivity;
import com.example.ma2023.activities.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        final EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.button1n1);

        mAuth = FirebaseAuth.getInstance();

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



        //ULOGUJ SE dugme
        final Button btn1n1 = findViewById(R.id.button1n1);
        btn1n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUnos = emailEditText.getText().toString();
                String passwordUnos = passwordEditText.getText().toString();

                firestore.collection("korisnici")
                        .whereEqualTo("email", emailUnos)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        // Get email and password fields from the document
                                        String email = documentSnapshot.getString("email");
                                        String password = documentSnapshot.getString("password");
                                        if (emailUnos.equals(email) && passwordUnos.equals(password)) {
                                            Intent intent = new Intent(MainActivity.this, PocetnaStranaActivity.class);
                                            startActivity(intent);
                                        }
                                        // Do something with the email and password
                                        Log.d("FirestoreData", "Email: " + email + ", Password: " + password);
                                    }
                                } else {
                                    Log.d("FirestoreData", "No matching documents found.");
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FirestoreData", "Error getting documents: " + e.getMessage());
                            }
                        });
            }
        });

        //REGISTRUJ SE dugme
        final Button btn1n2 = findViewById(R.id.button1n2);
        btn1n2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //IGRAJ KAO GOST dugme
        final Button btn1n3 = findViewById(R.id.button1n3);
        btn1n3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PocetnaStranaActivity.class);
                startActivity(intent);
            }
        });
    }
}



