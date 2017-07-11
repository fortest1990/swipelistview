package com.example.a001.swipelistview.views;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by 廖婵001 on 2017/7/11 0011.
 */

public class SwipeListView extends ListView {

    private static final String TAG = "SwipeListView";

    private int downX;
    private int currX;
    private int lastX;
    private int touchSlop;

    private int height;
    private int childHeight;

    boolean isFirstLayout = false;
    boolean isChildOpened = false;

    public SwipeListView(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(SlideView.isViewOpen){
                    int childCount = getChildCount();
                    int position = 0;
                    int y = (int)ev.getY();
                    for(int i = 0; i < childCount; i ++){
                        if(y < getChildAt(i).getBottom()){
                            position = getFirstVisiblePosition()+ i + 1;
                            Log.d(TAG,"position=" + position + "firstPosition=" + getFirstVisiblePosition());
                            break;
                        }
                    }
                    if(position != SlideView.openItemPosition+1){
                        int currPosition = SlideView.openItemPosition - getFirstVisiblePosition();
                        SlideView slideView = (SlideView)getChildAt(currPosition);
                        if(slideView != null){
                            slideView.closeItem();
                        }
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
        }
        lastX = currX;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!isFirstLayout){
            height = getMeasuredHeight();
            childHeight = getChildAt(0).getMeasuredHeight();
        }
        isFirstLayout = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"TOP=" + getChildAt(4).getBottom());
                break;
        }
        return super.onTouchEvent(ev);
    }
}
