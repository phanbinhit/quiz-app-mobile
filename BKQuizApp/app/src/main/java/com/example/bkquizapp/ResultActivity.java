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

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ResultActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvClass, tvExam, tvScore;
    private Button btnViewResult;
    private Button btnStartAgain;
    private Socket socket;
    private Student student;
    private Exam exam;
    private String score;
    private String numberQuestion;
    private String numberRight;
    private static final String URI_SERVER = new Address().getAddressV4();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        onBackPressed();

        tvId = (TextView) findViewById(R.id.tv_id_result);
        tvName = (TextView) findViewById(R.id.tv_name_result);
        tvClass = (TextView) findViewById(R.id.tv_class_result);
        tvExam = (TextView) findViewById(R.id.tv_exam_result);
        tvScore = (TextView) findViewById(R.id.tv_score_result);
        btnViewResult = (Button) findViewById(R.id.btn_view_result);
        btnStartAgain = (Button) findViewById(R.id.btn_again);

        Intent intentRs = getIntent();
        student = (Student) intentRs.getSerializableExtra("student");
        exam = (Exam) intentRs.getSerializableExtra("exam");
        score = intentRs.getStringExtra("score");
        numberQuestion = intentRs.getStringExtra("numberQuestion");
        numberRight = intentRs.getStringExtra("numberRight");

        //connect to server
        try {
            socket= IO.socket(URI_SERVER);
        } catch (URISyntaxException e) {
            Log.v("AvisActivity", "error connecting to socket");
            Toast.makeText(ResultActivity.this, "Server is not ready", Toast.LENGTH_SHORT).show();
        }

        socket.connect();

        JSONObject resultJSON = new JSONObject();
        try {
            resultJSON.put("idStudent", student.getId());
            resultJSON.put("roomId", exam.getRoomId());
            resultJSON.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit("client-send-result", resultJSON);

        tvId.setText(student.getId());
        tvName.setText(student.getName());
        tvClass.setText(student.getClassName());
        tvExam.setText(exam.getTitle());
        tvScore.setText(score + " (" + numberRight +"/"+ numberQuestion + ")");

        btnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Question> questions = exam.getQuestions();
                Intent intent = new Intent(v.getContext(), ViewAnswerActivity.class);
                intent.putExtra("questions", (Serializable) questions);
                startActivity(intent);
            }
        });

        btnStartAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.disconnect();
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "You have completed the test", Toast.LENGTH_SHORT).show();
    }
}
