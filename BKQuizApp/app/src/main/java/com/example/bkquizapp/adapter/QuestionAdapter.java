package com.example.bkquizapp.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bkquizapp.R;
import com.example.bkquizapp.common.Type;
import com.example.bkquizapp.model.Question;
import com.squareup.picasso.Picasso;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context context;
    private List<Question> questions;
    private Map<Integer, String> selectedMap;
    private Map<Integer, Set<Integer>> checkedMap = new HashMap<>();

    public QuestionAdapter(Context context, List<Question> questions, Map<Integer, String> selectedMap) {
        super();
        this.context = context;
        this.questions = questions;
        this.selectedMap = selectedMap;
        setHasStableIds(true);
        initMapChecked(checkedMap);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            Question question = questions.get(position);
            RadioButton[] radioButtons;
            CheckBox[] checkBoxes;
            RadioGroup group = null;
            //viewHolder.setIsRecyclable(false);
            viewHolder.tvQuestion.setText(question.getQuestion().getTitle());
            if (question.getQuestion().getType().equals(Type.NORMAL)) {
                radioButtons = new RadioButton[question.getAnswers().size()];
                group = new RadioGroup(context);
                group.setTag(Integer.valueOf(position));
                group.setId(position);
                viewHolder.layoutInner.addView(group);
                for (int i = 0; i < radioButtons.length; i++) {
                    radioButtons[i] = new RadioButton(context);
                    radioButtons[i].setChecked(false);
                    radioButtons[i].setText(question.getAnswers().get(i));
                    radioButtons[i].setId(i);
                    radioButtons[i].setTag(position + i + "");
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
                Log.d("checkbox", position + "");
                checkBoxes = new CheckBox[question.getAnswers().size()];
                CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        selectedMap.replace(position, getChecked(checkedMap, isChecked, position, buttonView));
                    }
                };
                for (int i = 0; i < checkBoxes.length; i++) {
                    checkBoxes[i] = new CheckBox(context);
                    checkBoxes[i].setChecked(false);
                    checkBoxes[i].setText(question.getAnswers().get(i));
                    checkBoxes[i].setTag(position + i + "");
                    checkBoxes[i].setTextSize(16);
                    checkBoxes[i].setId(i);
                    checkBoxes[i].setOnCheckedChangeListener(listener);
                    viewHolder.layoutInner.addView(checkBoxes[i]);
                }
            }
            if(question.getQuestion().getType().equals(Type.IMAGE)) {
                ImageView imageView = new ImageView(context);
                Picasso.get().load(question.getQuestion().getPath()).into(imageView);
                ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setId(position);
                viewHolder.layoutInner.addView(imageView);
                radioButtons = new RadioButton[question.getAnswers().size()];
                group = new RadioGroup(context);
                group.setTag(Integer.valueOf(position));
                group.setId(position);
                viewHolder.layoutInner.addView(group);
                for (int i = 0; i < radioButtons.length; i++) {
                    radioButtons[i] = new RadioButton(context);
                    radioButtons[i].setChecked(false);
                    radioButtons[i].setText(question.getAnswers().get(i));
                    radioButtons[i].setId(i);
                    radioButtons[i].setTag(position + i + "");
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
    }

    public void initMapChecked(Map<Integer, Set<Integer>> checkedMap) {
        for (int i = 0; i < getItemCount(); i++) {
            checkedMap.put(i, new HashSet<>());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getChecked(Map<Integer, Set<Integer>> checkedMap, boolean isChecked, int position, CompoundButton buttonView) {
        String result = "";
        for (Map.Entry<Integer, Set<Integer>> entry: checkedMap.entrySet()) {
            if(entry.getKey().equals(position)) {
                if (isChecked) {
                    entry.getValue().add(buttonView.getId());
                } else {
                    entry.getValue().remove(buttonView.getId());
                }
                result = entry.getValue().stream().sorted().map(i -> String.valueOf(i)).collect(Collectors.joining(", "));
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        LinearLayout layoutInner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question_main);
            layoutInner = itemView.findViewById(R.id.layout_inner);
        }
    }
}
