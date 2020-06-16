package com.example.schedule_he.ui.Cloud;

import cn.bmob.v3.BmobObject;

public class Courses extends BmobObject {

    private String username;
    private String course_name;
    private String teacher;
    private String class_room;
    private Integer day;
    private Integer class_start;
    private Integer class_end;
    private String week;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClass_room() {
        return class_room;
    }

    public void setClass_room(String class_room) {
        this.class_room = class_room;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getClass_start() {
        return class_start;
    }

    public void setClass_start(Integer class_start) {
        this.class_start = class_start;
    }

    public Integer getClass_end() {
        return class_end;
    }

    public void setClass_end(Integer class_end) {
        this.class_end = class_end;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }


}
