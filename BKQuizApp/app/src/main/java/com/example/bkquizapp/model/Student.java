package com.example.bkquizapp.model;

import com.example.bkquizapp.model.ExamResult;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable {
    private String id;
    private String name;
    private String className;
    private List<ExamResult> examIds;
    public Student() {

    }

    public Student(String id, String name, String className, List<ExamResult> examIds) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.examIds = examIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ExamResult> getExamIds() {
        return examIds;
    }

    public void setExamIds(List<ExamResult> examIds) {
        this.examIds = examIds;
    }
}
