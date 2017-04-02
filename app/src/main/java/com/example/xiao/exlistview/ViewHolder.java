package com.example.xiao.exlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiao on 2017/2/3.
 */

public class ViewHolder {
    private View mConvertView;
    private Map<Integer,View> mViewMap;
    private Context mContext;

    public ViewHolder(Context context,int layoutId,ViewGroup parent){
        mConvertView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        mViewMap=new HashMap<>();
        mConvertView.setTag(this);
        mContext=context;
    }

    public synchronized static ViewHolder getHolder(Context context,int layoutId, View convertView, ViewGroup parent){
        if (null==convertView){
            return new ViewHolder(context,layoutId,parent);
        }else{
            return (ViewHolder) convertView.getTag();
        }
    }

    public View getConvertView(){
        return mConvertView;
    }

    /**
     * 获取控件,mViewMap里有则直接取,没有则通过findViewById拿到并保存到mViewMap
     * @param viewId 控件id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId){
        if (mViewMap.containsKey(viewId)){
            return (T) mViewMap.get(viewId);
        }else{
            View view=mConvertView.findViewById(viewId);
            mViewMap.put(viewId,view);
            return (T) view;
        }
    }

    /**
     * 内容适配,一般就用到ImageView和TextView,而且ImageView通常是url,所以就用反射封装一下,返回自身实现链式调用
     * @param viewId 控件id
     * @param content 要适配的内容(文字字符串/图片url)
     * @return
     */
    public ViewHolder set(int viewId,String content){
        View view=getView(viewId);
        if (view instanceof TextView){
            ((TextView) view).setText(content);
        }else if (view instanceof ImageView){
            Picasso.with(mContext).load(content).into((ImageView) view);
        }
        return this;
    }
}
