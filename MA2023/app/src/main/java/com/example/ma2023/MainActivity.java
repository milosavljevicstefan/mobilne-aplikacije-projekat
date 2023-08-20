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

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);

        TextView userNotFoundTextView = findViewById(R.id.userNotFoundTextView);
        userNotFoundTextView.setVisibility(View.GONE);

        Button loginButton = findViewById(R.id.button1n1);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validatePassword()) {
                    // Validation failed, do nothing or show an error message
                    Log.d("LoginValidation", "Validation failed");
                } else {

                    String emailUnos = emailEditText.getText().toString();
                    String passwordUnos = passwordEditText.getText().toString();

                    firestore.collection("korisnici")
                            .whereEqualTo("email", emailUnos)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        userNotFoundTextView.setVisibility(View.GONE);
                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String email = documentSnapshot.getString("email");
                                            String password = documentSnapshot.getString("password");
                                            if (emailUnos.equals(email) && passwordUnos.equals(password)) {
                                                Intent intent = new Intent(MainActivity.this, PocetnaStranaActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Log.d("FirestoreData", "Invalid credentials.");
                                            }
                                        }
                                    } else {
                                        userNotFoundTextView.setVisibility(View.VISIBLE);
                                        Log.d("FirestoreData", "No matching documents found.");

                                        // Delay hiding the warning message for 5 seconds
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                userNotFoundTextView.setVisibility(View.GONE);
                                            }
                                        }, 5000); // 5000 milliseconds = 5 seconds
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




