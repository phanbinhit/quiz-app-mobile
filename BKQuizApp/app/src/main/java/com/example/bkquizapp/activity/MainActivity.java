package com.example.bkquizapp.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bkquizapp.R;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.Question;
import com.example.bkquizapp.model.Student;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvRoom, tvUser, tvQuestion, tvCount, tvCountDown;
    private RadioButton rbtnAnswer1, rbtnAnswer2, rbtnAnswer3, rbtnAnswer4;
    private Button btnNextQuestion, btnFinish;
    private RadioGroup radioGroup;
    private int flag = 0;
    private int numberRight = 0;
    private String roomId;
    private String userName;
    private List<Question> questions;
    private CountDownTimer countDownTimer;
    private Student student;
    private Exam exam;
    private long time;
    private float score = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        student = (Student) getIntent().getSerializableExtra("student");
        exam = (Exam) getIntent().getSerializableExtra("exam");
        userName = student.getName();
        roomId = exam.getTitle();
        questions = exam.getQuestions();
        questions.forEach(question -> {
            Collections.shuffle(question.getAnswers());
        });
        Collections.shuffle(questions);
        time = exam.getTime();

        tvRoom = (TextView) findViewById(R.id.tv_room);
        tvUser = (TextView) findViewById(R.id.tv_user);
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        rbtnAnswer1 = (RadioButton) findViewById(R.id.rbtn_answer1);
        rbtnAnswer2 = (RadioButton) findViewById(R.id.rbtn_answer2);
        rbtnAnswer3 = (RadioButton) findViewById(R.id.rbtn_answer3);
        rbtnAnswer4 = (RadioButton) findViewById(R.id.rbtn_answer4);
        btnNextQuestion = (Button) findViewById(R.id.btn_nextQuestion);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        radioGroup = (RadioGroup) findViewById(R.id.rg_question);
        tvCount = (TextView) findViewById(R.id.tv_count);
        tvCountDown = (TextView) findViewById(R.id.tv_countdown);

        //set text textview info
        tvRoom.setText("Exam: " + roomId);
        tvUser.setText("Student: "  + userName);

        //set textview countdown
        countDownTimer = new CountDownTimer(time * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long minute = millisUntilFinished / 1000/ 60;
                long second = millisUntilFinished / 1000 - minute * 60;
                tvCountDown.setText(minute + ":" + second);
            }

            @Override
            public void onFinish() {
                tvCountDown.setText("00:00");
                intentResult();
            }
        }.start();

        //set textview question and answer;
        setTextQuestionAndAnswer();

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Chọn 1 đáp án", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton choseAns = findViewById(radioGroup.getCheckedRadioButtonId());
                    String textChoseAns = choseAns.getText().toString();

                    if (textChoseAns.equals(questions.get(flag).getRightAnswer())) {
                        numberRight++;
                    }

                    flag++;

                    if (flag < questions.size()) {
                        setTextQuestionAndAnswer();
                    } else {
                        intentResult();
                    }
                }

                //clear checked radio button
                radioGroup.clearCheck();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentResult();
            }
        });

        //override back press
        onBackPressed();
    }

    private void intentResult() {
        Intent intent = new Intent(this, ResultActivity.class);
        countDownTimer.cancel();
        intent.putExtra("numberQuestion", questions.size() + "");
        intent.putExtra("numberRight", numberRight + "");
        score = 10 * ((float) numberRight / (float) questions.size());
        intent.putExtra("score", score + "");
        intent.putExtra("student", student);
        intent.putExtra("exam", exam);
        startActivity(intent);
    }

    private void setTextQuestionAndAnswer() {
        tvCount.setText("Question " + (flag + 1) + "/" + questions.size() + ":");
        tvQuestion.setText(questions.get(flag).getQuestion());
        rbtnAnswer1.setText(questions.get(flag).getAnswers().get(0));
        rbtnAnswer2.setText(questions.get(flag).getAnswers().get(1));
        rbtnAnswer3.setText(questions.get(flag).getAnswers().get(2));
        rbtnAnswer4.setText(questions.get(flag).getAnswers().get(3));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Enter finish to end", Toast.LENGTH_SHORT).show();
    }

}