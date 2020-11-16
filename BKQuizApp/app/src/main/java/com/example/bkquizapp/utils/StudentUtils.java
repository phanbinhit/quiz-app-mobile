package com.example.bkquizapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.bkquizapp.activity.ExamActivity;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentUtils {
    private StudentUtils() {

    }

    public static Student changeStudentJsonObjToStudentObj(JSONObject studentJson) throws JSONException {
        String idUser = studentJson.getString("idUser");
        String name = studentJson.getString("name");
        String className = studentJson.getString("className");
        JSONArray examArrayJson = studentJson.getJSONArray("exams");
        List<ExamResult> examIds = new ArrayList<>();
        for (int i = 0; i < examArrayJson.length(); i++) {
            String roomId = examArrayJson.getJSONObject(i).getString("roomId");
            boolean isCompleted = examArrayJson.getJSONObject(i).getBoolean("isCompleted");
            double score = examArrayJson.getJSONObject(i).getDouble("score");
            String time = examArrayJson.getJSONObject(i).getString("time");
            ExamResult examResult = new ExamResult(roomId, isCompleted, score, time);
            examIds.add(examResult);
        }
        Student student = new Student(idUser, name, className, examIds);
        return student;
    }
}
