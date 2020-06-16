package com.example.schedule_he.ui.bianqian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabase extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "notes";//定义我的数据库名字
    public static final String CONTENT = "content";//我的数据库内容名字
    public static final String ID = "_id"; //主键id
    public static final String TIME = "time";
    public static final String MODE = "mode";

    public NoteDatabase(Context context){//传入上下文环境
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//创建一个表
        db.execSQL("CREATE TABLE "+ TABLE_NAME
                + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"//id 为主键
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL,"
                + MODE + " INTEGER DEFAULT 1)"//TAG 默认标签为1
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//用于升级
        /*for(int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    break;
                case 2:
                    updateMode(db);
                default:
                    break;
            }
        }*/
    }
}
