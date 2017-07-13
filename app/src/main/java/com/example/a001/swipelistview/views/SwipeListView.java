package com.example.a001.swipelistview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;


public class SwipeListView extends ListView {

    private static final String TAG = "SwipeListView";

    private boolean interceptTouchEvent;

    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(interceptTouchEvent){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"item count" + getChildCount());
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
        return super.onInterceptTouchEvent(ev);
    }
    public void setInterceptTouchEventWhenDeleting(boolean b){
        interceptTouchEvent = b;
    }
}
