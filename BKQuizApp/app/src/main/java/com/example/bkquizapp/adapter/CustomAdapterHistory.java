package com.example.bkquizapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.R;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Student;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterHistory extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Exam> exams;
    private List<ExamResult> examResults;
    public CustomAdapterHistory(Context context, int resource, ArrayList<Exam> exams, ArrayList<ExamResult> examResults) {
        super(context, resource, exams);
        this.context = context;
        this.resource = resource;
        this.exams = exams;
        this.examResults = examResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderHistory viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_history, parent, false);
            viewHolder = new ViewHolderHistory();
            viewHolder.tvHistoryExam = (TextView) convertView.findViewById(R.id.tv_history_exam);
            viewHolder.tvHistoryScore = (TextView) convertView.findViewById(R.id.tv_history_score);
            viewHolder.tvHistoryTime = (TextView) convertView.findViewById(R.id.tv_history_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderHistory) convertView.getTag();
        }
        Exam exam = exams.get(position);
        ExamResult examResult = examResults.get(position);
        viewHolder.tvHistoryExam.setText(exam.getTitle());
        viewHolder.tvHistoryScore.setText("Điểm: " + examResult.getScore());
        viewHolder.tvHistoryTime.setText("Hoàn thành: " + examResult.getTime());
        return convertView;
    }

    public static class ViewHolderHistory {
        TextView tvHistoryExam;
        TextView tvHistoryScore;
        TextView tvHistoryTime;
    }
}
