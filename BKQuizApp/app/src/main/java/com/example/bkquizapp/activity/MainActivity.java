package com.example.bkquizapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bkquizapp.R;
import com.example.bkquizapp.adapter.CustomAdapterMain;
import com.example.bkquizapp.common.Type;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView tvTimeCountDown;
    private Button btnFinish;
    private CountDownTimer countDownTimer;
    private Student student;
    private Exam exam;
    private ListView lvQuestion;
    private CustomAdapterMain adapter;
    private Map<Integer, String> selectedMap = new LinkedHashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get intent
        student = (Student) getIntent().getSerializableExtra("student");
        exam = (Exam) getIntent().getSerializableExtra("exam");

        tvTimeCountDown = (TextView) findViewById(R.id.tv_countdown);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        lvQuestion = (ListView) findViewById(R.id.lv_question);

        //set adapter for listview
        adapter = new CustomAdapterMain(MainActivity.this, R.layout.activity_row_main, exam.getQuestions(), selectedMap);
        lvQuestion.setAdapter(adapter);

        for (int i = 0; i < exam.getQuestions().size(); i++) {
            selectedMap.put(i, "");
        }

        //time count down
        countDownTimer = new CountDownTimer(exam.getTime() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minute = millisUntilFinished / 1000/ 60;
                long second = millisUntilFinished / 1000 - minute * 60;
                tvTimeCountDown.setText(minute + ":" + second);
            }

            @Override
            public void onFinish() {
                tvTimeCountDown.setText("00:00");
                result();
            }
        }.start();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result();
            }
        });
    }

    private void result() {
        for (Map.Entry<Integer, String> entry: selectedMap.entrySet()) {
            Log.d("map", entry.getValue());
            if (entry.getValue().equals("")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Bạn chưa hoàn thành bài thi. Vẫn muốn nộp?")
                        .setPositiveButton("Nộp", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intentResult();
                                adapter.notifyDataSetChanged();
                            };
                        })
                        .setNegativeButton("Tiếp tục làm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return;
            }
        }
        intentResult();
    }

    private void intentResult() {
        int numberRight = 0;
        List<Map.Entry<Integer, String>> entries = new ArrayList<>(selectedMap.entrySet());
        for (int i = 0; i < exam.getQuestions().size(); i++) {
            if (exam.getQuestions().get(i).getRightAnswer() == entries.get(i).getValue()) {
                numberRight++;
            }
        }
        Intent intent = new Intent(this, ResultActivity.class);
        countDownTimer.cancel();
        intent.putExtra("numberQuestion", exam.getQuestions().size() + "");
        intent.putExtra("numberRight", numberRight + "");
        float score = 10 * ((float) numberRight / (float) exam.getQuestions().size());
        intent.putExtra("score", score + "");
        intent.putExtra("student", student);
        intent.putExtra("exam", exam);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Nhấn nộp để kết thúc", Toast.LENGTH_SHORT).show();
    }


//    @Override
//    public void finish() {
//        result();
//        super.finish();
//    }
}
