package com.example.schedule_he.ui.kecheng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schedule_he.BarColor;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.richeng.RC_xiangxiActivity;

public class AddCourseActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Side_Menu.night_mode){
            setContentView(R.layout.activity_add_course);
        }
        else{
            setContentView(R.layout.night_activity_add_course);
        }
        if(!Side_Menu.night_mode){
            BarColor.setStatusBarColor(AddCourseActivity.this, Color.parseColor("#A09AB4"));
        }
        else{
            BarColor.setStatusBarColor(AddCourseActivity.this,Color.parseColor("#3b3b3b"));
        }
        setFinishOnTouchOutside(false);

        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }

        final EditText inputCourseName = (EditText) findViewById(R.id.course_name);
        final EditText inputTeacher = (EditText) findViewById(R.id.teacher_name);
        final EditText inputClassRoom = (EditText) findViewById(R.id.class_room);
        final EditText inputDay = (EditText) findViewById(R.id.week);
        final EditText inputStart = (EditText) findViewById(R.id.classes_begin);
        final EditText inputEnd = (EditText) findViewById(R.id.classes_ends);

        toolbar = (Toolbar) findViewById(R.id.add_courses_Toolbar);
        if(!Side_Menu.night_mode){
            toolbar.setNavigationIcon(R.drawable.ic_back_edit_24dp);
        }
        else{
            toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置其点击事件
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button okButton = (Button) findViewById(R.id.button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();

                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {
                    Course course = new Course(courseName, teacher, classRoom,
                            Integer.valueOf(day), Integer.valueOf(start), Integer.valueOf(end), getWeekString());

                    Log.d("he", "周的字符串为"+getWeekString());
                    Intent intent = new Intent(AddCourseActivity.this, NotificationsFragment.class);
                    intent.putExtra("course", course);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    public String getWeekString(){//周的拼接字符串

       CheckBox checkBox1 = findViewById(R.id.check_1);
       CheckBox checkBox2 = findViewById(R.id.check_2);
        CheckBox checkBox3 = findViewById(R.id.check_3);
        CheckBox checkBox4 = findViewById(R.id.check_4);
        CheckBox checkBox5 = findViewById(R.id.check_5);
        CheckBox checkBox6 = findViewById(R.id.check_6);
        CheckBox checkBox7 = findViewById(R.id.check_7);
        CheckBox checkBox8 = findViewById(R.id.check_8);
        CheckBox checkBox9 = findViewById(R.id.check_9);
        CheckBox checkBox10 = findViewById(R.id.check_10);
        CheckBox checkBox11 = findViewById(R.id.check_11);
        CheckBox checkBox12 = findViewById(R.id.check_12);
        CheckBox checkBox13 = findViewById(R.id.check_13);
        CheckBox checkBox14 = findViewById(R.id.check_14);
        CheckBox checkBox15 = findViewById(R.id.check_15);
        CheckBox checkBox16 = findViewById(R.id.check_16);
        CheckBox checkBox17 = findViewById(R.id.check_17);
        CheckBox checkBox18 = findViewById(R.id.check_18);
        CheckBox checkBox19 = findViewById(R.id.check_19);
        CheckBox checkBox20 = findViewById(R.id.check_20);

        String s=get_bool_w(checkBox1)+get_bool_w(checkBox2)+get_bool_w(checkBox3)+get_bool_w(checkBox4)+get_bool_w(checkBox5)
                +get_bool_w(checkBox6)+get_bool_w(checkBox7)+get_bool_w(checkBox8)+get_bool_w(checkBox9)+get_bool_w(checkBox10)
                +get_bool_w(checkBox11)+get_bool_w(checkBox12)+get_bool_w(checkBox13)+get_bool_w(checkBox14)+get_bool_w(checkBox15)
                +get_bool_w(checkBox16)+get_bool_w(checkBox17)+get_bool_w(checkBox18)+get_bool_w(checkBox19)+get_bool_w(checkBox20);

        return s;
    }
    public String get_bool_w(CheckBox checkBox){//根据是否选中，生成字符0/1
        if(checkBox.isChecked()){
            return "1";
        }
        else{
            return "0";
        }
    }



}
