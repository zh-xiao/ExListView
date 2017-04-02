package com.example.xiao.exlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by xiao on 2017年3月31日,0031.
 */

public class ExListView extends ListView implements AbsListView.OnScrollListener {

    //是否加载中
    private boolean mIsLoading;
    //总的条目数
    private int mTotalItemCount;
    //加载起始时间
    private long mLoadBeginTime;
    //是否所有条目都可见
    private boolean mIsAllVisible;

    private OnLoadMoreListener mOnLoadMoreListener;
    private View mLoadMoreView;
    private View mLoadCompleteView;
    private BaseAdapter mAdapter;

    public ExListView(Context context) {
        super(context);
        init(context);
    }

    public ExListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter=adapter;
    }

    //加载更多回调接口
    public interface OnLoadMoreListener {
        void loadMore();
    }

    //初始化
    private void init(Context context) {
        mLoadMoreView = LayoutInflater.from(context).inflate(R.layout.load_more, null);
        mLoadCompleteView = LayoutInflater.from(context).inflate(R.layout.load_complete, null);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //(最后一条可见item==最后一条item)&&(停止滑动)&&(!加载数据中)&&(!所有条目都可见)
        if (view.getLastVisiblePosition() == mTotalItemCount - 1 && scrollState == SCROLL_STATE_IDLE && !mIsLoading&&!mIsAllVisible) {
            if (null != mOnLoadMoreListener) {
                //加载更多
                mIsLoading = true;
                addFooterView(mLoadMoreView);
                mLoadBeginTime = System.currentTimeMillis();
                mOnLoadMoreListener.loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        mIsAllVisible=totalItemCount==visibleItemCount;
    }

    /**
     * 加载更多回调
     *
     * @param onLoadMoreListener 加载更多回调接口
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 通知此次加载完成,remove footerView
     *
     * @param allComplete 是否已加载全部数据
     */
    public void setLoadCompleted(final boolean allComplete) {
        //加载时间低于一秒则延时一秒
        postDelayed(new Runnable() {
            @Override
            public void run() {
                removeFooterView(mLoadMoreView);
                //已加载全部数据,则显示"没有更多数据"一秒钟
                if (allComplete) {
                    addFooterView(mLoadCompleteView);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIsLoading = false;
                            removeFooterView(mLoadCompleteView);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    mIsLoading = false;
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, System.currentTimeMillis() - mLoadBeginTime < 2000 ? 2000 : System.currentTimeMillis() - mLoadBeginTime);
    }
}
