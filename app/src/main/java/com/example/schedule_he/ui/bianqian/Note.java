package com.example.schedule_he.ui.bianqian;

public class Note {
    private long id;//自增长id
    private String content;//内容
    private String time;//最后编辑时间
    private int tag;//笔记的分类标签(娱乐、学习、工作)

    public Note() {
    }
    public Note(String content, String time, int tag) {
        this.content = content;
        this.time = time;
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getTag() {
        return tag;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return content + "\n" + time.substring(5,16) + " "+ id;
    }


}
