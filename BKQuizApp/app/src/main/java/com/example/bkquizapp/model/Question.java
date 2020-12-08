package com.example.bkquizapp.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Question implements Serializable {
    private Ask question;
    private Map<Integer, String> answers;
    private String rightAnswer;

    public Question() {

    }

    public Question(Ask question, Map<Integer, String> answers, String rightAnswer) {
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }

    public Ask getQuestion() {
        return question;
    }

    public void setQuestion(Ask question) {
        this.question = question;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
