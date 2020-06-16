package com.example.schedule_he.ui.bianqian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBop {//操作的是我们的数据库
    SQLiteOpenHelper dbHandler;//数据库处理器
    SQLiteDatabase db;

    private static final String[] columns = {//把NodeDB每一列找出来做成个数组，便于操作
            NoteDatabase.ID,
            NoteDatabase.CONTENT,
            NoteDatabase.TIME,
            NoteDatabase.MODE
    };

    public DBop(Context context){
        dbHandler = new NoteDatabase(context);//把DBhandler指向我们的数据库NoteDB
    }

    public void open(){
        db = dbHandler.getWritableDatabase();//打开写入模式
    }

    public void close(){
        dbHandler.close();
    }

    //把note 加入到database里面   //参数为一个数据Node结点
    public Note addNote(Note note){
        //ContentValues专门处理数据
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT, note.getContent());//把结点中的content值放入到我们数据库的CONTENT中
        contentValues.put(NoteDatabase.TIME, note.getTime());
        contentValues.put(NoteDatabase.MODE, note.getTag());
        long insertId = db.insert(NoteDatabase.TABLE_NAME, null, contentValues);//自增长的id加入
        note.setId(insertId);//把Node中的id设为数据库中的id
        Log.d("he1", "插入了"+note.getContent());
        return note;
    }

    public Note getNote(long id){//通过id获取Node
        //cursor是一行的集合   标准写法
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null) cursor.moveToFirst();
        Note e = new Note(cursor.getString(1),cursor.getString(2), cursor.getInt(3));
        return e;
    }

    //获取数据库中的所有内容
    public List<Note> getAllNotes(){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,null,null,null, null, null);

        List<Note> notes = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.MODE)));
                notes.add(note);
            }
        }
        return notes;
    }

    public int updateNote(Note note) {//更新 更改
        //update the info of an existing note
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.CONTENT, note.getContent());
        values.put(NoteDatabase.TIME, note.getTime());
        values.put(NoteDatabase.MODE, note.getTag());
        // updating row
        return db.update(NoteDatabase.TABLE_NAME, values,
                NoteDatabase.ID + "=?",new String[] { String.valueOf(note.getId())});
    }


    public void removeNote(Note note) {//删除
        //remove a note according to ID value
        db.delete(NoteDatabase.TABLE_NAME, NoteDatabase.ID + "=" + note.getId(), null);
    }


}
