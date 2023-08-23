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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.TaskCompletionSource;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity<global> extends AppCompatActivity {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    ArrayList<String> listaUsername = new ArrayList<>();
    ArrayList<String> listaEmail = new ArrayList<>();

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

                String email = ((EditText) findViewById(R.id.editEmail)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.editTextTextPassword3)).getText().toString().trim();
                String username = ((EditText) findViewById(R.id.editTextDate)).getText().toString(); // Get the username from EditText

                // Check if the username is already taken
                
                if (validateFields(provera.isChecked())) {
                    // All fields are valid, proceed with registration
                    firestore = FirebaseFirestore.getInstance();
                    Map<String, Object> korisnik = new HashMap<>();
                    String mEdit = ((EditText) findViewById(R.id.editImePrezime)).getText().toString();
                    String[] niz = mEdit.split(" ");
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
                    checkUsernameAvailability(username, email, password);
                }
            }
        });
    }


    private void checkUsernameAvailability(final String username, final String email, final String password) {

        firestore.collection("korisnici")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // If the query result is empty, the username is not taken
                            if (task.getResult().isEmpty()) {
                                // Create the user using email and password
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // User registration success
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(username)
                                                            .build();
                                                    Log.d("displayName", "Setting display name to: " + username);
                                                    user.updateProfile(profileUpdates)
                                                            .addOnCompleteListener(taskk -> {
                                                                if (taskk.isSuccessful()) {
                                                                    Log.d("displayName", "Display name updated successfully");
                                                                } else {
                                                                    Log.e("displayName", "Error updating display name: " + task.getException());
                                                                }
                                                            });
                                                    saveUserDataToFirestore(user);
                                                    // Start the main activity or any other desired activity
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    // User registration failed
                                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                // Username is already taken
                                Toast.makeText(RegisterActivity.this, "Username is already taken. Please choose a different username.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred while checking username availability
                            Toast.makeText(RegisterActivity.this, "Error checking username availability.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void saveUserDataToFirestore(FirebaseUser user) {
        Map<String, Object> korisnik = new HashMap<>();
        String mEdit = ((EditText) findViewById(R.id.editImePrezime)).getText().toString();
        String[] niz = mEdit.split(" ");

        korisnik.put("Ime", niz[0]);
        korisnik.put("Prezime", niz[1]);
        korisnik.put("username", ((EditText) findViewById(R.id.editTextDate)).getText().toString());
        korisnik.put("email", user.getEmail());
        korisnik.put("profilePicture", ((EditText) findViewById(R.id.editProfilna)).getText().toString());
        korisnik.put("tokens", 5);
        korisnik.put("stars", 0);

        firestore.collection("korisnici").add(korisnik)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Firestore", "User data saved to Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error saving user data to Firestore: " + e.getMessage());
                    }
                });
    }

    private boolean validateFields(boolean isCheckBoxChecked) {
        EditText imePrezimeEditText = findViewById(R.id.editImePrezime);
        EditText usernameEditText = findViewById(R.id.editTextDate);
        EditText emailEditText = findViewById(R.id.editEmail);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword3);
        final boolean[] retVal = {true};

        String imePrezime = imePrezimeEditText.getText().toString().trim();
        if (imePrezime.isEmpty()) {
            imePrezimeEditText.setError("Morate uneti ime i prezime");
            return false;
        } else if (!imePrezime.contains(" ")) {
            imePrezimeEditText.setError("Unesite ime i prezime odvojene razmakom");
            return false;
        }
        for (String e : listaEmail) {
            if (emailEditText.getText().toString().trim().equals(e)) {
                emailEditText.setError("Taj email vec postoji");
                return false;
            }
        }
        if (usernameEditText.getText().toString().isEmpty()) {
            usernameEditText.setError("Morate uneti korisničko ime");
            return false;
        }
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
        for (String username : listaUsername) {
            if (username.equals(usernameEditText.getText().toString().trim())) {
                usernameEditText.setError("Taj username vec postoji!");
                return false;
            }

        }
        return true;
    }
}

