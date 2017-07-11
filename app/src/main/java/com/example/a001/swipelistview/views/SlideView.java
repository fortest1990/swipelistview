package com.example.a001.swipelistview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by 廖婵001 on 2017/7/11 0011.
 */

public class SlideView extends ViewGroup {

    public static boolean isViewOpen;

    public static int openItemPosition;

    private static final String TAG = "SlideView";

    private int downX;
    private int currX;
    private int lastX;
    private int positionInListView;

    private int scrollX;

    private int touchSlop;

    //view随手指滑动界限
    private int bound;

    private Scroller mScroller;

    boolean isOpen = false;

    boolean isFirstLayout = false;

    public SlideView(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            width += getChildAt(i).getMeasuredWidth();
        }
        setMeasuredDimension(width,getChildAt(0).getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getChildAt(0).getMeasuredWidth();
        int height = getChildAt(0).getMeasuredHeight();
        Log.d(TAG,"width =" + width + "parent width=" + getWidth() + " " + getChildAt(0).getWidth());

        View childView1 = getChildAt(0);
        View childView2 = getChildAt(1);
        View childView3 = getChildAt(2);
        childView1.layout(0,0,width,height);
        childView2.layout(width,0,width+getChildAt(1).getMeasuredWidth(),height);
        childView3.layout(width+getChildAt(1).getMeasuredWidth(),0,width + getChildAt(1).getMeasuredWidth() + getChildAt(2).getMeasuredWidth(),height);
        bound = getChildAt(1).getMeasuredWidth() + getChildAt(2).getMeasuredWidth();
        if(!isFirstLayout){
            getChildAt(0).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(isOpen){
                                closeItem();
                                return true;
                            }
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int)ev.getX();
                currX = (int)ev.getX();
                Log.d(TAG,"onDown");
                break;
            case MotionEvent.ACTION_MOVE:
                currX = (int)ev.getX();
                int dX = Math.abs(currX - downX);
                Log.d(TAG,"onInterceptMove");
                if(dX > touchSlop && !isOpen){
                    return true;
                }
        }
        lastX = currX;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scrollX = getScrollX();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                requestDisallowInterceptTouchEvent(true);
                currX = (int)event.getX();
                int dx = currX - lastX;
                if(scrollX< bound && scrollX >= 0){
                    scrollBy(-dx,0);
                    Log.d(TAG,"onMove");
                }else if (scrollX >= bound && dx > 0 ){
                    scrollBy(-dx,0);
                }else if(scrollX < 0 && dx  < 0){
                    scrollBy(-dx,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(scrollX <=70){
                    mScroller.startScroll(scrollX,0,-scrollX,0);
                    invalidate();
                }
                if(scrollX > 70){
                    mScroller.startScroll(scrollX,0,bound - scrollX,0);
                    isOpen = true;
                    setIsViewOpen(true);
                    openItemPosition = getPositionInListView();
                    invalidate();
                }
                break;
        }
        lastX = currX;
        return true;
    }

    public void  closeItem(){
        mScroller.startScroll(getScrollX(),0,-bound,0);
        isOpen = false;
        setIsViewOpen(false);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    public static void setIsViewOpen(boolean b){
        isViewOpen = b;
    }

    public void setPositionInListView(int p){
        this.positionInListView = p ;
        Log.d(TAG,"positon in listview=" + p);
    }
    public int getPositionInListView(){
        return positionInListView;
    }
}
