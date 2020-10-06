package com.example.bkquizapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswerActivity extends AppCompatActivity {
    private ListView lvAnswer;
    private CustomAdaperAnswer adapter;
    private List<Question> questions = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);

        questions = (List<Question>) getIntent().getSerializableExtra("questions");

        lvAnswer = (ListView) findViewById(R.id.lv_answers);
        adapter = new CustomAdaperAnswer(ViewAnswerActivity.this, R.layout.activity_row_answer, (ArrayList<Question>) questions);
        lvAnswer.setAdapter(adapter);
    }
}
