package com.example.schedule_he.ui.richeng;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Note_RC {
    private long id;//自增长id
    private String title;
    private String content;//内容
    private String time;//最后编辑时间
    private String day;//用于记录该条计划的日期
    private Calendar planTime;



    public Note_RC() {
        this.planTime = Calendar.getInstance();
    }

    public Note_RC(String title, String content, String time, String day) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.day = day;
        setPTime(day+" "+time);
        Log.d("he", "time格式为"+time);
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


    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
//////////////////////////////！！！！！！！！！！！！！！
    public String getDay(){return day;};

    public void setDay(String day){ this.day=day; }


    public int getYear(){
        return planTime.get(Calendar.YEAR);
    }

    public int getMonth(){
        return planTime.get(Calendar.MONTH);
    }

    ////////////！！！！！！！！！！！！！！！！！
    public int _getDay() {
        return planTime.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return planTime.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return planTime.get(Calendar.MINUTE);
    }
    public Calendar getPlanTime() {
        return planTime;
    }
    @Override
    public String toString() {
        return content + "\n" + time.substring(5,16) + " "+ id;
    }

    public String getPTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(planTime.getTime());
    }
    public void setPTime(String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
        try {
            Date temp = simpleDateFormat.parse(format);
            Log.d("shit", ""+temp);
            planTime = Calendar.getInstance();
            planTime.setTime(temp);
        } catch (ParseException e) {

        }
    }

}
