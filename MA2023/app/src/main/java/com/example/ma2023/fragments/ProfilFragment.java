package com.example.ma2023.fragments;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ma2023.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfilFragment extends Fragment {
    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
/*
    DatabaseReference reference;
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
/*
         View rootView = inflater.inflate(R.layout.fragment_profil, container, false);
*/




/*
        reference = FirebaseDatabase.getInstance().getReference("users");
*/

       /* editName = rootView.findViewById(R.id.editName);
        editEmail = rootView.findViewById(R.id.editEmail);
        editUsername = rootView.findViewById(R.id.editUsername);
        editPassword = rootView.findViewById(R.id.editPassword);
        saveButton = rootView.findViewById(R.id.saveButton);*/
//        showData();

/*        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged()) {
                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        return inflater.inflate(R.layout.fragment_profil, container, false);
    }



 /*   public boolean isNameChanged(){
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isEmailChanged(){
        if (!emailUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(editPassword.getText().toString())){
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public void showData(){
        Bundle arguments = getArguments();
        if (arguments != null) {
            nameUser = arguments.getString("name");
            emailUser = arguments.getString("email");
            usernameUser = arguments.getString("username");
            passwordUser = arguments.getString("password");

            editName.setText(nameUser);
            editEmail.setText(emailUser);
            editUsername.setText(usernameUser);
            editPassword.setText(passwordUser);
        }
    }*/

}