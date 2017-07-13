package com.example.a001.swipelistview;


import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a001.swipelistview.adapters.SwipeListViewAdapter;
import com.example.a001.swipelistview.views.SlideView;
import com.example.a001.swipelistview.views.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SwipeListView swipeListView = (SwipeListView)findViewById(R.id.list_view);
        for(int i=0;i<12;i++){
            String string = "我是一串字符"+i;
            list.add(string);
        }
        Log.d(TAG,list.toString()+ list.size());
        final SwipeListViewAdapter adapter = new SwipeListViewAdapter(this,R.layout.swipe_listview_item,list);
        swipeListView.setAdapter(adapter);
        adapter.setOnItemDeleteButtonClickListener(new SwipeListViewAdapter.ItemDeleteButonClickListerner() {
            @Override
            public void onDelete(final int position, View v) {
                final SlideView slideView = (SlideView)v;
                swipeListView.setInterceptTouchEventWhenDeleting(true);
                Log.d(TAG,"position" + position);
                slideView.setOnAnimationEndListener(new SlideView.OnAnimationEndListener() {
                    @Override
                    public void run() {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        swipeListView.setInterceptTouchEventWhenDeleting(false);
                    }
                });
            }
        });
        Log.d(TAG,"MainActivity onCreate");
    }
}
