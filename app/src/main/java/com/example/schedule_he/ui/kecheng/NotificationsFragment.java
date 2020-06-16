package com.example.schedule_he.ui.kecheng;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.richeng.NoteBD_RC;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment{



    //我的添加代码
    //星期几
    private RelativeLayout day;

    //SQLite Helper类
    private DatabaseHelper databaseHelper;

    int WEEK = 0;//本周周数
    ImageView imageView;
    String change_day;//上次修改本周的日子

    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;
    Toolbar myToolbar;
    View root;
    Spinner spinner;

    List<View> viewList1 ;
    List<View> viewList2 ;
    private SharedPreferences preferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {

        //root = inflater.inflate(R.layout.fragment_notifications, container, false);
        if(!Side_Menu.night_mode){
            root = inflater.inflate(R.layout.fragment_notifications, container, false);
        }
        else{
            root = inflater.inflate(R.layout.night_layout_notification, container, false);
        }

        databaseHelper = new DatabaseHelper
                (root.getContext(), "database.db", null, 1);


        //获取今日日期
        Calendar calendar =  Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String the_day = simpleDateFormat.format(calendar.getTime());//今天
        //读取保存的周数
        preferences=getContext().getSharedPreferences("week_location", Context.MODE_PRIVATE);
        WEEK = preferences.getInt("week",0);
        change_day = preferences.getString("change_day",the_day);//上次修改时的日期

        if(calendar.get(Calendar.DAY_OF_WEEK)==2){//周一的时候判断    *如果周一，并且上次设置“为本周”的日子不是今天，则本周数增1
            if(!change_day.equals(the_day)){
                ++WEEK;
                //存储本周周数
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("week",WEEK);
                editor.putString("change_day",the_day);
                editor.commit();
            }
        }

        myToolbar=(Toolbar) root.findViewById(R.id.myToolbar3);
        //不加这行菜单无法显示，告诉fragment我们有菜单的
        setHasOptionsMenu(true);
        //清理
        Menu menu = myToolbar.getMenu();
        menu.clear();
        //加载菜单
        if(!Side_Menu.night_mode){
            myToolbar.inflateMenu(R.menu.toolbar3);
        }
        else{
            myToolbar.inflateMenu(R.menu.night_toolbar3);
        }
        myToolbar.setTitle("课程");
        if(!Side_Menu.night_mode){
            myToolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
        }
        else{
            myToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_courses:
                        Log.d("he", "已经点击了增加课程: ");
                        Intent intent = new Intent(root.getContext(), AddCourseActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.menu_delet:
                        onDeleteAllClic();
                        break;
                }

                return true;
            }
        });
        spinner = root.findViewById(R.id.week_spinner);
        spinner.setSelection(WEEK);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"目前处于第"+(position+1)+"周",Toast.LENGTH_SHORT).show();
                clearView();//清空视图
                //从数据库读取数据
                loadData(date(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Side_Menu side_menu = new Side_Menu(root,this,R.id.notification_layout,3);
        side_menu.initPopupView();
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_menu.showPopUpView();
            }
        });

        imageView = root.findViewById(R.id.kc_location);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WEEK=spinner.getSelectedItemPosition();

                //备注修改日期
                Calendar calendar =  Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                change_day = simpleDateFormat.format(calendar.getTime());

                //存储本周周数
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("week",WEEK);
                editor.putString("change_day",change_day);
                editor.commit();

                Toast.makeText(getContext(),"已经设置第"+(WEEK+1)+"周为当前周",Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }


    private ArrayList<Course>  date(int n){//参数表示周数
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);

        Cursor cursor = sqLiteDatabase.query("courses",null,null,null,null, null,null );
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end")),
                        cursor.getString(cursor.getColumnIndex("week"))
                ));
            } while(cursor.moveToNext());
        }
        cursor.close();

        //找出符合周数的课程
        ArrayList<Course> coursesList2 = new ArrayList<>(); //符合周的课程列表

        for(Course course :coursesList){//遍历，筛选出对应位为1的课程
            String s = course.getWeek();
            String s_week ;
            if(n<20){
                s_week = s.substring(n,n+1);//截取从n之前开始，到n+1之前结束
            }
            else{
                s_week = s.substring(n);//截取最后一位
            }


            if(s_week.equals("1")){
                coursesList2.add(course);
            }
        }

        return coursesList2;
    }



    //从数据库加载数据
    private void loadData(ArrayList<Course> coursesList) {
        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
           viewList1= createLeftView(course);
           viewList2= createItemCourseView(course);
        }
    }

    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end, week) " + "values(?, ?, ?, ?, ?, ?, ?)",
                        new String[] {course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay()+"",
                                course.getStart()+"",
                                course.getEnd()+"",
                                course.getWeek()}
                );
    }

    //创建"第几节数"视图
    private  List<View> createLeftView(Course course) {
        int endNumber = course.getEnd();
        List<View> viewList =new ArrayList<View>();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View view;
                if(!Side_Menu.night_mode){
                    view= View.inflate(getContext(), R.layout.left_view, null);
                }
                else{
                    view= View.inflate(getContext(), R.layout.night_left_view, null);
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout = root.findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
                viewList.add(view);
            }
            maxCoursesNumber = endNumber;
        }
        return viewList;
    }

    //创建单个课程视图
    private  List<View> createItemCourseView(final Course course) {
        int getDay = course.getDay();
        List<View> viewList =new ArrayList<View>();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd()){
            Toast.makeText(root.getContext(), "星期几没写对,或课程结束时间比开始时间还早~~", Toast.LENGTH_LONG).show();
            SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});
        }
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = root.findViewById(dayId);

            int height = 180;

            final View v = View.inflate(getContext(), R.layout.course_card, null); //加载单个课程布局
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
            day.addView(v);
            viewList.add(v);
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    //弹窗警告
                    new AlertDialog.Builder(root.getContext())
                            .setMessage("删除吗？")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  //删除
                                    v.setVisibility(View.GONE);//先隐藏
                                    day.removeView(v);//再移除课程视图
                                    SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
                                    sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});


                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();//创建并显示

//                    v.setVisibility(View.GONE);//先隐藏
//                    day.removeView(v);//再移除课程视图
//                    SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
//                    sqLiteDatabase.execSQL("delete from courses where course_name = ?", new String[] {course.getCourseName()});
                    return true;
                }
            });
        }
        return viewList;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar3, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_courses:
//                Log.d("he", "已经点击了增加课程: ");
//                Intent intent = new Intent(root.getContext(), AddCourseActivity.class);
//                startActivityForResult(intent, 0);
//                break;
//            case R.id.menu_delet:
//                //onDeleteAllClic();
//                break;
//        }
//        return true;
//    }
    private void onDeleteAllClic(){
        new AlertDialog.Builder(root.getContext())
                .setMessage("删除全部吗？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dbHelper = new NoteBD_RC(getContext());
//                        SQLiteDatabase db = dbHelper.getWritableDatabase();
//                        db.delete("notes_rc", null, null);
//                        db.execSQL("update sqlite_sequence set seq=0 where name='notes_rc'");
//                        refreshListViwe(_day);
                        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
                        sqLiteDatabase.delete("courses", null, null);
                        sqLiteDatabase.execSQL("update sqlite_sequence set seq=0 where name='courses'");

                        //清空视图
                        clearView();
                        Toast.makeText(getContext(),"已清空所有课程",Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


    public void clearView(){

        //课程删除
        RelativeLayout day1 = root.findViewById(R.id.monday);
        RelativeLayout day2 = root.findViewById(R.id.tuesday);
        RelativeLayout day3 = root.findViewById(R.id.wednesday);
        RelativeLayout day4 = root.findViewById(R.id.thursday);
        RelativeLayout day5 = root.findViewById(R.id.friday);
        RelativeLayout day6 = root.findViewById(R.id.saturday);
        RelativeLayout day7 = root.findViewById(R.id.weekday);
        day1.removeAllViews();
        day2.removeAllViews();
        day3.removeAllViews();
        day4.removeAllViews();
        day5.removeAllViews();
        day6.removeAllViews();
        day7.removeAllViews();


        currentCoursesNumber=0;//当前课程归0
        maxCoursesNumber=0;//左侧数目归0
        //左侧节数删除
        LinearLayout left_view = root.findViewById(R.id.left_view_layout);
        left_view.removeAllViewsInLayout();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode ==RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //存储数据到数据库
            saveData(course);
            clearView();//清空视图
            //从数据库读取数据
            loadData(date(spinner.getSelectedItemPosition()));

        }
    }

}
