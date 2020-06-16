package com.example.schedule_he.ui.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schedule_he.BarColor;
import com.example.schedule_he.MainActivity;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Cloud.Load;
import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.richeng.RC_xiangxiActivity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends AppCompatActivity {

    Toolbar myToolbar;

    TextView usernameview;
    TextView passwordview;
    TextView passwordview2;
    RadioGroup radioGroup;
    TextView school;
    TextView myflag;
    Button change_button;
    Button exit_button;

    String oldname;
    String uname="";
    String pword="";
    String pword2="";
    String sex="";
    String sch="";
    String flg="";

    User new_user;
    SharedPreferences user_preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Side_Menu.night_mode){
            setContentView(R.layout.user_info);
        }
        else{
            setContentView(R.layout.night_user_info);
        }
        if(!Side_Menu.night_mode){
            BarColor.setStatusBarColor(UserInfoActivity.this, Color.parseColor("#A09AB4"));
        }
        else{
            BarColor.setStatusBarColor(UserInfoActivity.this,Color.parseColor("#3b3b3b"));
        }
        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }
        myToolbar = (Toolbar) findViewById(R.id.info_Toolbar);
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

        BmobDBHelper.getInstance().init(this);//连接bmob

        usernameview=findViewById(R.id.et_info_name);
        passwordview=findViewById(R.id.et_info_password);
        passwordview2=findViewById(R.id.et_info_password2);
        radioGroup=findViewById(R.id.info_radioGroup);
        school = findViewById(R.id.info_school);
        myflag = findViewById(R.id.info_flag);
        change_button = findViewById(R.id.info_change);
        exit_button = findViewById(R.id.exit_login);



        user_preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = user_preferences.getString("username","");
        if(!username.equals("")){
            checkByName(username);//搜索用户信息完成初始化
        }
        else{
            Toast.makeText(UserInfoActivity.this,"用户信息获取错误",Toast.LENGTH_LONG).show();
        }

        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当修改信息被点击
                uname = usernameview.getText().toString();
                pword = passwordview.getText().toString();
                pword2 = passwordview2.getText().toString();
                sch = school.getText().toString();
                flg = myflag.getText().toString();
                for(int i = 0;i<radioGroup.getChildCount();i++){
                    RadioButton rd = (RadioButton) radioGroup.getChildAt(i);
                    if(rd.isChecked()){
                        sex = rd.getText().toString();
                    }
                }

                new_user=new User();
                new_user.setUsername(uname);
                new_user.setPassword(pword);
                new_user.setSex(sex);
                new_user.setUniversty(sch);
                new_user.setMyflag(flg);
                //检查密码是否为空，，用户名是否重复（等于现在 、不等于现在   有几个数据）
                //然后修改数据

                if(pword.equals("")){
                    Toast.makeText(UserInfoActivity.this,"密码不允许为空",Toast.LENGTH_LONG).show();
                }
                else{
                    if(!pword.equals(pword2)){//两次密码不同
                        Toast.makeText(UserInfoActivity.this,"两次密码不同，请验证后修改",Toast.LENGTH_LONG).show();
                    }
                    else{
                        checkByName_ChangeInfo(uname);
                    }
                }

            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(UserInfoActivity.this);
                builder.setMessage("确定退出登录？");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //当退出登录被点击
                        SharedPreferences.Editor editor = user_preferences.edit();//保存到本地
                        editor.putString("username","");
                        editor.commit();
                        Toast.makeText(UserInfoActivity.this,"用户"+username+"已经退出登录",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
                        finish();
                    }
                });
                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
    }

    /**单个查询  用户信息
     * 并执行显示操作
     * **/
    public void checkByName(String uname) {

        BmobQuery<User> query = new BmobQuery<User>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", uname);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if(object.size()>0){
                        oldname = object.get(0).getUsername();
                        usernameview.setText(object.get(0).getUsername());
                        passwordview.setText(object.get(0).getPassword());
                        passwordview2.setText(object.get(0).getPassword());
                        school.setText(object.get(0).getUniversty());
                        myflag.setText(object.get(0).getMyflag());
                        if(object.get(0).getSex().equals("男")){
                            RadioButton radioButton = findViewById(R.id.info_radioButton);
                            radioButton.setChecked(true);
                        }
                        else{
                            RadioButton radioButton = findViewById(R.id.info_radioButton2);
                            radioButton.setChecked(true);
                        }
                    }
                    else {
                        Toast.makeText(UserInfoActivity.this,"用户信息获取失败",Toast.LENGTH_LONG).show();
                    }

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**单个查询  用户信息
     * 并执行修改操作
     * **/
    public void checkByName_ChangeInfo(String uname) {

        BmobQuery<User> query = new BmobQuery<User>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("username", uname);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {

                    String id= object.get(0).getObjectId();
                    if(uname.equals(oldname)){//用户名没改，可以修改
                            update(new_user,id);
                            Toast.makeText(UserInfoActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                    }
                    else{//
                        if(object.size()>0){//不可修改
                            Toast.makeText(UserInfoActivity.this,"用户名已被占用，修改失败",Toast.LENGTH_LONG).show();
                        }
                        else {
                            update(new_user,id);
                            Toast.makeText(UserInfoActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }


    //更新单条数据
    public void update(BmobObject ob, String objectId){//objectid是bmob后台自动生成的唯一id
        ob.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //LogUtil.e(MainActivity.class,"===更新成功===");
                }else{
                    // LogUtil.e(MainActivity.class,"更新失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


}
