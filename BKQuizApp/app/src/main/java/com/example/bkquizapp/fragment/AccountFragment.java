package com.example.bkquizapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bkquizapp.R;
import com.example.bkquizapp.activity.LoginActivity;
import com.example.bkquizapp.manager.SessionManagement;
import com.example.bkquizapp.model.Student;
import com.example.bkquizapp.utils.ActivityUtil;
import com.example.bkquizapp.utils.Connect;

public class AccountFragment extends Fragment {
    private Activity activity;
    private TextView tvAccountName;
    private TextView tvAccountClass;
    private TextView tvAccountId;
    private Button btnLogout;
    private Connect connect;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        activity = ActivityUtil.getActivity(view);
        Student student = (Student) getArguments().getSerializable("student");
        connect = new Connect();

        tvAccountId = (TextView) view.findViewById(R.id.frag_tv_id);
        tvAccountClass = (TextView) view.findViewById(R.id.frag_tv_class);
        tvAccountName = (TextView) view.findViewById(R.id.frag_tv_name);
        btnLogout = (Button) view.findViewById(R.id.btn_logout);

        tvAccountName.setText(student.getName());
        tvAccountId.setText(student.getId());
        tvAccountClass.setText(student.getClassName());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(activity);
                sessionManagement.removeSession();
                connect.socket.disconnect();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }
}
