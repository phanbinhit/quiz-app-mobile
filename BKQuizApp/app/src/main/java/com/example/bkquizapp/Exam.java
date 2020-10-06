package com.example.bkquizapp;

import java.io.Serializable;
import java.util.List;

public class Exam implements Serializable {

    private String roomId;
    private String title;
    private List<Question> questions;
    private long time;

    public Exam() {

    }

    public Exam(String roomId, String title, List<Question> questions, long time) {
        this.roomId = roomId;
        this.title = title;
        this.questions = questions;
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
