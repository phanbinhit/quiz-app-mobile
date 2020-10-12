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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity{
    private EditText edtId, edtPassword;
    private Button btnLogin;
    private Connect connect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtId = (EditText) findViewById(R.id.edt_id);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        connect = new Connect();
        connect.connectServer();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String password = edtPassword.getText().toString();
                JSONObject loginObj = new JSONObject();
                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter id and password", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        loginObj.put("id", id);
                        loginObj.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    connect.socket.emit("client-send-login", loginObj);
                    Emitter.Listener loginEmit = new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject serverLogin = (JSONObject) args[0];
                                    try {
                                        if(serverLogin.getBoolean("hasAccount") == false) {
                                            Toast.makeText(LoginActivity.this, "Student not exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (serverLogin.getBoolean("isRightPass") == false) {
                                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                            } else {
                                                JSONObject studentJson = serverLogin.getJSONObject("student");
                                                String idStudent = studentJson.getString("idStudent");
                                                String name = studentJson.getString("name");
                                                String className = studentJson.getString("className");
                                                JSONArray examArrayJson = studentJson.getJSONArray("exams");
                                                List<ExamResult> examIds = new ArrayList<>();
                                                for (int i = 0 ; i < examArrayJson.length(); i++) {
                                                    String roomId = examArrayJson.getJSONObject(i).getString("roomId");
                                                    boolean isCompleted = examArrayJson.getJSONObject(i).getBoolean("isCompleted");
                                                    double score = examArrayJson.getJSONObject(i).getDouble("score");
                                                    ExamResult examResult = new ExamResult(roomId, isCompleted, score);
                                                    examIds.add(examResult);
                                                }
                                                Student student = new Student(idStudent, name, className, examIds);
                                                Intent intent = new Intent(getApplicationContext(), ExamActivity.class);
                                                intent.putExtra("studentIntent", (Serializable) student);
                                                startActivity(intent);
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    };
                    connect.socket.on("server-send-login", loginEmit);
                }
            }
        });

    }
}
