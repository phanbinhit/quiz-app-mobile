package com.example.bkquizapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class RoomIdActivity extends AppCompatActivity {

    private EditText edtRoomId;
    private Button btnRoomId;
    private Socket mSocket;
    private static final String URI_SERVER = "http://192.168.1.6:5000/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamepin_activity);

        edtRoomId = (EditText) findViewById(R.id.edt_roomId);
        btnRoomId = (Button) findViewById(R.id.btn_roomId);

        btnRoomId.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                String inputRoomId = edtRoomId.getText().toString();
                if (inputRoomId.isEmpty()) {
                    Toast.makeText(RoomIdActivity.this, "Enter room id", Toast.LENGTH_SHORT).show();
                } else {
                    //connect to server
                    try {
                        mSocket = IO.socket(URI_SERVER);
                    } catch (URISyntaxException e) {
                        Log.v("AvisActivity", "error connecting to socket");
                        Toast.makeText(RoomIdActivity.this, "Server is not ready", Toast.LENGTH_SHORT).show();
                    }

                    mSocket.connect();

                    mSocket.emit("client-send-roomId", inputRoomId);
                    Emitter.Listener onExist = new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject serverObjRoomId = (JSONObject) args[0];
                                    try {
                                        boolean isExist = serverObjRoomId.getBoolean("exist");
                                        if (isExist == true) {
                                            Intent intent = new Intent(v.getContext(), UserActivity.class);
                                            intent.putExtra("roomId", inputRoomId);
                                            startActivity(intent);
                                            mSocket.disconnect();
                                        } else {
                                            Toast.makeText(RoomIdActivity.this, "Wrong room id", Toast.LENGTH_SHORT).show();
                                            mSocket.disconnect();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };

                    mSocket.on("server-send-roomId", onExist);
                }
            }
        });
    }
}
