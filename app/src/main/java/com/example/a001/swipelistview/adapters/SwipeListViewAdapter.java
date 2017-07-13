package com.example.a001.swipelistview.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a001.swipelistview.R;
import com.example.a001.swipelistview.views.SlideView;

import java.util.List;


public class SwipeListViewAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SwipeAdatper";

    private List<String> stringList;

    private int resourceId;

    private ItemDeleteButonClickListerner mListener;

    public SwipeListViewAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
        stringList = objects;
        Log.d(TAG,"resourceId =" + resource);
    }

    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        String string = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.bt1 = (LinearLayout)view.findViewById(R.id.bt1);
            holder.bt2 = (Button)view.findViewById(R.id.bt2);
            holder.bt3 = (Button)view.findViewById(R.id.bt3);
            holder.tv = (TextView)view.findViewById(R.id.tv);
            view.setTag(holder);
        }else{
            view =convertView;
            holder = (ViewHolder) view.getTag();
        }
        final SlideView slideView = (SlideView) view;
        slideView.setPositionInListView(position);
        Log.d(TAG,"getVIEW positon =" + position);
        holder.tv.setText(string);
        holder.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"你点击了Item"+position,Toast.LENGTH_SHORT).show();
            }
        });
        holder.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"你取消了关注",Toast.LENGTH_SHORT).show();
            }
        });

        holder.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "你点击了删除", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"item view parent =" + slideView.getParent().getClass().getName());
                slideView.deleteItem();
                mListener.onDelete(position,slideView);
            }
        });
        Log.d(TAG,"position=" + position);
        return view;
    }

     class ViewHolder{
         LinearLayout bt1;
         Button bt2;
         Button bt3;
         TextView tv;
    }

    public void setOnItemDeleteButtonClickListener(ItemDeleteButonClickListerner listener){
        this.mListener = listener;
    }

    public interface ItemDeleteButonClickListerner{
        public void onDelete(int position,View v);
    }

    public List<String> getStringList(){
        return stringList;
    }
}
