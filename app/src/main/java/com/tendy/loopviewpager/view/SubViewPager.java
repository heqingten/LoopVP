package com.tendy.loopviewpager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class SubViewPager extends ViewPager implements OnGestureListener {
    private GestureDetector mDetector;
    private onSimpleClickListener listener;


    public SubViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(context, this);
    }

    public SubViewPager(Context context) {
        super(context);
    }

    public GestureDetector getGestureDetector() {
        return mDetector;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);// 这句话的作用
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (listener != null) {
            listener.setOnSimpleClickListenr(getCurrentItem());
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }


    public interface onSimpleClickListener {
        void setOnSimpleClickListenr(int position);
    }

    public void setOnSimpleClickListener(onSimpleClickListener listener) {
        this.listener = listener;
    }
}
