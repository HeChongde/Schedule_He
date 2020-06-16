package com.example.schedule_he.ui.richeng;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.schedule_he.R;

import com.example.schedule_he.ui.Side_Menu;
import com.example.schedule_he.ui.bianqian.HomeFragment;
import com.example.schedule_he.ui.bianqian.NoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment implements AdapterView.OnItemClickListener {

    //private DashboardViewModel dashboardViewModel;

    private NoteBD_RC dbHelper = new NoteBD_RC(getContext());
    //private Context context = this;
    FloatingActionButton btn;
    private Note_RC_Adapter adapter;
    private List<Note_RC> noteList = new ArrayList<Note_RC>();


    private Toolbar myToolbar;
    private ListView lv_rc;
    CalendarView myCalendarView;
    TextView nong_Li;
    FloatingActionButton btn2;
    View root;
    private String _day="";
    private String select_day="";

    private AlarmManager alarmManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // dashboardViewModel =
                //ViewModelProviders.of(this).get(DashboardViewModel.class);
        //root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        if(!Side_Menu.night_mode){
            root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        }
        else{
            root = inflater.inflate(R.layout.night_layout_dashboard, container, false);
        }

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        myToolbar=root.findViewById(R.id.myToolbar2);
        //不加这行菜单无法显示，告诉fragment我们有菜单的
        setHasOptionsMenu(true);
        //加载菜单
        if(!Side_Menu.night_mode){
            myToolbar.inflateMenu(R.menu.rc_menu);
        }
        else{
            myToolbar.inflateMenu(R.menu.night_rc_menu);
        }

        myToolbar.setTitle("日程");
        if(!Side_Menu.night_mode){
            myToolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
        }
        else{
            myToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
        final Side_Menu side_menu = new Side_Menu(root,this,R.id.dashboard_layout,2);
        side_menu.initPopupView();
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                side_menu.showPopUpView();
            }
        });



        btn2=(FloatingActionButton) root.findViewById(R.id.add_richeng);
        btn2.setOnClickListener(new View.OnClickListener() {//点击添加
            @Override
            public void onClick(View v) {
                //Log.d("HH", "Ok");//log输出测试
                Intent intent = new Intent(getContext(), Edit_RCActivity.class);//意图
                intent.putExtra("mode",4);//模式为4 代表新建笔记
                intent.putExtra("day",select_day);
                startActivityForResult(intent,0);//启功EditActivity,并获取结果
            }
        });


        myCalendarView = (CalendarView) root.findViewById(R.id.cal_View);
        Date date = new Date();
        date.setTime(myCalendarView.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        _day=sdf.format(date);
        select_day=_day;//设置选中值初始为当前值
        Log.d("he", "当前获取的日期为" +_day );//获取到时间
        //设置日历点击监听
        myCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month=month+1;
                String day_format="";
                if(month<=9){
                    if(dayOfMonth<=9){
                        day_format=year+"0"+month+"0"+dayOfMonth;
                    }else{
                        day_format=year+"0"+month+dayOfMonth;
                    }
                }
                else{
                    if(dayOfMonth<=9){
                        day_format=year+""+month+"0"+dayOfMonth;
                    }else{
                        day_format=year+""+month+dayOfMonth;
                    }
                }
                Log.d("he", "选中了新的日期："+day_format);
                select_day=day_format;
                refreshListViwe(day_format);

                //农历
                try {
                    nong_Li.setText(CalendarUtil.solarToLunar(select_day));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rc_delete_all:
                        onDeleteAllClic();
                        break;
                    case R.id.ic_rc_all:
                        Intent intent1 = new Intent(root.getContext(), RC_xiangxiActivity.class);
                        startActivityForResult(intent1, 0);
                        break;
                }
                return true;
            }
        });
        //显示农历
        nong_Li=root.findViewById(R.id.non_Li);
        try {
            nong_Li.setText(CalendarUtil.solarToLunar(select_day));
        } catch (Exception e) {
            e.printStackTrace();
        }


        lv_rc = root.findViewById(R.id.lv_Cale);
        adapter = new Note_RC_Adapter(root.getContext(), noteList);
        refreshListViwe(_day);
        lv_rc.setAdapter(adapter);//给ListView设置适配器显示内容
        lv_rc.setOnItemClickListener(this);//给文本设置点击事件监听器
        lv_rc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(root.getContext());
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note_RC note=(Note_RC)adapter.getItem(position);
                        if(adapter.get_RC_NoteList().remove(position)!=null){
                            Note_RC curNote = new Note_RC();
                            curNote.setId(note.getId());
                            DBop_rc op = new DBop_rc(getContext());
                            op.open();
                            cancelAlarm(curNote);//删除闹钟
                            op.removeNote(curNote);
                            op.close();
                            //System.out.println("success");
                        }else {
                            //System.out.println("failed");
                        }
                        adapter.notifyDataSetChanged();
                        //Toast.makeText(getContext(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }

        });



//////////////////////////////////////////////////////////////////
        return root;
    }

    private void onDeleteAllClic(){
        new AlertDialog.Builder(root.getContext())
                .setMessage("删除全部吗？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBop_rc op = new DBop_rc(getContext());
                        op.open();
                        cancelAlarms(op.getAllNotes());//删除所有闹钟
                        op.close();

                        dbHelper = new NoteBD_RC(getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("notes_rc", null, null);
                        db.execSQL("update sqlite_sequence set seq=0 where name='notes_rc'");
                        refreshListViwe(_day);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    //接收startActivityForResult的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//用来接收传回来的
        String old_day=select_day;
        int returnMode;
        long note_id;
        returnMode=data.getExtras().getInt("mode",-1);//获取模式 默认-1 啥也没改
        note_id = data.getExtras().getLong("id",0);

        if (returnMode == 1) {  //更新当前笔记
            String title = data.getExtras().getString("title");
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            String day = data.getExtras().getString("day");
            old_day=day;//用于刷新列表的day
            Note_RC newNote = new Note_RC(title,content, time,day);
            newNote.setId(note_id);
            DBop_rc op = new DBop_rc(getContext());
            op.open();
            op.updateNote(newNote);
            //更新闹钟
            cancelAlarm(newNote);//删除闹钟
            //startAlarm(newNote);//添加新闹钟
            op.close();
        } else if (returnMode == 0) {  // 新建了一个
            String title = data.getExtras().getString("title");
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            String day = data.getExtras().getString("day");
            old_day=day;
            Note_RC newNote = new Note_RC(title,content,time,day);
            DBop_rc op = new DBop_rc(getContext());
            op.open();
            op.addNote(newNote);
            //添加闹钟
            //startAlarm(newNote);//添加新闹钟
            op.close();
            Log.d("he", "新建Day"+day);
        } else if (returnMode == 2) { //删除
            Note_RC curNote = new Note_RC();
            curNote.setId(note_id);
            DBop_rc op = new DBop_rc(getContext());
            op.open();
            op.removeNote(curNote);
            //删除闹钟
            cancelAlarm(curNote);//删除闹钟
            op.close();
        }
        else{//什么也不做

        }
        //Log.d("he", "mode是"+returnMode);
        String showday = data.getExtras().getString("old_day");
        refreshListViwe(showday);//刷新
        super.onActivityResult(requestCode, resultCode, data);

//        super.onActivityResult(requestCode, resultCode, data);
//        String content = data.getStringExtra("content");
//        String time = data.getStringExtra("time");
//        //测试接收
//        Log.d("he1", content);
//        Log.d("he2", time);
//        if(!content.equals("")){
//            Note note = new Note(content,time,1);
//            DBop op = new DBop(getContext());
//            op.open();
//            op.addNote(note);
//            op.close();
//        }
//        refreshListViwe();
    }

    public void refreshListViwe(String day){//更新内容
        try {
            nong_Li.setText(CalendarUtil.solarToLunar(day));
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBop_rc op = new DBop_rc(getContext());
        op.open();
        // set adapter
        //Log.d("he3", "数据长度"+noteList.size());
        if (noteList.size() > 0){
            noteList.clear();
            //cancelAlarms(noteList);//删除所有闹钟
        }
        noteList.addAll(op.getAllDayNotes(day));
        startAlarms(op.getAllNotes());//添加所有新闹钟
        op.close();
        adapter.notifyDataSetChanged();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_Cale:
                Note_RC curNote = (Note_RC) parent.getItemAtPosition(position);
                Intent intent = new Intent(root.getContext(), Edit_RCActivity.class);
                intent.putExtra("title", curNote.getTitle());
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("mode", 3);     //打开模式   此处为3 即代表打开的是已有文件
                intent.putExtra("day",curNote.getDay());
                startActivityForResult(intent, 1);
                // Log.d(TAG, "onItemClick: " + position);
                break;
        }
    }







    /////////设置提醒

    //设置提醒
    private void startAlarm(Note_RC p) {
        Calendar c = p.getPlanTime();
        if(!c.before(Calendar.getInstance())) {
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("title", p.getTitle());
            intent.putExtra("content", p.getContent());
            intent.putExtra("id", (int)p.getId());
            Log.d("he", "测试测试测试"+p.getTitle()+p.getContent()+(int)p.getId());
            Log.d("he", "时间时间"+c.getTime());
            Log.d("he", "时间时间"+Calendar.getInstance().getTime());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) p.getId(), intent, 0);
            //单次提醒
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    //设置很多提醒
    private void startAlarms(List<Note_RC> plans){
        for(Note_RC note_rc : plans){
            startAlarm(note_rc);
        }
    }

    //取消提醒
    private void cancelAlarm(Note_RC p) {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int)p.getId(), intent, 0);
        alarmManager.cancel(pendingIntent);

    }

    //取消很多提醒
    private void cancelAlarms(List<Note_RC> plans){
        for(Note_RC note_rc : plans){
            cancelAlarm(note_rc);
        }
    }

}
