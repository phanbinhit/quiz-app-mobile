//package com.example.bkquizapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.example.bkquizapp.model.Exam;
//import com.example.bkquizapp.R;
//import com.example.bkquizapp.model.Student;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomAdapterExamAdmin extends ArrayAdapter {
//    private Context context;
//    private int resource;
//    private List<Exam> exams;
//    private Student student;
//    public CustomAdapterExamAdmin(Context context, int resource, ArrayList<Exam> exams) {
//        super(context, resource, exams);
//        this.context = context;
//        this.resource = resource;
//        this.exams = exams;
//        this.student = student;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolderExamAdmin viewHolder;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_exam_admin, parent, false);
//            viewHolder = new ViewHolderExamAdmin();
//            viewHolder.tvExam = (TextView) convertView.findViewById(R.id.tv_exam);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolderExamAdmin) convertView.getTag();
//        }
//        Exam exam = exams.get(position);
//        viewHolder.tvExam.setText(exam.getTitle());
//        return convertView;
//    }
//
//    public static class ViewHolderExamAdmin {
//        TextView tvExam;
//    }
//}
