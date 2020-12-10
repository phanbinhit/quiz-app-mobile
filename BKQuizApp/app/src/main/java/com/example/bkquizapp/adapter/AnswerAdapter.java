package com.example.bkquizapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bkquizapp.R;
import com.example.bkquizapp.common.Type;
import com.example.bkquizapp.model.Question;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnswerAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context context;
    private List<Question> questions;
    private Map<Integer, String> selectedMap;
    private Set<Integer> scrollingSet = new HashSet<>();
    private Map<Integer, Set<Integer>> checkedMap = new HashMap<>();
    private ColorStateList tintRed;
    private ColorStateList tintGreen;

    public AnswerAdapter(Context context, List<Question> questions, Map<Integer, String> selectedMap) {
        super();
        this.context = context;
        this.questions = questions;
        this.selectedMap = selectedMap;
        setHasStableIds(true);
        tintRed = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        Color.BLACK //disabled
                        , context.getResources().getColor(R.color.tintRed) //enabled
                }
        );

        tintGreen = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[] {
                        Color.BLACK //disabled
                        , context.getResources().getColor(R.color.tintGreen) //enabled
                }
        );
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
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_row_main, parent, false);
        return new QuestionAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder viewHolder, int position) {
        if (scrollingSet.contains(position)) {
            return;
        }
        scrollingSet.add(position);

        Question question = questions.get(position);
        RadioButton[] radioButtons = new RadioButton[question.getAnswers().size()];
        CheckBox[] checkBoxes;

        viewHolder.tvQuestion.setText("Câu " + (position + 1) + ": "+ question.getQuestion().getTitle());
        if (question.getQuestion().getType().equals(Type.NORMAL)) {
            createRadioButton(radioButtons, question, viewHolder, position);
        }
        if (question.getQuestion().getType().equals(Type.CHECKBOX)) {
            checkBoxes = new CheckBox[question.getAnswers().size()];
            List<String> answers = Arrays.asList(question.getRightAnswer().split(", "));
            List<String> selected = Arrays.asList(selectedMap.get(position).split(", "));
            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i] = new CheckBox(context);
                checkBoxes[i].setChecked(false);
                if(selected.contains(i + "")) {
                    checkBoxes[i].setChecked(true);
                    checkBoxes[i].setButtonTintList(tintRed);
                }
                if (answers.contains(i + "")) {
                    checkBoxes[i].setChecked(true);
                    checkBoxes[i].setButtonTintList(tintGreen);
                }
                checkBoxes[i].setText(question.getAnswers().get(i));
                checkBoxes[i].setTextSize(16);
                checkBoxes[i].setId(i);
                checkBoxes[i].setClickable(false);
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
            createRadioButton(radioButtons, question, viewHolder, position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createRadioButton(RadioButton[] radioButtons, Question question, QuestionAdapter.ViewHolder viewHolder, int position) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new RadioButton(context);
            radioButtons[i].setChecked(false);
            radioButtons[i].setText(question.getAnswers().get(i));
            radioButtons[i].setId(i);
            if(selectedMap.get(position).equals(i + "")) {

                radioButtons[i].setChecked(true);
                radioButtons[i].setButtonTintList(tintRed);
            }
            if(Integer.parseInt(question.getRightAnswer()) == i) {
                radioButtons[i].setChecked(true);
                radioButtons[i].setButtonTintList(tintGreen);
            }
            radioButtons[i].setTextSize(16);
            radioButtons[i].setClickable(false);
            viewHolder.layoutInner.addView(radioButtons[i]);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
