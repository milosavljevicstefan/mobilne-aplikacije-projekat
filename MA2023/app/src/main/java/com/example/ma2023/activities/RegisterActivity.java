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

        // REGISTRUJ SE dugme
        final Button btn2n1 = findViewById(R.id.button2n1);
        final CheckBox provera = findViewById(R.id.checkBox);
        mAuth = FirebaseAuth.getInstance();

        btn2n1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateFields(provera.isChecked())) {
                    // All fields are valid, proceed with registration
                    firestore = FirebaseFirestore.getInstance();
                    Map<String, Object> korisnik = new HashMap<>();
                    String mEdit = ((EditText) findViewById(R.id.editImePrezime)).getText().toString();
                    String[] niz = mEdit.split(" ");
                    Log.d("korisnik/registracija", "aloooooooooooooooooooooooooooooooooooooooooooooooo");
                    korisnik.put("Ime", niz[0].toString());
                    korisnik.put("Prezime", niz[1].toString());
                    korisnik.put("username", ((EditText) findViewById(R.id.editTextDate)).getText().toString());
                    korisnik.put("email", ((EditText) findViewById(R.id.editEmail)).getText().toString());
                    //
                    korisnik.put("password", ((EditText) findViewById(R.id.editTextTextPassword3)).getText().toString());
                    korisnik.put("profilePicture", ((EditText) findViewById(R.id.editProfilna)).getText().toString());
                    korisnik.put("tokens", 5);
                    korisnik.put("stars", 0);
                    Log.d("korisnik/registracija", korisnik.toString());
                    firestore.collection("korisnici").add(korisnik).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            // Only start the new activity if the registration is successful
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateFields(boolean isCheckBoxChecked) {
        EditText imePrezimeEditText = findViewById(R.id.editImePrezime);
        EditText usernameEditText = findViewById(R.id.editTextDate);
        EditText emailEditText = findViewById(R.id.editEmail);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword3);

        String imePrezime = imePrezimeEditText.getText().toString().trim();
        if (imePrezime.isEmpty()) {
            imePrezimeEditText.setError("Morate uneti ime i prezime");
            return false;
        } else if (!imePrezime.contains(" ")) {
            imePrezimeEditText.setError("Unesite ime i prezime odvojene razmakom");
            return false;
        }

        //DODATI: PROVERA DA LI U BAZI VEC POSTOJI KORISNIK SA TIM KORISNICKIM IMENOM
        if (usernameEditText.getText().toString().isEmpty()) {
            usernameEditText.setError("Morate uneti korisničko ime");
            return false;
        }

        //DODATI: PROVERA DA LI U BAZI VEC POSTOJI KORISNIK SA TOM EMAIL ADRESOM
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Morate uneti email");
            return false;
        } else if (!email.contains("@")) {
            emailEditText.setError("Pogrešan format email adrese");
            return false;
        }

        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError("Morate uneti šifru");
            return false;
        }
        if (!isCheckBoxChecked) {
            Toast.makeText(getApplicationContext(), "Morate prihvatiti uslove koriscenja", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}