package com.example.bkquizapp;

import java.io.Serializable;

public class ExamResult implements Serializable {
    private String roomId;
    private boolean isCompleted;
    private double score;
    public ExamResult() {

    }
    public ExamResult(String roomId, boolean isCompleted, double score) {
        this.roomId = roomId;
        this.isCompleted = isCompleted;
        this.score = score;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
