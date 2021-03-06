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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private Context context;
    private List<Question> questions;
    private Map<Integer, String> selectedMap;
    private Set<Integer> scrollingSet = new HashSet<>();
    private Map<Integer, Set<Integer>> checkedMap = new HashMap<>();

    public QuestionAdapter(Context context, List<Question> questions, Map<Integer, String> selectedMap) {
        super();
        this.context = context;
        this.questions = questions;
        this.selectedMap = selectedMap;
        setHasStableIds(true);
        initCheckedMap(checkedMap);
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

    public void initCheckedMap(Map<Integer, Set<Integer>> checkedMap) {
        for(int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getQuestion().getType().equals(Type.CHECKBOX)) {
                checkedMap.put(i, new HashSet<>());
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        if (scrollingSet.contains(position)) {
            return;
        }
        scrollingSet.add(position);
        Question question = questions.get(position);
        RadioButton[] radioButtons = new RadioButton[question.getAnswers().size()];
        CheckBox[] checkBoxes;
        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectedMap.replace(position, checkedId + "");
            }
        });
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectedMap.replace(position, getChecked(checkedMap, isChecked, position, buttonView));
            }
        };
        viewHolder.tvQuestion.setText("Câu " + (position + 1) + ": "+ question.getQuestion().getTitle());
        if (question.getQuestion().getType().equals(Type.NORMAL)) {
            createRadioButton(radioButtons, question, viewHolder);
        }
        if (question.getQuestion().getType().equals(Type.CHECKBOX)) {
            checkBoxes = new CheckBox[question.getAnswers().size()];
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i] = new CheckBox(context);
                checkBoxes[i].setChecked(false);
                checkBoxes[i].setText(question.getAnswers().get(i));
                checkBoxes[i].setTextSize(16);
                checkBoxes[i].setId(i);
                checkBoxes[i].setOnCheckedChangeListener(listener);
                viewHolder.layoutInner.addView(checkBoxes[i]);
            }
        }
        if (question.getQuestion().getType().equals(Type.IMAGE)) {
            ImageView imageView = new ImageView(context);
            Picasso.get().load(question.getQuestion().getPath()).into(imageView);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setId(position);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.layoutImage.addView(imageView);
            createRadioButton(radioButtons, question, viewHolder);
        }
    }

    public void createRadioButton(RadioButton[] radioButtons, Question question, ViewHolder viewHolder) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(context);
            radioButtons[i].setChecked(false);
            radioButtons[i].setText(question.getAnswers().get(i));
            radioButtons[i].setId(i);
            radioButtons[i].setTextSize(16);
            viewHolder.radioGroup.addView(radioButtons[i]);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestion;
        LinearLayout layoutInner;
        RadioGroup radioGroup;
        LinearLayout layoutImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question_main);
            layoutInner = itemView.findViewById(R.id.layout_inner);
            radioGroup = itemView.findViewById(R.id.rg_quesion);
            layoutImage = itemView.findViewById(R.id.layout_image);
        }
    }
}
