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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import java.util.HashMap;
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

        mAuth = FirebaseAuth.getInstance();



        TextView userNotFoundTextView = findViewById(R.id.userNotFoundTextView);
        userNotFoundTextView.setVisibility(View.GONE);


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




