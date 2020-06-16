package com.example.schedule_he.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.schedule_he.MainActivity;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Cloud.Load;
import com.example.schedule_he.ui.bianqian.HomeFragment;
import com.example.schedule_he.ui.user.BmobDBHelper;
import com.example.schedule_he.ui.user.LoginActivity;
import com.example.schedule_he.ui.user.UserInfoActivity;

public class Side_Menu {

    //弹出菜单
    private PopupWindow popupWindow;
    private PopupWindow popupCover;//覆盖阴影
    private ViewGroup customView;
    private ViewGroup coverView;//覆盖阴影
    private WindowManager wm;
    private DisplayMetrics metrics;
    private LayoutInflater layoutInflater;
    private RelativeLayout home;
    private View root;
    private Fragment fra;
    private int layout_id;
    private int flag;

    Switch night_Switch;
    LinearLayout user;
    LinearLayout downlode;
    LinearLayout uplode;
    TextView topusername;

    String username;

    public static boolean night_mode;
    //private SharedPreferences sharedPreferences;
    private SharedPreferences preferences;

    public Side_Menu(Context context){
        preferences=context.getSharedPreferences("n_mode",Context.MODE_PRIVATE);
        night_mode = preferences.getBoolean("night_mode",false);
    }

    public Side_Menu(View root,Fragment fra,int id,int flag){//flag用来标记是哪个页面
        this.root=root;
        this.fra=fra;
        this.layout_id=id;
        this.flag=flag;
        //获取该数据
        preferences=root.getContext().getSharedPreferences("n_mode",Context.MODE_PRIVATE);
        night_mode = preferences.getBoolean("night_mode",false);
    }
    @Override
    protected void finalize(){//析构方法
        night_mode = preferences.getBoolean("night_mode",false);
    }

    public void initPopupView(){
        layoutInflater = (LayoutInflater) root.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(!night_mode){
            customView = (ViewGroup) layoutInflater.inflate(R.layout.left_seting_layout, null);
        }
        else{
            customView = (ViewGroup) layoutInflater.inflate(R.layout.night_layout_left_etting, null);
        }
        //获取该数据
        //preferences=root.getContext().getSharedPreferences("mode",Context.MODE_PRIVATE);

        coverView = (ViewGroup) layoutInflater.inflate(R.layout.lefe_setting_cover_layout, null);
        home = root.findViewById(layout_id);
        wm = fra.getActivity().getWindowManager();
        metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
/*
        linearLayout_left=customView.findViewById(R.id.left_setting);
        aSwitch=customView.findViewById(R.id.nightMode);
        nightMode=customView.findViewById(R.id.ic_night);
        setIc=customView.findViewById(R.id.settings_image);
        textSet=customView.findViewById(R.id.settings_text);
        textNight=customView.findViewById(R.id.night_mode);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                linearLayout_left.setBackgroundColor(Color.BLACK);
                nightMode.setImageResource(R.drawable.ic_night_white_24dp);
                setIc.setImageResource(R.drawable.ic_settings_white_24dp);
                textSet.setTextColor(Color.WHITE);
                textNight.setTextColor(Color.WHITE);
            }
        });

*/
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(customView.getContext());
        //Intent intent = getIntent();
        //if(intent.getExtras() != null) night_change = intent.getBooleanExtra("night_change", false);
        //else night_change = false;

        initView();

        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //if(isNightMode()) myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_settings_white_24dp));
        //else myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_settings_black_24dp));

        //night_Switch.setChecked(night_mode);



    }
    public void showPopUpView(){
        int width = metrics.widthPixels;//屏幕宽高
        int height = metrics.heightPixels+30;

        popupCover = new PopupWindow(coverView, width, height, false);
        popupWindow = new PopupWindow(customView, (int) (width * 0.7), height, true);
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        popupCover.setAnimationStyle(R.style.PopupCoverAnimation);

        user =  customView.findViewById(R.id.user);
        user.setClickable(true);
        downlode = customView.findViewById(R.id.downlode);
        uplode = customView.findViewById(R.id.uplode);

        //在主界面加载成功之后 显示弹出
        root.findViewById(layout_id).post(new Runnable() {
            @Override
            public void run() {
                popupCover.showAtLocation(home, Gravity.NO_GRAVITY, 0, 0);
                popupWindow.showAtLocation(home, Gravity.NO_GRAVITY, 0, 0);

                //setting_image = customView.findViewById(R.id.setting_settings_image);
                //setting_text = customView.findViewById(R.id.setting_settings_text);

//                setting_image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
//                    }
//                });
//
//                setting_text.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(MainActivity.this, UserSettingsActivity.class));
//                    }
//                });
//
                coverView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popupCover.dismiss();
                    }
                });
            }

        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("")){
                    fra.startActivity(new Intent(customView.getContext(), LoginActivity.class));
                }
                else {
                    fra.startActivity(new Intent(customView.getContext(), UserInfoActivity.class));
                }
            }
        });

        /**
         * 点击下载文件到本地
         * **/
        downlode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("")||username==null){
                    Toast.makeText(root.getContext(),"请先登录后再操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(root.getContext());
                builder.setMessage("确定将云端的数据同步到本地?（本地文件会被清除）");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //将本地三个数据库清空，并将云端的三个数据库的内容都读取出来并存储到本地
                        Load load = new Load();
                        load.download(root.getContext(),username,fra);
                        Toast.makeText(root.getContext(),"云端的数据同步到本地成功",Toast.LENGTH_SHORT).show();

                        //刷新界面
                        popupWindow.dismiss();
                        fra.startActivity(new Intent(root.getContext(), MainActivity.class));
                        fra.getActivity().overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
                        fra.getActivity().finish();
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

        /**
         * 点击上传文件到云端
         *
         * **/
        uplode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.equals("")||username==null){
                    Toast.makeText(root.getContext(),"请先登录后再操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(root.getContext());
                builder.setMessage("确定将本地的数据同步到云端?（云端文件会被清除）");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //将云端三个数据库清空，并将本地的三个数据库的内容都读取出来并存储到云端
                        Load load = new Load();
                        load.upload(root.getContext(),username);
                        Toast.makeText(root.getContext(),"本地的数据同步到云端成功",Toast.LENGTH_SHORT).show();
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

    //夜间模式
    private void initView(){
        night_Switch = customView.findViewById(R.id.nightMode);
//        linearLayout_left=customView.findViewById(R.id.left_setting);
//        nightMode=customView.findViewById(R.id.ic_night);
//        setIc=customView.findViewById(R.id.settings_image);
//        textSet=customView.findViewById(R.id.settings_text);
//        textNight=customView.findViewById(R.id.night_mode);
        topusername=customView.findViewById(R.id.top_username);

        BmobDBHelper.getInstance().init(root.getContext());
        TextView user = customView.findViewById(R.id.user_text);
        SharedPreferences user_preferences;
        user_preferences=root.getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        username = user_preferences.getString("username","");
        if(!username.equals("")){
            user.setText("用户 ["+username+"]");
            topusername.setText(username);
        }

        night_Switch.setChecked(night_mode);
        //night_Switch.setChecked(sharedPreferences.getBoolean("nightMode", false));
        final SharedPreferences.Editor editor = preferences.edit();
        night_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!night_mode){
                    night_mode=true;//改为夜间模式
                    editor.putBoolean("night_mode",true);

                    Intent intent = new Intent(root.getContext(), MainActivity.class);
                    intent.putExtra("night_mode", !night_mode); //重启一次，正负颠倒。最终为正值时重启MainActivity。
                    popupWindow.dismiss();
                    fra.startActivity(new Intent(root.getContext(), MainActivity.class));
                    fra.getActivity().overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
                    fra.getActivity().finish();
                }
                else{
                    night_mode=false;//改为日间模式
                    editor.putBoolean("night_mode",false);

                    Intent intent = new Intent(root.getContext(), MainActivity.class);
                    intent.putExtra("night_mode", !night_mode); //重启一次，正负颠倒。最终为正值时重启MainActivity。
                    popupWindow.dismiss();
                    fra.startActivity(new Intent(root.getContext(), MainActivity.class));
                    fra.getActivity().overridePendingTransition(R.anim.night_switch, R.anim.night_switch_over);
                    fra.getActivity().finish();
                }
                editor.commit();
            }
        });
    }


}
