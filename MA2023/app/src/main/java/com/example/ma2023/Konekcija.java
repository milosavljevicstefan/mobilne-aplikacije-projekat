package com.example.ma2023;

import android.app.Application;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import io.socket.client.Socket;


public class Konekcija  extends Application {
    private Socket socket;
    private QueryDocumentSnapshot user;

    public Socket getSocket(){
        return socket;
    }
    public QueryDocumentSnapshot getUser(){
        return user;
    }

    public Socket setSocket(Socket socket1){
        socket = socket1;
        return socket1;
    }
    public void setUser(QueryDocumentSnapshot u){
        this.user = u;
    }

}