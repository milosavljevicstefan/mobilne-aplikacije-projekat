package com.example.ma2023.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ma2023.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfilActivity extends AppCompatActivity {
    EditText  editEmail, editUsername;
    Button changePhoto;
/*
    String nameUser, emailUser, usernameUser, passwordUser;
*/
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        changePhoto = findViewById(R.id.changePhoto);

/*
        showData();
*/


    }


/*
    public boolean isEmailChanged(){
        if (!emailUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }
*/

/*
    public void showData(){
        Intent intent = getIntent();
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
    }*/
}