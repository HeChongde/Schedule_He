package com.example.schedule_he.ui.richeng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class DBop_rc {
    SQLiteOpenHelper dbHandler;//数据库处理器
    SQLiteDatabase db;

    private static final String[] columns = {//把NodeDB每一列找出来做成个数组，便于操作
            NoteBD_RC.ID,
            NoteBD_RC.TITLE,
            NoteBD_RC.CONTENT,
            NoteBD_RC.TIME,
            NoteBD_RC.DAY
    };

    public DBop_rc(Context context){
        dbHandler = new NoteBD_RC(context);//把DBhandler指向我们的数据库NoteDB
    }

    public void open(){
        db = dbHandler.getWritableDatabase();//打开写入模式
    }

    public void close(){
        dbHandler.close();
    }

    //把note 加入到database里面   //参数为一个数据Node结点
    public Note_RC addNote(Note_RC note){
        //ContentValues专门处理数据
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteBD_RC.TITLE, note.getTitle());
        contentValues.put(NoteBD_RC.CONTENT, note.getContent());//把结点中的content值放入到我们数据库的CONTENT中
        contentValues.put(NoteBD_RC.TIME, note.getTime());
        contentValues.put(NoteBD_RC.DAY, note.getDay());
        long insertId = db.insert(NoteBD_RC.TABLE_NAME, null, contentValues);//自增长的id加入
        note.setId(insertId);//把Node中的id设为数据库中的id
        Log.d("he1", "插入了"+note.getContent());
        return note;
    }

    public Note_RC getNote(long id){//通过id获取Node
        Log.d("hehehe", "找到了找到了11111"+id);
        List<Note_RC> notes = new ArrayList<>();
        notes=getAllNotes();
        Log.d("hehehe", "找到了999"+notes.size());
        for(Note_RC note_rc:notes){
            if(note_rc.getId()==id){
                Log.d("hehehe", "找到了找到了");
                return note_rc;
            }
            Log.d("hehehe", "找到了找到了222"+note_rc.getId());
        }
        Log.d("hehehe", "没找到");
        return null;
    }

    //获取数据库中的所有内容
    public List<Note_RC> getAllNotes(){
        Cursor cursor = db.query(NoteBD_RC.TABLE_NAME,columns,null,null,null, null, "day,time");
        List<Note_RC> notes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Note_RC note = new Note_RC();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteBD_RC.ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NoteBD_RC.TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteBD_RC.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteBD_RC.TIME)));
                note.setDay(cursor.getString(cursor.getColumnIndex(NoteBD_RC.DAY)));
                note.setPTime(note.getDay()+" "+note.getTime());
                notes.add(note);
            }
        }
        return notes;
    }
    public List<Note_RC> getAllDayNotes(String day){

        Cursor cursor = db.query(NoteBD_RC.TABLE_NAME,columns,NoteBD_RC.DAY + "=" + day,null,null, null, "time");
        List<Note_RC> notes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Note_RC note = new Note_RC();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteBD_RC.ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NoteBD_RC.TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteBD_RC.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteBD_RC.TIME)));
                note.setDay(cursor.getString(cursor.getColumnIndex(NoteBD_RC.DAY)));
                notes.add(note);
            }
        }
        return notes;
    }

    public int updateNote(Note_RC note) {//更新 更改
        //update the info of an existing note
        ContentValues values = new ContentValues();
        values.put(NoteBD_RC.TITLE, note.getTitle());
        values.put(NoteBD_RC.CONTENT, note.getContent());
        values.put(NoteBD_RC.TIME, note.getTime());
        values.put(NoteBD_RC.DAY, note.getDay());
        // updating row
        return db.update(NoteBD_RC.TABLE_NAME, values,
                NoteBD_RC.ID + "=?",new String[] { String.valueOf(note.getId())});
    }

    public void removeNote(Note_RC note) {//删除
        //remove a note according to ID value
        db.delete(NoteBD_RC.TABLE_NAME, NoteBD_RC.ID + "=" + note.getId(), null);
    }



}
