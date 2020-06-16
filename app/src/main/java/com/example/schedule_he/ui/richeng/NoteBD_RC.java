package com.example.schedule_he.ui.richeng;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteBD_RC extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "notes_rc";//定义我的数据库名字
    public static final String TITLE = "title_rc";
    public static final String CONTENT = "content_rc";//我的数据库内容名字
    public static final String ID = "_id_rc"; //主键id
    public static final String TIME = "time";
    public static final String DAY = "day";//记录事件日期

    //注意参数！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
    public NoteBD_RC(Context context){//传入上下文环境
        super(context, "notes_rc", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) {//创建一个表
        db1.execSQL("CREATE TABLE "+ TABLE_NAME
                + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"//id 为主键
                + TITLE + " TEXT NOT NULL,"
                + CONTENT + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL,"
                + DAY +" TEXT NOT NULL)"
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
