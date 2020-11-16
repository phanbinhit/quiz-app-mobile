package com.example.bkquizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bkquizapp.model.Question;
import com.example.bkquizapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdaperAnswer extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Question> questions;

    public CustomAdaperAnswer(Context context, int resource, ArrayList<Question> questions) {
        super(context, resource, questions);
        this.context = context;
        this.resource = resource;
        this.questions = questions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderAnswer viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_answer, parent, false);
            viewHolder = new ViewHolderAnswer();
            viewHolder.tvQuestionRs = (TextView) convertView.findViewById(R.id.tv_question_rs);
            viewHolder.tvAnswerRs = (TextView) convertView.findViewById(R.id.tv_answer_rs);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderAnswer) convertView.getTag();
        }
        Question question = questions.get(position);
        viewHolder.tvQuestionRs.setText(question.getQuestion());
        viewHolder.tvAnswerRs.setText(question.getRightAnswer());
        return convertView;
    }

    public class ViewHolderAnswer {
        TextView tvQuestionRs, tvAnswerRs;
    }
}
