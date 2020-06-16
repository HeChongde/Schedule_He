package com.example.schedule_he.ui.bianqian;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.schedule_he.MainActivity;
import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {


    //////////////////////////////////////
    private NoteDatabase dbHelper;
    //private Context context = this;
    final String TAG = "tag";
    FloatingActionButton btn;
    private NodeAdapter adapter;
    private List<Note> noteList = new ArrayList<Note>();


    private Toolbar myToolbar;
    private ListView lv;
    FloatingActionButton btn1;
    View root;
    private int marginTop =140;


    ////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(!Side_Menu.night_mode){
            root = inflater.inflate(R.layout.fragment_home, container, false);
        }
        else{
            root = inflater.inflate(R.layout.night_layout_home, container, false);
        }

//    原始代码
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });




        //我的代码分割线///////////////////////
        btn1=(FloatingActionButton) root.findViewById(R.id.add_edit);
        btn1.setOnClickListener(new View.OnClickListener() {//点击添加
            @Override
            public void onClick(View v) {
                //Log.d("HH", "Ok");//log输出测试
                Intent intent = new Intent(getContext(),EditActivity.class);//意图
                intent.putExtra("mode",4);//模式为4 代表新建笔记
                startActivityForResult(intent,0);//启功EditActivity,并获取结果
            }
        });

        myToolbar=root.findViewById(R.id.myToolbar);
        //不加这行菜单无法显示，告诉fragment我们有菜单的
        setHasOptionsMenu(true);
        //清理
//        Menu menu = myToolbar.getMenu();
//        menu.clear();
        //加载菜单
        if(!Side_Menu.night_mode){
            myToolbar.inflateMenu(R.menu.bq_menu);
        }
        else{
            myToolbar.inflateMenu(R.menu.night_bq_menu);
        }
        myToolbar.setTitle("便签");
        if(!Side_Menu.night_mode){
            myToolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
        }
        else{
            myToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_clear:
                        onDeleteAllClic();
                        break;
                }
                return true;
            }
        });
        SearchCreate();

        final Side_Menu side_menu = new Side_Menu(root,this,R.id.home_layout,1);
        side_menu.initPopupView();
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               side_menu.showPopUpView();
            }
        });

        lv = root.findViewById(R.id.lv);
        adapter = new NodeAdapter(root.getContext(), noteList);
        refreshListViwe();
        lv.setAdapter(adapter);//给ListView设置适配器显示内容
        lv.setOnItemClickListener(this);//给文本设置点击事件监听器
        //initView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                        Note note=(Note)adapter.getItem(position);
                        if(adapter.get_NoteList().remove(position)!=null){
                            Note curNote = new Note();
                            curNote.setId(note.getId());
                            DBop op = new DBop(getContext());
                            op.open();
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





        ///我的代码分割线////////////////////////////////////////////////
        return root;
    }



    private void onDeleteAllClic(){
        new AlertDialog.Builder(root.getContext())
                .setMessage("删除全部吗？")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new NoteDatabase(getContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("notes", null, null);
                        db.execSQL("update sqlite_sequence set seq=0 where name='notes'");
                        refreshListViwe();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void SearchCreate(){
        Menu menu = myToolbar.getMenu();
        MenuItem mSearch = menu.findItem(R.id.action_search);


        if(Side_Menu.night_mode){
            //通过找到search view里面的一个叫做appcompatImageViewbutton来修改图标和颜色
            AppCompatImageView button=(AppCompatImageView)root.findViewById(androidx.appcompat.R.id.search_button);
            button.setImageResource(R.drawable.ic_search_white_24dp);
            TextView textView=(TextView)root.findViewById(androidx.appcompat.R.id.search_src_text);
            textView.setTextColor(0xAAFFFFFF);
            textView.setHintTextColor(0xAAFFFFFF);
            AppCompatImageView button2=(AppCompatImageView)root.findViewById(androidx.appcompat.R.id.search_close_btn);
            button2.setImageResource(R.drawable.ic_close_white_24dp);
        }
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);//适配器过滤
                return false;
            }
        });
    }


    //接收startActivityForResult的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//用来接收传回来的

        int returnMode;
        long note_id;
        returnMode=data.getExtras().getInt("mode",-1);//获取模式 默认-1 啥也没改
        note_id = data.getExtras().getLong("id",0);

        if (returnMode == 1) {  //更新当前笔记

            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);

            Note newNote = new Note(content, time, tag);
            newNote.setId(note_id);
            DBop op = new DBop(getContext());
            op.open();
            op.updateNote(newNote);
            op.close();
        } else if (returnMode == 0) {  // 新建了一个
            String content = data.getExtras().getString("content");
            String time = data.getExtras().getString("time");
            int tag = data.getExtras().getInt("tag", 1);
            Note newNote = new Note(content, time, tag);
            DBop op = new DBop(getContext());
            op.open();
            op.addNote(newNote);
            op.close();
        } else if (returnMode == 2) { //删除
            Note curNote = new Note();
            curNote.setId(note_id);
            DBop op = new DBop(getContext());
            op.open();
            op.removeNote(curNote);
            op.close();
        }
        else{//什么也不做

        }
        Log.d("he", "mode是"+returnMode);
        refreshListViwe();//刷新
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

    public void refreshListViwe(){//更新内容
        DBop op = new DBop(getContext());
        op.open();
        // set adapter
        //Log.d("he3", "数据长度"+noteList.size());
        if (noteList.size() > 0) noteList.clear();
        noteList.addAll(op.getAllNotes());
        op.close();
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv:
                Note curNote = (Note) parent.getItemAtPosition(position);
                Intent intent = new Intent(root.getContext(), EditActivity.class);
                intent.putExtra("content", curNote.getContent());
                intent.putExtra("id", curNote.getId());
                intent.putExtra("time", curNote.getTime());
                intent.putExtra("mode", 3);     //打开模式   此处为3 即代表打开的是已有文件
                intent.putExtra("tag", curNote.getTag());
                startActivityForResult(intent, 1);
               // Log.d(TAG, "onItemClick: " + position);
                break;
        }
    }

}
