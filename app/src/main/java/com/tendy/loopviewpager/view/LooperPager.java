package com.tendy.loopviewpager.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.tendy.loopviewpager.Constants;
import com.tendy.loopviewpager.R;
import com.tendy.loopviewpager.adapter.LooperPagerAdapter;
import com.tendy.loopviewpager.bean.LooperBean;
import com.tendy.loopviewpager.listener.OnPagerItemClickListener;
import com.tendy.loopviewpager.util.DensityUtil;
import com.tendy.loopviewpager.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tendy
 */

public class LooperPager extends FrameLayout {
    private static final int COUNT_TIME_FLAG = 1000;
    private static final long LOOPER_TIME = 3000;

    private RelativeLayout rl_subview_pager;//轮播图布局
    private SubViewPager subViewPager;
    private LinearLayout ll_dots;

    private LooperPagerAdapter pagerAdapter;
    private ScheduledExecutorService scheduledExecutor;
    private boolean stopLoopBanner;// true : 停止轮播大图
    private long lastActionUpTime;// 滑动SubView手指抬起的时间
    private float mLastMotionX;
    private float mLastMotionY;
    private float xDistance;
    private float yDistance;
    private boolean mIsBeingDragged;
    private LinearLayout.LayoutParams margin;
    private ArrayList<ImageView> dots;
    private int oldPosition;
    private int currentPosition = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNT_TIME_FLAG:
                    if (!stopLoopBanner && (System.currentTimeMillis() - lastActionUpTime > LOOPER_TIME)) {
                        subViewPager.setCurrentItem(currentPosition);
                    }
                    break;
            }
        }
    };

    private OnPagerItemClickListener listener;

    public void setListener(OnPagerItemClickListener listener) {
        this.listener = listener;
    }

    public LooperPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.looper_pager_layout, this);
        rl_subview_pager = (RelativeLayout) findViewById(R.id.rl_subview_pager);
        subViewPager = (SubViewPager) findViewById(R.id.subViewPager);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        rl_subview_pager.getLayoutParams().height = (int) ((DensityUtil.getWidth(getContext())) * Constants.IMG_SCALE);
        pagerAdapter = new LooperPagerAdapter(getContext());
        margin = new LinearLayout.LayoutParams(DensityUtil.dip2px(getContext(), 8), DensityUtil.dip2px(getContext(), 8));
        margin.setMargins(0, 0, 10, 10);// 设置布局位置
        dots = new ArrayList<>();

        subViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                dots.get(position % dots.size()).setBackgroundResource(R.drawable.radio_checked);
                dots.get(oldPosition % dots.size()).setBackgroundResource(R.drawable.radio_unchecked);
                oldPosition = position;
                currentPosition = position;
                if (dots.size() == 1)
                    dots.get(0).setBackgroundResource(R.drawable.radio_checked);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        subViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                subViewPager.getGestureDetector().onTouchEvent(motionEvent);
                final float x = motionEvent.getRawX();
                final float y = motionEvent.getRawY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 停止轮播大图
                        stopLoopBanner = true;
                        xDistance = yDistance = 0f;
                        mLastMotionX = x;
                        mLastMotionY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float xDiff = Math.abs(x - mLastMotionX);
                        final float yDiff = Math.abs(y - mLastMotionY);
                        xDistance += xDiff;
                        yDistance += yDiff;
                        if (xDistance > yDistance || Math.abs(xDistance - yDistance) < 0.00001f) {
                            mIsBeingDragged = true;
                            mLastMotionX = x;
                            mLastMotionY = y;
                            view.getParent().requestDisallowInterceptTouchEvent(true);// 父ViewPager不要干扰
                        } else {
                            mIsBeingDragged = false;
                            view.getParent().requestDisallowInterceptTouchEvent(false);// 父ViewPager需要干扰
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        lastActionUpTime = System.currentTimeMillis();
                        // 开始轮播大图
                        stopLoopBanner = false;
                        if (mIsBeingDragged) {
                            view.getParent().requestDisallowInterceptTouchEvent(false);// 父ViewPager需要干扰
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void startCycle(final List<LooperBean> index) {

        if (!StringUtil.isNullOrEmpty(index)) {
            for (int i = 0; i < index.size(); i++) {
                ImageView imagedots1 = new ImageView(getContext());
                if (i == 0) {
                    imagedots1.setBackgroundResource(R.drawable.radio_checked);
                } else {
                    imagedots1.setBackgroundResource(R.drawable.radio_unchecked);
                }
                imagedots1.setLayoutParams(margin);
                dots.add(imagedots1);
                ll_dots.addView(imagedots1);
            }
            pagerAdapter.setDatas(index);
            subViewPager.setAdapter(pagerAdapter);

            subViewPager.setOnSimpleClickListener(new SubViewPager.onSimpleClickListener() {
                @Override
                public void setOnSimpleClickListenr(int position) {
                    int temp = position % index.size();
                    if (listener != null)
                        listener.onPagerItemClick(temp);
                }
            });

        }

        if (scheduledExecutor == null)
            scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!stopLoopBanner && (System.currentTimeMillis() - lastActionUpTime > 5000)) {
                    currentPosition++;
                    // 修改页面显示要放在子线程中完成
                    handler.sendEmptyMessage(COUNT_TIME_FLAG);
                }
            }
        }, LOOPER_TIME, LOOPER_TIME, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdown();
            scheduledExecutor = null;
        }
    }
}
