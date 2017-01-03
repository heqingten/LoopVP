package com.tendy.loopviewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tendy.loopviewpager.R;
import com.tendy.loopviewpager.bean.LooperBean;
import com.tendy.loopviewpager.util.StringUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/1/2 0002.
 */

public class LooperPagerAdapter extends PagerAdapter {
    private List<LooperBean> imgUrls;
    private Context context;

    public LooperPagerAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<LooperBean> datas) {
        this.imgUrls = datas;
    }

    public List<LooperBean> getImgUrls() {
        return imgUrls;
    }

    /**
     * 实例化ViewPager的Item
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.homepager_item, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        if (imgUrls != null) {
            LooperBean looperBean = imgUrls.get(position % imgUrls.size());
            if (looperBean != null) {
                Picasso.with(context).load(looperBean.resouceID).into(iv);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}
