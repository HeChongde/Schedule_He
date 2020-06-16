package com.example.schedule_he.ui.Cloud;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.schedule_he.ui.bianqian.DBop;
import com.example.schedule_he.ui.bianqian.Note;
import com.example.schedule_he.ui.bianqian.NoteDatabase;
import com.example.schedule_he.ui.kecheng.Course;
import com.example.schedule_he.ui.kecheng.DatabaseHelper;
import com.example.schedule_he.ui.richeng.AlarmReceiver;
import com.example.schedule_he.ui.richeng.DBop_rc;
import com.example.schedule_he.ui.richeng.NoteBD_RC;
import com.example.schedule_he.ui.richeng.Note_RC;
import com.example.schedule_he.ui.user.LoginActivity;
import com.example.schedule_he.ui.user.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Load {


    private NoteDatabase dbHelper;
    private NoteBD_RC dbHelper_rc ;
    private DatabaseHelper databaseHelper;
    private AlarmManager alarmManager;

    public void download(Context context,String username,Fragment fragment){
        clear_date_offline(context,fragment);//清空本地三个数据
        read_date_online(username,context);//读取云端数据并放到本地
    }

    public void upload(Context context,String username){
        clear_date_online(username);//清空云端三个数据
        read_date_offline(context,username);//读取本地数据并一一上传
    }

    /**
     * 清空本地数据库
     * **/
    public void clear_date_offline(Context context, Fragment fragment){
        //清空便签
        dbHelper = new NoteDatabase(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("notes", null, null);
        db.execSQL("update sqlite_sequence set seq=0 where name='notes'");

        //清空日程
        alarmManager = (AlarmManager) fragment.getActivity().getSystemService(Context.ALARM_SERVICE);
        DBop_rc op = new DBop_rc(context);
        op.open();
        cancelAlarms(op.getAllNotes(),context);//删除所有闹钟
        op.close();

        dbHelper_rc = new NoteBD_RC(context);
        SQLiteDatabase db_rc = dbHelper_rc.getWritableDatabase();
        db_rc.delete("notes_rc", null, null);
        db_rc.execSQL("update sqlite_sequence set seq=0 where name='notes_rc'");

        //清空课程
        databaseHelper = new DatabaseHelper
                (context, "database.db", null, 1);
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("courses", null, null);
        sqLiteDatabase.execSQL("update sqlite_sequence set seq=0 where name='courses'");
    }
    /**
     * 清空云端数据库（用户名）
     * **/
    public void clear_date_online(String username){
        //批量搜索50条
        //批量删除50条
        //直到搜索出的数为0
        checkList_Note(username);
        checkList_Note_rc(username);
        checkList_Courses(username);
    }

    /**
     * 读取本地数据库  3个
     * **/
    public void read_date_offline(Context context,String username){
        List<Note> noteList = new ArrayList<Note>();
        DBop op = new DBop(context);
        op.open();
        noteList =op.getAllNotes();
        op.close();

        for(Note note:noteList){
            Notes notes = new Notes();
            notes.setUsername(username);
            notes.setContent(note.getContent());
            notes.setTime(note.getTime());
            notes.setMode(note.getTag());
            insert(notes);
        }

        ///////////
        List<Note_RC> noteList_rc = new ArrayList<Note_RC>();
        DBop_rc op_rc = new DBop_rc(context);
        op_rc.open();
        noteList_rc=op_rc.getAllNotes();
        op_rc.close();

        for(Note_RC note:noteList_rc){
            Notes_rc notes1 = new Notes_rc();
            notes1.setUsername(username);
            notes1.setTitle(note.getTitle());
            notes1.setContent(note.getContent());
            notes1.setDay(note.getDay());
            notes1.setTime(note.getTime());
            insert(notes1);
        }


        /////////////////
        databaseHelper = new DatabaseHelper
                (context, "database.db", null, 1);
        ArrayList<Course> coursesList1 = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
        //Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        Cursor cursor = sqLiteDatabase.query("courses",null,null,null,null, null,null );
        if (cursor.moveToFirst()) {
            do {
                coursesList1.add(new Course(
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
        for(Course cours:coursesList1){
            Courses cc = new Courses();
            cc.setUsername(username);
            cc.setCourse_name(cours.getCourseName());
            cc.setTeacher(cours.getTeacher());
            cc.setClass_room(cours.getClassRoom());
            cc.setDay(cours.getDay());
            cc.setClass_start(cours.getStart());
            cc.setClass_end(cours.getEnd());
            cc.setWeek(cours.getWeek());
            insert(cc);
        }
    }
    /**
     * 读取云端数据库  按用户名称查找 并放到本地
     * **/
    public void read_date_online(String username,Context context){
        BmobQuery<Notes> query = new BmobQuery<Notes>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Notes>() {
            @Override
            public void done(List<Notes> object, BmobException e) {
                if(e==null){
                    for (Notes notes : object) {
                        Note newNote = new Note(notes.getContent(), notes.getTime(), notes.getMode());
                        DBop op = new DBop(context);
                        op.open();
                        op.addNote(newNote);
                        op.close();
                    }
                    if(object.size()==500){
                        Toast.makeText(context,"数据太多，无法下载",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
///////////////////日程部分
        BmobQuery<Notes_rc> query_rc = new BmobQuery<Notes_rc>();
        //查询playerName叫“比目”的数据
        query_rc.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query_rc.setLimit(500);
        //执行查询方法
        query_rc.findObjects(new FindListener<Notes_rc>() {
            @Override
            public void done(List<Notes_rc> object, BmobException e) {
                if(e==null){
                    for (Notes_rc notes_rc : object) {
                        Note_RC newNote = new Note_RC(notes_rc.getTitle(),notes_rc.getContent(),notes_rc.getTime(),notes_rc.getDay());
                        DBop_rc op_rc = new DBop_rc(context);
                        op_rc.open();
                        op_rc.addNote(newNote);
                        op_rc.close();
                    }
                    if(object.size()==500){
                        Toast.makeText(context,"数据太多，无法下载",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        ///////////////////课程部分
        BmobQuery<Courses> query_kc = new BmobQuery<Courses>();
        //查询playerName叫“比目”的数据
        query_kc.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query_kc.setLimit(500);
        //执行查询方法
        query_kc.findObjects(new FindListener<Courses>() {
            @Override
            public void done(List<Courses> object, BmobException e) {
                if(e==null){
                    for (Courses kc : object) {
                        Course course = new Course(kc.getCourse_name(), kc.getTeacher(), kc.getClass_room(),
                                kc.getDay(), kc.getClass_start(),kc.getClass_end(), kc.getWeek());
                        saveData(course);
                    }
                    if(object.size()==500){
                        Toast.makeText(context,"数据太多，无法下载",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    //保存数据到数据库(课程)
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
    /**
     * 插入本地数据库
     * **/
    public void insert_date_offline(){

    }

    /**
     * 批量删除50条
     * **/
    public void deleteList(List<BmobObject> list){
            //注：泛型要写BmobObject，不能写Person
            new BmobBatch().deleteBatch(list).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> o, BmobException e) {
                    if(e==null){
                        for(int i=0;i<o.size();i++){
                            BatchResult result = o.get(i);
                            BmobException ex =result.getError();
                            if(ex==null){
                                //LogUtil.e("第"+i+"个数据批量删除成功");
                            }else{
                                //LogUtil.e("第"+i+"个数据批量删除失败："+ex.getMessage()+","+ex.getErrorCode());
                            }
                        }
                    }else{
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
    }
    /**删除整条数据**/
    public void deleteObject_Course(String objectId){
        Courses courses=new Courses();
        courses.setObjectId(objectId);
        courses.setObjectId(objectId);
        courses.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    // LogUtil.e(MainActivity.class,"===删除成功===");
                }else{
                    // LogUtil.e(MainActivity.class,"删除失败："+e.getMessage()+","+e.getErrorCode());
                    Log.d("hehe", "检测Bomb运行位置啊啊啊啊嗄222222"+"删除失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**删除整条数据**/
    public void deleteObject_Note(String objectId){
        Notes notes = new Notes();
        notes.setObjectId(objectId);
        notes.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    // LogUtil.e(MainActivity.class,"===删除成功===");
                    Log.d("hehe", "检测Bomb运行位置啊啊啊啊嗄11111");
                }else{
                    // LogUtil.e(MainActivity.class,"删除失败："+e.getMessage()+","+e.getErrorCode());
                    Log.d("hehe", "检测Bomb运行位置啊啊啊啊嗄222222"+"删除失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**删除整条数据**/
    public void deleteObject_Notes_Rc(String objectId){
        Notes_rc notes_rc = new Notes_rc();
        notes_rc.setObjectId(objectId);
        notes_rc.setObjectId(objectId);
        notes_rc.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    // LogUtil.e(MainActivity.class,"===删除成功===");
                }else{
                    // LogUtil.e(MainActivity.class,"删除失败："+e.getMessage()+","+e.getErrorCode());
                    Log.d("hehe", "检测Bomb运行位置啊啊啊啊嗄222222"+"删除失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**
     * 批量查找
     *
     * **/
    public void checkList_Note(String username){
        BmobQuery<Notes> query = new BmobQuery<Notes>();
        query.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Notes>() {
            @Override
            public void done(List<Notes> object, BmobException e) {
                if(e==null){
                   if(object.size()>0){
                       for(Notes notes:object){
                           deleteObject_Note(notes.getObjectId());

                       }
                       //checkList_Note(username);
                   }

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**
     * 批量查找
     *
     * **/
    public void checkList_Note_rc(String username){
        BmobQuery<Notes_rc> query = new BmobQuery<Notes_rc>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Notes_rc>() {
            @Override
            public void done(List<Notes_rc> object, BmobException e) {
                if(e==null){
                    if(object.size()>0){
                        for(Notes_rc notes:object){
                            deleteObject_Notes_Rc(notes.getObjectId());
                        }
                        //checkList_Note(username);
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
    /**
     * 批量查找
     *
     * **/
    public void checkList_Courses(String username){
        BmobQuery<Courses> query = new BmobQuery<Courses>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", username);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Courses>() {
            @Override
            public void done(List<Courses> object, BmobException e) {
                if(e==null){
                    if(object.size()>0){
                        for(Courses notes:object){
                            deleteObject_Course(notes.getObjectId());
                        }
                        //checkList_Note(username);
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    //添加单条数据
    public void insert(BmobObject ob){
        boolean b;
        ob.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    //添加成功
                    //LogUtil.e(RegisterActivity.class,"========objectId="+objectId);
                    Log.d("he", "插入正常"+e.getMessage());
                }else{
                    //添加失败
                    //LogUtil.e(RegisterActivity.class,"========e="+e.getErrorCode()+"   errorMessage="+e.getMessage());
                    Log.d("he", "插入异常"+e.getMessage()+"错误其他信息"+e.getErrorCode());
                }
            }
        });
    }
    //取消提醒
    private void cancelAlarm(Note_RC p,Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)p.getId(), intent, 0);
        alarmManager.cancel(pendingIntent);

    }

    //取消很多提醒
    private void cancelAlarms(List<Note_RC> plans,Context context){
        for(Note_RC note_rc : plans){
            cancelAlarm(note_rc,context);
        }
    }

}
