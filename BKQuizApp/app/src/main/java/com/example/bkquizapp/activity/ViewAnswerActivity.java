package com.example.bkquizapp.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bkquizapp.R;
import com.example.bkquizapp.adapter.AnswerAdapter;
import com.example.bkquizapp.adapter.QuestionAdapter;
import com.example.bkquizapp.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewAnswerActivity extends AppCompatActivity {
    private RecyclerView lvAnswer;
    private AnswerAdapter adapter;
    private List<Question> questions;
    private Map<Integer, String> selectedMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);

        questions = (List<Question>) getIntent().getSerializableExtra("questions");
        selectedMap = (Map<Integer, String>) getIntent().getSerializableExtra("selectedMap");

        lvAnswer = (RecyclerView) findViewById(R.id.lv_answer);
        adapter = new AnswerAdapter(getApplicationContext(), questions, selectedMap);
        lvAnswer.setAdapter(adapter);
        lvAnswer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
