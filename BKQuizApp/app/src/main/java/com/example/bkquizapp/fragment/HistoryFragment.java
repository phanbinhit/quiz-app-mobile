package com.example.bkquizapp.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.bkquizapp.utils.Connect;
import com.example.bkquizapp.adapter.CustomAdapterHistory;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.R;
import com.example.bkquizapp.model.ExamResult;
import com.example.bkquizapp.model.Student;
import com.example.bkquizapp.utils.ActivityUtil;
import com.example.bkquizapp.utils.ExamUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.socket.emitter.Emitter;

public class HistoryFragment extends Fragment {
    private Activity activity;
    private ListView lvExamHistory;
    private CustomAdapterHistory adapter;
    private List<Exam> exams;
    private List<ExamResult> examResults;
    private Student student;
    private Connect connect;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        activity = ActivityUtil.getActivity(view);
        lvExamHistory = (ListView) view.findViewById(R.id.lv_history);
        student = (Student) getArguments().getSerializable("student");
        connect = new Connect();

        List<String> ids = student.getExamIds().stream()
                .filter(exam -> exam.isCompleted() == true)
                .map(exam -> exam.getRoomId())
                .collect(Collectors.toList());
        JSONArray idJSONArray = new JSONArray();
        ids.forEach(id -> {
            idJSONArray.put(id);
        });
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idStudent", student.getId());
            jsonObject.put("ids", idJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        connect.socket.emit("id-not-complete", jsonObject);
        Emitter.Listener examEmitter = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject examEmitJSON = (JSONObject) args[0];
                        try {
                            JSONArray examEmitJSONArray = examEmitJSON.getJSONArray("exams");
                            JSONArray examResultJsonArray = examEmitJSON.getJSONArray("examResults");
                            examResults = ExamUtil.changeExamResultJsonArrayToListExamResult(examResultJsonArray);
                            exams = ExamUtil.changeExamJsonArrayToListExam(examEmitJSONArray);
                            adapter = new CustomAdapterHistory(activity, R.layout.activity_row_history, (ArrayList<Exam>) exams, (ArrayList<ExamResult>) examResults);
                            lvExamHistory.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        connect.socket.on("exam-not-complete", examEmitter);
        return view;
    }
}
