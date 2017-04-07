package com.example.xiao.exlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by xiao on 2017年3月31日,0031.
 */

public class ExListView extends ListView implements AbsListView.OnScrollListener {

    //是否加载中或已加载所有数据
    private boolean mIsLoadingOrComplete;
    //是否所有条目都可见
    private boolean mIsAllVisible;

    private OnLoadMoreListener mOnLoadMoreListener;
    private View mLoadMoreView;
    private View mLoadCompleteView;

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
        if (view.getLastVisiblePosition() == getAdapter().getCount() - 1 && scrollState == SCROLL_STATE_IDLE && !mIsLoadingOrComplete && !mIsAllVisible) {
            if (null != mOnLoadMoreListener) {
                //加载更多(延时1.5秒,防止加载速度过快导致加载更多布局闪现)
                mIsLoadingOrComplete = true;
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnLoadMoreListener.loadMore();
                    }
                }, 1500);
            }
        }
        if (getFooterViewsCount() == 0 && !mIsAllVisible) addFooterView(mLoadMoreView);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mIsAllVisible = totalItemCount == visibleItemCount;
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
        if (allComplete && getFooterViewsCount() != 0) {
            removeFooterView(mLoadMoreView);
            removeFooterView(mLoadCompleteView);
            addFooterView(mLoadCompleteView);
        } else {
            mIsLoadingOrComplete = false;
        }
    }
}
