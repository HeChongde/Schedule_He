package com.example.schedule_he.ui.richeng;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule_he.R;
import com.example.schedule_he.ui.Side_Menu;

import java.util.ArrayList;
import java.util.List;

public class RC_ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater inflater;
    private List<RC_item> itemList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;

    public RC_ItemAdapter(Context context, List<RC_item> itemList) {
        inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_richeng_all, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            if(!Side_Menu.night_mode){
                itemHolder.tvAcceptTime.setTextColor(0xff336633);
                itemHolder.tvAcceptStation.setTextColor(0xff000000);
            }
            else{
                itemHolder.tvAcceptTime.setTextColor(0xff66cccc);
                itemHolder.tvAcceptStation.setTextColor(0xffffffff);
            }
            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            if(!Side_Menu.night_mode){
                itemHolder.tvAcceptTime.setTextColor(0xff336633);
                itemHolder.tvAcceptStation.setTextColor(0xff000000);
            }
            else{
                itemHolder.tvAcceptTime.setTextColor(0xff66cccc);
                itemHolder.tvAcceptStation.setTextColor(0xffffffff);
            }

            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }

        itemHolder.bindHolder(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAcceptTime, tvAcceptStation;
        private TextView tvTopLine, tvDot;
        public ViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvAcceptStation = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
        }

        public void bindHolder(RC_item item) {
            tvAcceptTime.setText(item.getTime());
            tvAcceptStation.setText(item.getTitle());
        }
    }

}
