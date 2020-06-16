package com.example.schedule_he.ui.user;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.schedule_he.BarColor;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.richeng.RC_xiangxiActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class RegisterActivity extends AppCompatActivity {

    Toolbar myToolbar;

    TextView username;
    TextView password;
    TextView password2;
    RadioGroup radioGroup;
    TextView school;
    TextView myflag;
    Button button;

    String uname="";
    String pword="";
    String pword2="";
    String sex="";
    String sch="";
    String flg="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Side_Menu.night_mode) {
            setContentView(R.layout.activity_register);
        }
        else{
            setContentView(R.layout.night_activity_register);
        }
        if(!Side_Menu.night_mode){
            BarColor.setStatusBarColor(RegisterActivity.this, Color.parseColor("#A09AB4"));
        }
        else{
            BarColor.setStatusBarColor(RegisterActivity.this,Color.parseColor("#3b3b3b"));
        }
        if (getSupportActionBar() != null){//去除默认的ActionBar
            getSupportActionBar().hide();
        }
        myToolbar = (Toolbar) findViewById(R.id.register_Toolbar);
        if (!Side_Menu.night_mode) {
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
        init();

        BmobDBHelper.getInstance().init(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pword = password.getText().toString();
                pword2 = password2.getText().toString();
                sch = school.getText().toString();
                flg = myflag.getText().toString();

                for(int i = 0;i<radioGroup.getChildCount();i++){
                    RadioButton rd = (RadioButton) radioGroup.getChildAt(i);
                    if(rd.isChecked()){
                        sex = rd.getText().toString();
                    }
                }
                //一切就绪开始提交处理
                if("".equals(uname)||pword.equals("")){
                    Toast.makeText(RegisterActivity.this,"账户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(!pword.equals(pword2)){
                        Toast.makeText(RegisterActivity.this,"重复密码不一样，请重新输入",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        checkByName(uname);//查用户名是否重复  并注册
                    }
                }
            }
        });

    }

    private void init(){
        username = findViewById(R.id.et_register_name);
        password = findViewById(R.id.et_register_password);
        password2 = findViewById(R.id.et_register_password2);
        radioGroup = findViewById(R.id.radioGroup);
        school = findViewById(R.id.school);
        myflag = findViewById(R.id.flag);
        button = findViewById(R.id.resister);
    }

    /**单个查询  用户信息**/
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
                        Toast.makeText(getApplicationContext(),"用户名重复",Toast.LENGTH_SHORT).show();
                    }else{
                        User user = new User();
                        user.setUsername(uname);
                        user.setPassword(pword);
                        user.setSex(sex);
                        user.setUniversty(sch);
                        user.setMyflag(flg);
                        BmobDBOP bmobDBOP = new BmobDBOP();
                        bmobDBOP.insert(user);
                        Toast.makeText(getApplicationContext(),"注册成功,请登录",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    //Log.d("he", "查询成功   共有"+object.size()+"条数据"+ob.size() );
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

}
