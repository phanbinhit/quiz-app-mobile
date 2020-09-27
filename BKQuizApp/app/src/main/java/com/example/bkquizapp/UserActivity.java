package com.example.bkquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class UserActivity extends AppCompatActivity {

    private EditText edtUser;
    private Button btnUser;
    private Socket mSocket;
    private static final String URI_SERVER = "http://192.168.1.6:5000/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        Intent intentRoomId = getIntent();
        final String roomId = intentRoomId.getStringExtra("roomId");

        edtUser = (EditText) findViewById(R.id.edt_userId);
        btnUser = (Button) findViewById(R.id.btn_userId);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUserName = edtUser.getText().toString();
                if (inputUserName.isEmpty()) {
                    Toast.makeText(UserActivity.this, "Enter user name", Toast.LENGTH_SHORT).show();
                } else {
                    //connect to server
                    try {
                        mSocket = IO.socket(URI_SERVER);
                    } catch (URISyntaxException e) {
                        Log.v("AvisActivity", "error connecting to socket");
                        Toast.makeText(UserActivity.this, "Server is not ready", Toast.LENGTH_SHORT).show();
                    }

                    mSocket.connect();
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("roomId", roomId);
                        obj.put("username", inputUserName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("client-sent-user", obj);
                    mSocket.on("is-contant-user", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   JSONObject contantObj = (JSONObject) args[0];
                                   try {
                                       boolean contant = contantObj.getBoolean("contant");
                                       if (contant) {
                                           Toast.makeText(UserActivity.this, "User is existed", Toast.LENGTH_SHORT).show();
                                           mSocket.disconnect();
                                       } else {
                                           Intent intent = new Intent(v.getContext(), MainActivity.class);
                                           intent.putExtra("roomId", roomId);
                                           intent.putExtra("userName", inputUserName);
                                           startActivity(intent);
                                           mSocket.disconnect();
                                       }
                                   } catch (JSONException e) {
                                       e.printStackTrace();
                                   }
                               }
                           });
                        }
                    });

                }
            }
        });

    }
}
