package com.example.bkquizapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bkquizapp.model.Student;

public class SessionManagement {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final String SHARED_PREF_NAME = "session";
    private final String SESSION_KEY = "session_key_user";

    public SessionManagement(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void saveSession(Student student) {
        editor.putString(SESSION_KEY, student.getId()).commit();
    }

    public String getSession() {
        return sharedPreferences.getString(SESSION_KEY, "-1");
    }

    public void removeSession() {
        editor.putString(SESSION_KEY, "-1").commit();
    }
}
