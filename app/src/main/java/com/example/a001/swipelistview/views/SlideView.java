package com.example.a001.swipelistview.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;


public class SlideView extends FrameLayout {
    //用于存储到底是哪个view在listview处于展开状态，只能一个view处于展开状态
    public static boolean isViewOpen;
    public static int openItemPosition;

    private static final String TAG = "SlideView";

    private int downX;
    private int currX;
    private int lastX;

    //私有position 每个对象在adapter的getview时 存储对应的position
    private int positionInListView;

    private int scrollX;

    private int touchSlop;

    //超出屏幕部分的宽度
    private int bound;

    private Scroller mScroller;

    private boolean isOpen = false;

    private boolean isFirstLayout = false;

    private boolean isFirstUpdate = false;

    private OnAnimationEndListener mListener;

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View firstChild = getChildAt(0);
        int  topMargin = getMarginTop(firstChild);
        int width = 0;
        int height = firstChild.getMeasuredHeight() + topMargin;
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            width += getChildAt(i).getMeasuredWidth();
        }
        setMeasuredDimension(width, height);
        Log.d(TAG,"onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //viewgroup的高等于第一个子布局的高，控制子view水平排列
        Log.d(TAG,"onMeasure");
        View firstChild = getChildAt(0);
        int width = firstChild.getMeasuredWidth();
        int height = firstChild.getMeasuredHeight();
        Log.d(TAG,"width =" + width + "parent width=" + getWidth() + " " + getChildAt(0).getWidth());

        int cl=0,cr=0;
        int topMargin = getMarginTop(firstChild);
        Log.d(TAG,"topMargin=" + topMargin);
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            cr += child.getMeasuredWidth();
            child.layout(cl,topMargin,cr,height+topMargin);
            cl += child.getMeasuredWidth();
        }
        bound = cr - firstChild.getMeasuredWidth();
        if(!isFirstLayout){
            firstChild.setOnTouchListener(new OnTouchListener() {
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

    private int getMarginTop(View v){
        MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
        return params.topMargin;
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
                //控制view随手指移动，防止越界
                if(scrollX - dx > bound ){
                    scrollBy(bound-scrollX,0);
                }else if(scrollX-dx < 0) {
                    scrollBy(-scrollX,0);
                }else {
                    scrollBy(-dx,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起，滑动view 如果是向左滑动，更新类的静态变量
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

    public void fastCloseItem(){
        scrollTo(0,0);
        isOpen = false;
        setIsViewOpen(false);
    }

    public void deleteItem(){
        isFirstUpdate = true;
        final View view = getChildAt(0);
        final MarginLayoutParams params = (MarginLayoutParams)view.getLayoutParams();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,-view.getMeasuredHeight());
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                params.topMargin = value;
                view.requestLayout();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fastCloseItem();
                ValueAnimator animator = ValueAnimator.ofInt(10,0);
                animator.setDuration(10);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        params.topMargin = 0;
                        view.requestLayout();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if((int)animation.getAnimatedValue() <2 && isFirstUpdate){
                            mListener.run();
                            Log.i(TAG,"value =" + animation.getAnimatedValue());
                            isFirstUpdate = false;
                        }
                    }
                });
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(400);
        valueAnimator.start();

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

    public interface OnAnimationEndListener{
        public void run();
    }

    public void setOnAnimationEndListener(OnAnimationEndListener listener){
        mListener = listener;
    }


}
