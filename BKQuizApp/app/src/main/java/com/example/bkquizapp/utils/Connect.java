package com.example.bkquizapp.utils;

import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Connect {
    public static Socket socket;
    private static final String URI_SERVER = "http://192.168.43.66:5000/";
    public static void connectServer() {
        //connect to server
        try {
            socket= IO.socket(URI_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();
    }
}
