package com.example.xiao.exlistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by xiao on 2017/2/3.
 */

public abstract class CommonAdapter<T> extends BaseAdapter{

    protected Context mContext;
    protected List<T> mList;
    protected int mLayoutId;

    public CommonAdapter(Context context, List<T> list, int layoutId) {
        mContext=context;
        mList=list;
        mLayoutId=layoutId;
    }

    //刷新数据
    public void refresh(List<T> list){
        mList=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(mContext, mLayoutId, convertView, parent);
        convertView(holder,mList.get(position));
        return holder.getConvertView();
    }

    public abstract void convertView(ViewHolder holder,T t);
}
