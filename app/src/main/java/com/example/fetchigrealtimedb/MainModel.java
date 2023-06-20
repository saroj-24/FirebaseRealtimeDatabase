package com.example.fetchigrealtimedb;

public class MainModel {

    String name,course,picurl,email;

    MainModel()
    {

    }

    public MainModel(String name, String course, String picurl, String email) {
        this.name = name;
        this.course = course;
        this.picurl = picurl;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
