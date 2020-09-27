package com.example.bkquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ResultActivity extends AppCompatActivity {

    private TextView tvResult;
    private TextView tvUserName;
    private Button btnRestart;
    private Socket mSocket;
    private static final String URI_SERVER = "http://192.168.1.6:5000/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intentResult = getIntent();
        String score = intentResult.getStringExtra("score");
        String userName = intentResult.getStringExtra("user");
        String numberRight = intentResult.getStringExtra("numberRight");
        String numberQuestion = intentResult.getStringExtra("numberQuestion");
        String roomId = intentResult.getStringExtra("roomId");
        JSONObject resultJsonObj = new JSONObject();
        try {
            resultJsonObj.put("roomId", roomId);
            resultJsonObj.put("user", userName);
            resultJsonObj.put("score", Float.parseFloat(score));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvResult = (TextView) findViewById(R.id.tv_result);
        tvUserName = (TextView) findViewById(R.id.tv_userResult);
        btnRestart = (Button) findViewById(R.id.btn_restart);

        //connect to server
        try {
            mSocket = IO.socket(URI_SERVER);
        } catch (URISyntaxException e) {
            Log.v("AvisActivity", "error connecting to socket");
            Toast.makeText(ResultActivity.this, "Server is not ready", Toast.LENGTH_SHORT).show();
        }

        mSocket.connect();

        mSocket.emit("client-send-result", resultJsonObj);

        tvUserName.setText(userName);
        tvResult.setText("Your score: " + score + " (" + numberRight + "/" + numberQuestion + ")");
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RoomIdActivity.class));
                mSocket.disconnect();
            }
        });

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You have completed the test", Toast.LENGTH_SHORT).show();
    }
}
