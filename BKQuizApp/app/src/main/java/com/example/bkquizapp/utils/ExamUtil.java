package com.example.bkquizapp.utils;

import com.example.bkquizapp.common.Type;
import com.example.bkquizapp.model.Ask;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExamUtil {
    private ExamUtil() {

    }

    public static List<Exam> changeExamJsonArrayToListExam(JSONArray examEmitJSONArray) throws JSONException {
        List<Exam> exams = new ArrayList<>();
        Question question = null;
        for (int i = 0; i < examEmitJSONArray.length(); i++) {
            JSONObject examJSON = examEmitJSONArray.getJSONObject(i);
            String title = examJSON.getString("title");
            String roomId = examJSON.getString("roomId");
            long time = examJSON.getLong("time");
            JSONArray questionsJson = examJSON.getJSONArray("questions");
            List<Question> questions = new ArrayList<>();
            for (int k = 0; k < questionsJson.length(); k++) {
                JSONObject askJson = questionsJson.getJSONObject(k).getJSONObject("ask");
                String titleQuestion = askJson.getString("title");
                Type type = getType(askJson.getInt("type"));
                Ask ask;
                if (type.equals(Type.IMAGE)) {
                    String path = askJson.getString("path");
                    ask = new Ask(title, type, path);
                } else {
                    ask = new Ask(titleQuestion, type);
                }
                JSONArray answersJson = questionsJson.getJSONObject(k).getJSONArray("answers");
                Map<Integer, String> answers = new LinkedHashMap<>();
                for (int j = 0; j < answersJson.length(); j++) {
                    answers.put(j, answersJson.getString(j));
                }
                String rightAnswer = questionsJson.getJSONObject(k).getString("rightAnswer");
                question = new Question(ask, answers, rightAnswer);
                questions.add(question);
            }
            Exam exam = new Exam(roomId, title, questions, time);
            exams.add(exam);
        }
        return exams;
    }

    public static Type getType(int i) {
        Type type = null;
        switch (i) {
            case 0:
                type = Type.NORMAL;
                break;
            case 1:
                type = Type.CHECKBOX;
                break;
            case 2:
                type = Type.IMAGE;
                break;
        }
        return type;
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
