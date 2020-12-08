package com.example.bkquizapp.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.bkquizapp.R;
import com.example.bkquizapp.common.Type;
import com.example.bkquizapp.model.Exam;
import com.example.bkquizapp.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomAdapterMain  extends ArrayAdapter {

    private Context context;
    private int resource;
    private List<Question> questions;
    private Map<Integer, String> selectedMap;

    public CustomAdapterMain(Context context, int resource, List<Question> questions, Map<Integer, String> selectedMap) {
        super(context, resource, questions);
        this.context = context;
        this.resource = resource;
        this.questions = questions;
        this.selectedMap = selectedMap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = questions.get(position);

        if (question.getQuestion().getType().equals(Type.NORMAL)) {
            ViewHolderMain viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_main, parent, false);
                viewHolder = new ViewHolderMain();
                viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question_main);
                viewHolder.rgAnswer = (RadioGroup) convertView.findViewById(R.id.rg_answer);
                viewHolder.rb1 = (RadioButton) convertView.findViewById(R.id.rb1);
                viewHolder.rb2 = (RadioButton) convertView.findViewById(R.id.rb2);
                viewHolder.rb3 = (RadioButton) convertView.findViewById(R.id.rb3);
                viewHolder.rb4 = (RadioButton) convertView.findViewById(R.id.rb4);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderMain) convertView.getTag();
            }

            List<RadioButton> radioButtons = Arrays.asList(viewHolder.rb1, viewHolder.rb2, viewHolder.rb3, viewHolder.rb4);
            viewHolder.tvQuestion.setText((position + 1) + ". " + question.getQuestion().getTitle());
            for (int i = 0; i < question.getAnswers().size(); i++) {
                radioButtons.get(i).setText(question.getAnswers().get(i));
            }
            viewHolder.rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rb1:
                            selectedMap.replace(position, question.getAnswers().get(0));
                            break;
                        case R.id.rb2:
                            selectedMap.replace(position, question.getAnswers().get(1));
                            break;
                        case R.id.rb3:
                            selectedMap.replace(position, question.getAnswers().get(2));
                            break;
                        case R.id.rb4:
                            selectedMap.replace(position, question.getAnswers().get(3));
                            break;
                    }
                }
            });
        }

        if(question.getQuestion().getType().equals(Type.CHECKBOX)) {
            ViewHolderCheckBox viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.row_checkbox, parent, false);
                viewHolder = new ViewHolderCheckBox();
                viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question_main);
                viewHolder.cb1 = (CheckBox) convertView.findViewById(R.id.cb1);
                viewHolder.cb2 = (CheckBox) convertView.findViewById(R.id.cb2);
                viewHolder.cb3 = (CheckBox) convertView.findViewById(R.id.cb3);
                viewHolder.cb4 = (CheckBox) convertView.findViewById(R.id.cb4);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderCheckBox) convertView.getTag();
            }

            List<CheckBox> checkBoxes = Arrays.asList(viewHolder.cb1, viewHolder.cb2, viewHolder.cb3, viewHolder.cb4);
            viewHolder.tvQuestion.setText((position + 1) + ". " + question.getQuestion().getTitle());
            for (int i = 0; i < question.getAnswers().size(); i++) {
                checkBoxes.get(i).setText(question.getAnswers().get(i));
            }
//            viewHolder.rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    switch (checkedId) {
//                        case R.id.rb1:
//                            selectedMap.replace(position, question.getAnswers().get(0));
//                            break;
//                        case R.id.rb2:
//                            selectedMap.replace(position, question.getAnswers().get(1));
//                            break;
//                        case R.id.rb3:
//                            selectedMap.replace(position, question.getAnswers().get(2));
//                            break;
//                        case R.id.rb4:
//                            selectedMap.replace(position, question.getAnswers().get(3));
//                            break;
//                    }
//                }
//            });
        }

        return convertView;
    }

    public int getRow(Type type) {
        int id = 0;
        switch (type) {
            case NORMAL:
                id = R.layout.activity_row_main;
                break;
            case CHECKBOX:
                id = R.layout.row_checkbox;
                break;
            case TRUEFALSE:
                //id = R.layout.activity_row_main;
                break;
            case IMAGE:
                //id = R.layout.activity_row_main;
                break;
        }
        return id;
    }

    public class ViewHolderMain {
        TextView tvQuestion;
        RadioGroup rgAnswer;
        RadioButton rb1;
        RadioButton rb2;
        RadioButton rb3;
        RadioButton rb4;
    }

    public class ViewHolderCheckBox {
        TextView tvQuestion;
        CheckBox cb1;
        CheckBox cb2;
        CheckBox cb3;
        CheckBox cb4;
    }

//    public class ViewHolderTrueFasle {
//        TextView tvQuestion;
//        RadioGroup rgAnswer;
//        RadioButton rb1;
//        RadioButton rb2;
//        RadioButton rb3;
//        RadioButton rb4;
//    }
//
//    public class ViewHolderImage {
//        TextView tvQuestion;
//        RadioGroup rgAnswer;
//        RadioButton rb1;
//        RadioButton rb2;
//        RadioButton rb3;
//        RadioButton rb4;
//    }

}
