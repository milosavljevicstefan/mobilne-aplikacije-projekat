package com.example.ma2023;

import android.app.Application;
import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3001");
            Log.d("LoginProcess", "uspesan chatapp: " + mSocket);
        } catch (URISyntaxException e) {
            Log.d("LoginProcess", "ne valja");
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}