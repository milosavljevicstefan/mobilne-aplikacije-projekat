package com.example.ma2023.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ma2023.MainActivity;
import com.example.ma2023.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //REGISTRUJ SE dugme
        final Button btn2n1 = findViewById(R.id.button2n1);
        final CheckBox provera = findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();
        btn2n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(true) {
                    firestore = FirebaseFirestore.getInstance();
                    Map<String, Object> korisnik = new HashMap<>();
                    String mEdit = ((EditText)findViewById(R.id.editImePrezime)).getText().toString();
                    String[] niz = mEdit.split(" ");
                    Log.d("korisnik/registracija", "aloooooooooooooooooooooooooooooooooooooooooooooooo");
                    korisnik.put("Ime", niz[0].toString());
                    korisnik.put("Prezime", niz[1].toString());
                    korisnik.put("username", ((EditText)findViewById(R.id.editTextDate)).getText().toString());
                    korisnik.put("email", ((EditText)findViewById(R.id.editEmail)).getText().toString());
//
                    korisnik.put("password", ((EditText)findViewById(R.id.editTextTextPassword3)).getText().toString());
                    korisnik.put("profilePicture", ((EditText)findViewById(R.id.editProfilna)).getText().toString());
                    korisnik.put("tokens", 5);
                    korisnik.put("stars", 0);
                    Log.d("korisnik/registracija", korisnik.toString());
                    firestore.collection("korisnici").add(korisnik).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failure", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                Intent intent = new Intent(RegisterActivity.this, PocetnaStranaActivity.class);
                startActivity(intent);
            }
        });
    }
}