package com.example.schedule_he.ui.Cloud;

import com.example.schedule_he.ui.bianqian.NoteDatabase;

import cn.bmob.v3.BmobObject;

public class Notes extends BmobObject {
    private String username;
    private String content;
    private String time;
    private Integer mode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
