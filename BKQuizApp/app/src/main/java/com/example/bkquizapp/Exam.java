package com.example.bkquizapp;

import java.util.List;

public class Exam {

    private String roomId;
    private List<Question> questions;
    private long time;

    public Exam() {

    }

    public Exam(String roomId, List<Question> questions, long time) {
        this.roomId = roomId;
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
}
