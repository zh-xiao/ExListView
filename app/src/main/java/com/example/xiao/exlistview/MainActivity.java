package com.example.xiao.exlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ExListView mExListView;
    private List<Integer> mList = new ArrayList<>();
    private CommonAdapter<Integer> mCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadData();
    }

    private void initView() {
        mExListView = (ExListView) findViewById(R.id.ex_listView);
        mExListView.setAdapter(mCommonAdapter = new CommonAdapter<Integer>(this, mList, R.layout.item_layout) {
            @Override
            public void convertView(ViewHolder holder, Integer s) {
                holder.set(R.id.content, "item:" + s);
            }
        });
        mExListView.setOnLoadMoreListener(new ExListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
                mExListView.setLoadCompleted(mList.size() >= 100 ? true : false);
            }
        });
    }

    private void loadData() {
        int size = mList.size();
        if (mList.size() >= 100) {
            return;
        }
        for (int i = size; i < size + 20; i++) {
            mList.add(i);
        }
        mCommonAdapter.notifyDataSetChanged();
    }

}
