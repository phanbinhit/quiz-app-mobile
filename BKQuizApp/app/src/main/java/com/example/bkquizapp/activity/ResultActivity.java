package com.example.bkquizapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bkquizapp.R;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.Question;
import com.example.bkquizapp.model.Student;
import com.example.bkquizapp.utils.Connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvClass, tvExam, tvScore;
    private Button btnViewResult;
    private Button btnStartAgain;
    private Student student;
    private Exam exam;
    private String score;
    private String numberQuestion;
    private String numberRight;
    private Connect connect;

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        //get curent date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime current = LocalDateTime.now();
        String timeCompleted = formatter.format(current);

        connect = new Connect();

        JSONObject resultJSON = new JSONObject();
        try {
            resultJSON.put("idStudent", student.getId());
            resultJSON.put("roomId", exam.getRoomId());
            resultJSON.put("score", score);
            resultJSON.put("time", timeCompleted);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connect.socket.emit("client-send-result", resultJSON);

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
                startActivity(new Intent(v.getContext(), ExamActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Bạn đã hoàn thành bài thi", Toast.LENGTH_SHORT).show();
    }
}
