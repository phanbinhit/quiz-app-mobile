package com.example.bkquizapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bkquizapp.R;
import com.example.bkquizapp.manager.SessionManagement;
import com.example.bkquizapp.model.Admin;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Student;
import com.example.bkquizapp.utils.Connect;
import com.example.bkquizapp.utils.StudentUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity {
    private EditText edtId, edtPassword;
    private Button btnLogin;
    private Connect connect;
    private Student student;
    private String id;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

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
                id = edtId.getText().toString();
                String password = edtPassword.getText().toString();
                JSONObject loginObj = new JSONObject();
                if (id.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Nhập mssv và mật khẩu", Toast.LENGTH_SHORT).show();
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
                                        if (serverLogin.getBoolean("hasAccount") == false) {
                                            Toast.makeText(LoginActivity.this, "Sinh viên không tồn tại", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (serverLogin.getBoolean("isRightPass") == false) {
                                                Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                            } else {
                                                JSONObject studentJson = serverLogin.getJSONObject("user");
                                                student = StudentUtils.changeStudentJsonObjToStudentObj(studentJson);
                                                SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                                sessionManagement.saveSession(student);
                                                moveExamActivity();
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

    @Override
    protected void onStart() {
        super.onStart();
        //check if student is logged in
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        id = sessionManagement.getSession();
        if (!id.equals("-1")) {
            moveExamActivity();
        }
    }

    private void moveExamActivity() {
        Intent intent = new Intent(getApplicationContext(), ExamActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
