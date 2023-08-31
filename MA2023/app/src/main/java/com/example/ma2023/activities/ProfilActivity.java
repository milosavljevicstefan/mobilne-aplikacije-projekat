package com.example.ma2023.activities;

import android.content.Intent;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.ma2023.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



public class ProfilActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView profileEmail, profileUsername;
    private FirebaseUser currentUser;
    private String userEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        profileEmail = findViewById(R.id.editEmail);
        profileUsername = findViewById(R.id.editUsername);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");

        if (currentUser != null && userEmail != null) {
            String userEmail = currentUser.getEmail();
            profileEmail.setText(userEmail);
            queryUserByUsername(userEmail);
        }


    }





    public void queryUserByUsername(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("korisnici")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            if (username != null) {
                                showUserData(username, userEmail);
                            } else {
                                // Handle case when username is not found
                            }
                        }
                    } else {
                        // Handle Firestore query error
                    }
                });
    }

    public void showUserData(String userUsername, String userEmail) {
        String modifiedUsername = "Korisnicko ime: " + userUsername;
        profileUsername.setText(modifiedUsername);

        String modifiedEmail = "Email: " + userEmail;
        profileEmail.setText(modifiedEmail);
    }
}