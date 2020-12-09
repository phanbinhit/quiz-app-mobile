package com.example.bkquizapp.adapter;

import android.content.Context;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        RadioButton[] radioButtons;
        CheckBox[] checkBoxes;
        CheckBox cb;
        RadioGroup group = null;
            ViewHolderMain viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_row_main, parent, false);
                viewHolder = new ViewHolderMain();
                viewHolder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question_main);
                viewHolder.layoutInner = (LinearLayout) convertView.findViewById(R.id.layout_inner);
                if (question.getQuestion().getType().equals(Type.NORMAL)) {
                    radioButtons = new RadioButton[question.getAnswers().size()];
                    group = new RadioGroup(context);
                    group.setId(View.generateViewId());
                    viewHolder.layoutInner.addView(group);
                    for (int i = 0; i < radioButtons.length; i++) {
                        radioButtons[i] = new RadioButton(context);
                        radioButtons[i].setChecked(false);
                        radioButtons[i].setText(question.getAnswers().get(i));
                        radioButtons[i].setId(position + 100 + i);
                        radioButtons[i].setTextSize(16);
                        group.addView(radioButtons[i]);
                    }
                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            selectedMap.replace(position, checkedId + "");
                        }
                    });
                }

                if (question.getQuestion().getType().equals(Type.CHECKBOX)) {
                    checkBoxes = new CheckBox[question.getAnswers().size()];
                    for (int i = 0; i < checkBoxes.length; i++) {
                        cb = new CheckBox(context);
                        cb.setChecked(false);
                        cb.setText(question.getAnswers().get(i));
                        cb.setTag(Integer.valueOf(position + i));
                        cb.setTextSize(16);
                        //cb.setOnCheckedChangeListener(listener);
                        viewHolder.layoutInner.addView(cb);
                    }
                }

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderMain) convertView.getTag();
            }
            viewHolder.tvQuestion.setText((position + 1) + ". " + question.getQuestion().getTitle());
            if (question.getQuestion().getType().equals(Type.NORMAL)) {

            }

            if (question.getQuestion().getType().equals(Type.CHECKBOX)){

            }

        return convertView;
    }

    public class ViewHolderMain {
        TextView tvQuestion;
        //RadioGroup group;
        LinearLayout layoutInner;
    }

}




















