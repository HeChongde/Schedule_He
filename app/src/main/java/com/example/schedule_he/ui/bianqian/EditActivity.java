package com.example.schedule_he.ui.bianqian;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schedule_he.BarColor;
import com.example.schedule_he.MainActivity;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    EditText et;
    //private String content;
    //private String time;
    private String old_content;
    private String old_time;
    private int old_Tag;
    private long id=0;
    private int openMode = 0;
    private int tag = 0;
    public Intent intent = new Intent();
    public boolean tagChange = false;

    private Spinner spinner;

    private Toolbar myToolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Side_Menu.night_mode){
            setContentView(R.layout.edit_layout);
            BarColor.setStatusBarColor(EditActivity.this, Color.parseColor("#A09AB4"));
        }
       else{
            setContentView(R.layout.night_edit_layout);
            BarColor.setStatusBarColor(EditActivity.this,Color.parseColor("#3b3b3b"));
        }

        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }

        myToolbar = (Toolbar) findViewById(R.id.edit_Toolbar);
        //不加这行菜单无法显示，告诉fragment我们有菜单的

        // setHasOptionsMenu(true);
        //清理
//        Menu menu = myToolbar.getMenu();
//        menu.clear();
        //加载菜单
        if(!Side_Menu.night_mode){
            myToolbar.inflateMenu(R.menu.bq_edit_menu);
            myToolbar.setNavigationIcon(R.drawable.ic_back_edit_24dp);
        }
        else{
            myToolbar.inflateMenu(R.menu.night_bq_edit_menu);
            myToolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        }
        myToolbar.setTitle("便签详情");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置其点击事件
            @Override
            public void onClick(View v) {
                autoSetMessage();
                setResult(RESULT_OK,intent);
                finish();//结束该活动，回到之前的Activity
            }
        });
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_delete:
                       onEditDeleteClic();
                        break;
                }
                return true;
            }
        });

        spinner=findViewById(R.id.spinner);
        et=findViewById(R.id.et);
        Intent getIntent = getIntent();
        openMode = getIntent.getIntExtra("mode",0);
        //Log.d("he", "mode是"+openMode);
        if (openMode == 3) {//打开已存在的note
            id = getIntent.getLongExtra("id", 0);
            old_content = getIntent.getStringExtra("content");
            old_time = getIntent.getStringExtra("time");
            old_Tag = getIntent.getIntExtra("tag", 0);
            spinner.setSelection(old_Tag);
            et.setText(old_content);//打开文本
            et.setSelection(old_content.length());//光标定位到末尾
        }
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               tag=position;
               tagChange=true;
               Log.d("he", "选中了标签序号"+tag);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_HOME){
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){//点击返回键
            autoSetMessage();
            setResult(RESULT_OK,intent);
            finish();//结束该活动，回到之前的Activity
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    public void autoSetMessage(){
        if(openMode == 4){//新建笔记
            //Log.d("he", "进来了");
            if(et.getText().toString().length() == 0){
                intent.putExtra("mode", -1); //-1代表什么也没发生
            }
            else{
                intent.putExtra("mode", 0); // 0代表创建了个新的笔记
                intent.putExtra("content", et.getText().toString());
                intent.putExtra("time", dateToStr());
                intent.putExtra("tag", tag);
            }
        }
        else {//打开已有笔记
            //Log.d("he", "进来了");
            if (et.getText().toString().equals(old_content) && !tagChange)//啥也没改 标签也没变
                intent.putExtra("mode", -1); // edit nothing
            else if(et.getText().toString().length() == 0){
                intent.putExtra("mode",2);//需要删除当前笔记
                intent.putExtra("id",id);
            }
            else {
                intent.putExtra("mode", 1); //mond 变为1  修改内容
                intent.putExtra("content", et.getText().toString());
                intent.putExtra("time", dateToStr());
                intent.putExtra("id", id);
                intent.putExtra("tag", tag);
            }
        }
    }

    public String dateToStr(){//获取时间并规定格式
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public void onEditDeleteClic() {
                Log.d("he", "点击了删除按钮");
                new AlertDialog.Builder(EditActivity.this)
                        .setMessage("删除吗？")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(openMode == 4) {//新笔记
                                    intent.putExtra("mode",-1);
                                    setResult(RESULT_OK,intent);
                                }
                                else{//存在的笔记
                                    intent.putExtra("mode",2);//需要删除当前笔记
                                    intent.putExtra("id",id);
                                    setResult(RESULT_OK,intent);
                                }
                                finish();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();//创建并显示
    }
}
