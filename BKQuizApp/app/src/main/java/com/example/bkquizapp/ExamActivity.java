package com.example.bkquizapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.stream.Collectors;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ExamActivity extends AppCompatActivity {
    private TextView tvId, tvName;
    private ListView lvExams;
    private CustomAdapter adapter;
    private List<Exam> exams = new ArrayList<>();
    private Connect connect;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        tvId = (TextView) findViewById(R.id.tv_id);
        tvName = (TextView) findViewById(R.id.tv_name);
        lvExams = (ListView) findViewById(R.id.lv_exam);
        Student student = (Student) getIntent().getSerializableExtra("studentIntent");

        tvId.setText(student.getId());
        tvName.setText(student.getName());

        connect = new Connect();

        List<String> ids = student.getExamIds().stream()
                .filter(exam -> exam.isCompleted() == false)
                .map(exam -> exam.getRoomId())
                .collect(Collectors.toList());
        JSONArray idJSONArray = new JSONArray();
        ids.forEach(id -> {
            idJSONArray.put(id);
        });

        connect.socket.emit("id-not-complete", idJSONArray);
            Emitter.Listener examEmitter = new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject examEmitJSON = (JSONObject) args[0];
                            Exam exam;
                            try {
                                JSONArray examEmitJSONArray = examEmitJSON.getJSONArray("exams");
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
                                    exam = new Exam(roomId, title, questions, time);
                                    exams.add(exam);
                                }
                                adapter = new CustomAdapter(ExamActivity.this, R.layout.activity_row, (ArrayList<Exam>) exams, student);
                                lvExams.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };
        connect.socket.on("exam-not-complete", examEmitter);
    }
}
