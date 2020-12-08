package com.example.bkquizapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bkquizapp.activity.MainActivity;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.R;
import com.example.bkquizapp.model.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Exam> exams;
    private Student student;
    public CustomAdapter(Context context, int resource, ArrayList<Exam> exams, Student student) {
        super(context, resource, exams);
        this.context = context;
        this.resource = resource;
        this.exams = exams;
        this.student = student;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvExam = (TextView) convertView.findViewById(R.id.tv_exam);
            viewHolder.btnStartExam = (Button) convertView.findViewById(R.id.btn_start_exam);
            viewHolder.btnStartExam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("student", (Serializable) student);
                    intent.putExtra("exam", (Serializable) exams.get(position));
                    context.startActivity(intent);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Exam exam = exams.get(position);
        viewHolder.tvExam.setText(exam.getTitle());
        return convertView;
    }

    public static class ViewHolder {
        TextView tvExam;
        Button btnStartExam;
    }
}
