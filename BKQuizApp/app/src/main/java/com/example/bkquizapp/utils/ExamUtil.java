package com.example.bkquizapp.utils;

import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExamUtil {
    private ExamUtil() {

    }

    public static List<Exam> changeExamJsonArrayToListExam(JSONArray examEmitJSONArray) throws JSONException {
        List<Exam> exams = new ArrayList<>();
        for (int i = 0; i < examEmitJSONArray.length(); i++) {
            JSONObject examJSON = examEmitJSONArray.getJSONObject(i);
            String title = examJSON.getString("title");
            String roomId = examJSON.getString("roomId");
            long time = examJSON.getLong("time");
            JSONArray questionsJson = examJSON.getJSONArray("questions");
            List<Question> questions = new ArrayList<>();
            for (int k = 0; k < questionsJson.length(); k++) {
                String questionStr = questionsJson.getJSONObject(k).getString("question");
                JSONArray answersJson = questionsJson.getJSONObject(k).getJSONArray("answers");
                String rightAnswer = questionsJson.getJSONObject(k).getString("rightAnswer");
                List<String> answers = new ArrayList<>();
                for (int j = 0; j < answersJson.length(); j++) {
                    answers.add(answersJson.getString(j));
                }
                Question question = new Question(questionStr, answers, rightAnswer);
                questions.add(question);
            }
            Exam exam = new Exam(roomId, title, questions, time);
            exams.add(exam);
        }
        return exams;
    }

    public static List<ExamResult> changeExamResultJsonArrayToListExamResult(JSONArray examResultJsonArray) throws JSONException {
        List<ExamResult> examResults = new ArrayList<>();
        for (int i = 0; i < examResultJsonArray.length(); i++) {
            JSONObject jsonObject = examResultJsonArray.getJSONObject(i);
            String roomId = jsonObject.getString("roomId");
            boolean isCompleted = jsonObject.getBoolean("isCompleted");
            String time = jsonObject.getString("time");
            double score = jsonObject.getDouble("score");
            ExamResult examResult = new ExamResult(roomId, isCompleted, score, time);
            examResults.add(examResult);
        }
        return examResults;
    }
}
