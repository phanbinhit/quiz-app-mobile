package com.example.bkquizapp.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bkquizapp.R;
import com.example.bkquizapp.fragment.AccountFragment;
import com.example.bkquizapp.fragment.ExamFragment;
import com.example.bkquizapp.fragment.HistoryFragment;
import com.example.bkquizapp.manager.SessionManagement;
import com.example.bkquizapp.model.Student;
import com.example.bkquizapp.utils.Connect;
import com.example.bkquizapp.utils.StudentUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class ExamActivity extends AppCompatActivity {
    private Student student;
    private BottomNavigationView bottomNavigationView;
    private Bundle bundle;
    private Connect connect;
    private Fragment selectedFragment;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        SessionManagement sessionManagement = new SessionManagement(ExamActivity.this);
        String id = sessionManagement.getSession();
        connect = new Connect();
        JSONObject object = new JSONObject();
        try {
            object.put("idStudent", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle = new Bundle();
        Emitter.Listener StudentEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject serverStudent = (JSONObject) args[0];
                        try {
                            JSONObject studentJson = serverStudent.getJSONObject("user");
                            student = StudentUtils.changeStudentJsonObjToStudentObj(studentJson);
                            bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
                            if (savedInstanceState == null) {
                                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        connect.socket.emit("client-send-id-student", object);
        connect.socket.on("server-responsive-user", StudentEmitter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new ExamFragment();
                            break;
                        case R.id.nav_history:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    bundle.putSerializable("student", student);
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commitAllowingStateLoss();
                    return true;
                }
            };


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Đăng xuất để thoát", Toast.LENGTH_SHORT).show();
    }
}
