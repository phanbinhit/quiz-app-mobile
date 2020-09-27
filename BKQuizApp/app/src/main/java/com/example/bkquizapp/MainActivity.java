package com.example.bkquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
    private long time;
    private float score = 0;
    private Socket socket;
    private static final String URI_SERVER = "http://192.168.1.6:5000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentUser = getIntent();
        roomId = intentUser.getStringExtra("roomId");
        userName = intentUser.getStringExtra("userName");


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
        tvRoom.setText("Room: " + roomId);
        tvUser.setText("User: "  + userName);

        //connect to server
        try {
            socket= IO.socket(URI_SERVER);
        } catch (URISyntaxException e) {
            Log.v("AvisActivity", "error connecting to socket");
            Toast.makeText(MainActivity.this, "Server is not ready", Toast.LENGTH_SHORT).show();
        }

        socket.connect();

        //get exam from server
        Emitter.Listener getExam = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject examServerJsonObj = (JSONObject) args[0];
                        try {
                            JSONObject examJsonObj = examServerJsonObj.getJSONObject("exam");
                            time = examJsonObj.getInt("time");
                            JSONArray questionsJson = examJsonObj.getJSONArray("questions");
                            questions = new ArrayList<>();
                            for (int i = 0; i < questionsJson.length(); i++) {
                                String questionStr = questionsJson.getJSONObject(i).getString("question");
                                JSONArray answersJson = questionsJson.getJSONObject(i).getJSONArray("answers");
                                String rightAnswer = questionsJson.getJSONObject(i).getString("rightAnswer");
                                List<String> answers = new ArrayList<>();
                                for (int j = 0; j < answersJson.length(); j++) {
                                    answers.add(answersJson.getString(j));
                                }
                                Question question = new Question(questionStr, answers, rightAnswer);
                                questions.add(question);
                            }
                            //set textview countdown
                            new CountDownTimer(time * 1000, 1000) {

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
                                        Toast.makeText(MainActivity.this, "Mark a answer", Toast.LENGTH_SHORT).show();
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        socket.emit("client-request-exam", roomId);
        socket.on("server-send-exam", getExam);

        //override back press
        onBackPressed();
    }

    private void intentResult() {
        Toast.makeText(this, questions.size()+"", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("user", userName);
        intent.putExtra("numberQuestion", questions.size() + "");
        intent.putExtra("numberRight", numberRight + "");
        score = 10 * ((float) numberRight / (float) questions.size());
        intent.putExtra("score", score + "");
        intent.putExtra("roomId", roomId);
        startActivity(intent);
        socket.disconnect();
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
