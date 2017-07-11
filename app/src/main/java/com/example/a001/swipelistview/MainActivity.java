package com.example.a001.swipelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.a001.swipelistview.adapters.SwipeListViewAdapter;
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
        SwipeListView swipeListView = (SwipeListView)findViewById(R.id.list_view);
        for(int i=0;i<20;i++){
            String string = "我是一串字符"+i;
            list.add(string);
        }
        Log.d(TAG,list.toString()+ list.size());
        SwipeListViewAdapter adapter = new SwipeListViewAdapter(this,R.layout.swipe_listview_item,list);
        swipeListView.setAdapter(adapter);
        swipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemClick");
            }
        });
    }
}
