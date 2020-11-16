package com.example.bkquizapp.model;

import java.io.Serializable;

public class ExamResult implements Serializable {
    private String roomId;
    private boolean isCompleted;
    private double score;
    private String time;
    public ExamResult() {

    }
    public ExamResult(String roomId, boolean isCompleted, double score, String time) {
        this.roomId = roomId;
        this.isCompleted = isCompleted;
        this.score = score;
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
