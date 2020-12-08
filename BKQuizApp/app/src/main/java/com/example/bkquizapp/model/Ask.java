package com.example.bkquizapp.model;

import com.example.bkquizapp.common.Type;

import java.io.Serializable;

public class Ask implements Serializable {
    private String title;
    private Type type;
    private String path;
    public Ask() {

    }
    public Ask(String title, Type type) {
        this.title = title;
        this.type = type;
    }
    public Ask(String title, Type type, String path) {
        this.title = title;
        this.type = type;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
