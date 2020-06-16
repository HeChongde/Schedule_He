package com.example.schedule_he.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schedule_he.BarColor;
import com.example.schedule_he.MainActivity;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.kecheng.AddCourseActivity;
import com.example.schedule_he.ui.richeng.RC_xiangxiActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {

    Toolbar myToolbar;
    Button registerBtn;
    Button login;
    TextView uname;
    TextView pword;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Side_Menu.night_mode){
            setContentView(R.layout.activity_login);
        }
        else{
            setContentView(R.layout.night_activity_login);
        }
        if(!Side_Menu.night_mode){
            BarColor.setStatusBarColor(LoginActivity.this, Color.parseColor("#A09AB4"));
        }
        else{
            BarColor.setStatusBarColor(LoginActivity.this,Color.parseColor("#3b3b3b"));
        }
        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }
        myToolbar = (Toolbar) findViewById(R.id.login_Toolbar);
        if(!Side_Menu.night_mode){
            myToolbar.setNavigationIcon(R.drawable.ic_back_edit_24dp);
        }
        else{
            myToolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);
        }
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置其点击事件
            @Override
            public void onClick(View v) {
                finish();//结束该活动，回到之前的Activity
            }
        });

        registerBtn = findViewById(R.id.go_resister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                //finish();//结束该活动
            }
        });
        preferences=LoginActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);

        uname = findViewById(R.id.et_name);
        pword = findViewById(R.id.et_password);

        login = findViewById(R.id.go_login);

        BmobDBHelper.getInstance().init(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uname.getText().toString().equals("")||pword.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请完善账号密码信息",Toast.LENGTH_SHORT).show();
                }
                else{
                    String s=uname.getText().toString();
                    String p=pword.getText().toString();
                    checkByName(s,p);
                }

            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_HOME){
            return true;
        }
        else if(keyCode==KeyEvent.KEYCODE_BACK){//点击返回键
            finish();//结束该活动，回到之前的Activity
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    /**单个查询  用户信息**/
    public void checkByName(String uname,String pw) {

        BmobQuery<User> query = new BmobQuery<User>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", uname);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {

                    if(object.size()>0){
                        if(object.get(0).getPassword().equals(pw)){
                            SharedPreferences.Editor editor = preferences.edit();//保存到本地
                            editor.putString("username",uname);
                            editor.commit();
                            Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                            //finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this,"失败：" + e.getMessage() + "," + e.getErrorCode(),Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

}
