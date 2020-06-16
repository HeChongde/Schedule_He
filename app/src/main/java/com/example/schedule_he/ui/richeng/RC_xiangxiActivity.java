package com.example.schedule_he.ui.richeng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.schedule_he.BarColor;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;

import java.util.ArrayList;
import java.util.List;


public class RC_xiangxiActivity extends AppCompatActivity {

    private RecyclerView rvTrace;
    private List<RC_item> traceList = new ArrayList<>(10);
    private RC_ItemAdapter adapter;
    public Intent intent_rc = new Intent();
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Side_Menu.night_mode){
            setContentView(R.layout.activiti_kc_xx);
        }
        else{
            setContentView(R.layout.night_activity_kc_xx);
        }
        if(!Side_Menu.night_mode){
            BarColor.setStatusBarColor(RC_xiangxiActivity.this, Color.parseColor("#A09AB4"));
        }
        else{
            BarColor.setStatusBarColor(RC_xiangxiActivity.this,Color.parseColor("#3b3b3b"));
        }

        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }
        toolbar = (Toolbar) findViewById(R.id.rc_list_Toolbar);
        if(!Side_Menu.night_mode){
            toolbar.setNavigationIcon(R.drawable.ic_back_edit_24dp);
        }
        else{
            toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置其点击事件
            @Override
            public void onClick(View v) {
                intent_rc.putExtra("mode", -1); //-1代表什么也没发生
                setResult(RESULT_OK,intent_rc);
                finish();//结束该活动，回到之前的Activity
            }
        });

        findView();
        initData();



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
            intent_rc.putExtra("mode", -1); //-1代表什么也没发生
            setResult(RESULT_OK,intent_rc);
            finish();//结束该活动，回到之前的Activity
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void findView() {
        rvTrace = (RecyclerView) findViewById(R.id.rvRC);
    }

    private void initData() {
        // 模拟一些假的数据
        DBop_rc op = new DBop_rc(this);
        op.open();
        List<Note_RC> list = op.getAllNotes();
        op.close();

        for(int i=0;i<list.size();i++){
            String day=list.get(i).getDay();
            String time = list.get(i).getTime();
            String fomat_day=day.substring(0,4)+"年 "+day.substring(4,6)+"月 "+day.substring(6,8)+"日"+time;
            traceList.add(new RC_item(fomat_day, list.get(i).getTitle()));
        }

       // traceList.add(new RC_item("2016-05-23 18:35:32", "[杭州市] [杭州乔司区]的市场部已收件 电话:18358xxxxxx"));

        adapter = new RC_ItemAdapter(this, traceList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);
    }
}
