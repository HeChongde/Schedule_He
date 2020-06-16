package com.example.schedule_he.ui.richeng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;

import java.util.ArrayList;
import java.util.List;

public class Note_RC_Adapter extends BaseAdapter{
    private Context mContext;//上下文环境

    private List<Note_RC> backList;//用来备份原始数据
    private List<Note_RC> noteList;//这个数据是会改变的，所以要有个变量来备份一下原始数据
    //private MyFilter mFilter;//自定义的过滤器

    public Note_RC_Adapter(Context mContext, List<Note_RC> noteList) {//传入一个List，用于适配
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Note_RC> get_RC_NoteList(){
        return backList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme(R.style.DayTheme);
        View v;
        if(!Side_Menu.night_mode){
            v= View.inflate(mContext, R.layout.note_rc_layout, null);
        }
        else {
           v = View.inflate(mContext, R.layout.night_note_rc_layout, null);
        }

        TextView tv_title = (TextView)v.findViewById(R.id.tv_title);
        TextView tv_content = (TextView)v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView)v.findViewById(R.id.tv_time);

        //Set text for TextView
        String title = noteList.get(position).getTitle();
        String allText = noteList.get(position).getContent();
        String time = noteList.get(position).getTime();
        /*if (sharedPreferences.getBoolean("noteTitle" ,true))
            tv_content.setText(allText.split("\n")[0]);*/
        tv_title.setText(title);
        tv_content.setText(allText);  //设置内容
        tv_time.setText(time); //设置时间

        // Log.d("he1", "view内容"+allText);
        //Save note id to tag
        v.setTag(noteList.get(position).getId());//设置Tag

        return v;
    }

//    @Override
//    public Filter getFilter() {
//        if (mFilter ==null){
//            mFilter = new MyFilter();
//        }
//        return mFilter;
//    }

//    class MyFilter extends Filter {
//        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            FilterResults result = new FilterResults();
//            List<Note_RC> list;
//            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
//                list = backList;
//            } else {//否则把符合条件的数据对象添加到集合中
//                list = new ArrayList<>();
//                for (Note_RC note : backList) {
//                    if (note.getContent().contains(charSequence)) {
//                        list.add(note);
//                    }
//
//                }
//            }
//            result.values = list; //将得到的集合保存到FilterResults的value变量中
//            result.count = list.size();//将集合的大小保存到FilterResults的count变量中
//
//            return result;
//        }
//        //在publishResults方法中告诉适配器更新界面
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            noteList = (List<Note_RC>) filterResults.values;
//            if (filterResults.count>0){
//                notifyDataSetChanged();//通知数据发生了改变
//            }else {
//                notifyDataSetInvalidated();//通知数据失效
//            }
//        }
//    }
//



}
