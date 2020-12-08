//package com.example.bkquizapp.activity;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bkquizapp.R;
//import com.example.bkquizapp.adapter.CustomAdapterExamAdmin;
//import com.example.bkquizapp.model.Admin;
//import com.example.bkquizapp.model.Exam;
//import com.example.bkquizapp.model.Question;
//import com.example.bkquizapp.utils.Connect;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.socket.emitter.Emitter;
//
//public class AdminActivity extends AppCompatActivity {
//    private TextView tvIdAdmin;
//    private TextView tvNameAdmin;
//    private ListView lvExamAdmin;
//    private CustomAdapterExamAdmin adapter;
//    private List<Exam> exams = new ArrayList<>();
//    private Connect connect;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin);
//
//        tvIdAdmin = (TextView) findViewById(R.id.tv_id_admin);
//        tvNameAdmin = (TextView) findViewById(R.id.tv_name_admin);
//        lvExamAdmin = (ListView) findViewById(R.id.lv_exam_admin);
//        Admin admin = (Admin) getIntent().getSerializableExtra("admin");
//        tvIdAdmin.setText(admin.getId());
//        tvNameAdmin.setText(admin.getName());
//        connect = new Connect();
//
//        connect.socket.emit("admin-request-exam");
//        Emitter.Listener examEmitter = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject examEmitJSON = (JSONObject) args[0];
//                        Exam exam;
//                        try {
//                            JSONArray examEmitJSONArray = examEmitJSON.getJSONArray("exams");
//                            for (int i = 0; i < examEmitJSONArray.length(); i++) {
//                                JSONObject examJSON = examEmitJSONArray.getJSONObject(i);
//                                String title = examJSON.getString("title");
//                                String roomId = examJSON.getString("roomId");
//                                long time = examJSON.getLong("time");
//                                JSONArray questionsJson = examJSON.getJSONArray("questions");
//                                List<Question> questions = new ArrayList<>();
//                                for (int k = 0; k < questionsJson.length(); k++) {
//                                    String questionStr = questionsJson.getJSONObject(k).getString("question");
//                                    JSONArray answersJson = questionsJson.getJSONObject(k).getJSONArray("answers");
//                                    String rightAnswer = questionsJson.getJSONObject(k).getString("rightAnswer");
//                                    List<String> answers = new ArrayList<>();
//                                    for (int j = 0; j < answersJson.length(); j++) {
//                                        answers.add(answersJson.getString(j));
//                                    }
//                                    Question question = new Question(questionStr, answers, rightAnswer);
//                                    questions.add(question);
//                                }
//                                exam = new Exam(roomId, title, questions, time);
//                                exams.add(exam);
//                            }
//                            adapter = new CustomAdapterExamAdmin(AdminActivity.this, R.layout.activity_row_exam_admin, (ArrayList<Exam>) exams);
//                            lvExamAdmin.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        };
//        connect.socket.on("server-send-exam", examEmitter);
//        lvExamAdmin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                new AlertDialog.Builder(AdminActivity.this)
//                        .setTitle("Bạn muốn xóa bài thi?")
//                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                connect.socket.emit("admin-request-delete", exams.get(position).getRoomId());
//                                exams.remove(position);
//                                adapter.notifyDataSetChanged();
//                            };
//                        })
//                        .show();
//
//                return true;
//            }
//        });
//    }
//}
