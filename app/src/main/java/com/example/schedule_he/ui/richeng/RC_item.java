package com.example.schedule_he.ui.richeng;

public class RC_item {

    /** 时间 */
    private String time;
    /** 标题*/
    private String title;

    public RC_item() {
    }

    public RC_item(String time, String title) {
        this.time = time;
        this.title = title;
    }
    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
