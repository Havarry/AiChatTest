package com.gzhy.aichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzhy.aichat.R;
import com.gzhy.aichat.model.MsgMode;

import java.util.ArrayList;

/**
 * Created by hy on 2018-05-29
 */

public class MsgAdapter extends BaseAdapter{
    private ArrayList<MsgMode> msgList;
    private LayoutInflater mInflater;

    public MsgAdapter(ArrayList<MsgMode> msgList,Context context){
        this.msgList = msgList;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        MsgMode msg = msgList.get(position);
        return msg.getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            if(getItemViewType(position) == 0){
                view = mInflater.inflate(R.layout.chat_item_l,null);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView)view.findViewById(R.id.iconLeft);
                viewHolder.text = (TextView)view.findViewById(R.id.textLeft);
            }else {
                view = mInflater.inflate(R.layout.chat_item_r,null);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView)view.findViewById(R.id.iconRight);
                viewHolder.text = (TextView)view.findViewById(R.id.textRight);
            }
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.icon.setImageBitmap(msgList.get(position).getIcon());
        viewHolder.text.setText(msgList.get(position).getContent());
        return view;
    }

    static class ViewHolder{
        ImageView icon;
        TextView text;
    }
}
