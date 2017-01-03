package com.tendy.loopviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tendy.loopviewpager.bean.LooperBean;
import com.tendy.loopviewpager.listener.OnPagerItemClickListener;
import com.tendy.loopviewpager.view.LooperPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LooperPager looperPager;
    private List<LooperBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        looperPager = (LooperPager) findViewById(R.id.looperPager);
        looperPager.setListener(new OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(int position) {
                LooperBean looperBean = datas.get(position);
                Toast.makeText(MainActivity.this, looperBean.title, Toast.LENGTH_SHORT).show();
            }
        });

        LooperBean item0 = new LooperBean();
        item0.title = "title 0";
        item0.resouceID = R.mipmap.a;
        datas.add(item0);

        LooperBean item1 = new LooperBean();
        item1.title = "title 1";
        item1.resouceID = R.mipmap.a;
        datas.add(item1);

        LooperBean item2 = new LooperBean();
        item2.title = "title 2";
        item2.resouceID = R.mipmap.a;
        datas.add(item2);

        LooperBean item3 = new LooperBean();
        item3.title = "title 3";
        item3.resouceID = R.mipmap.a;
        datas.add(item3);

        LooperBean item4 = new LooperBean();
        item4.title = "title 4";
        item4.resouceID = R.mipmap.a;
        datas.add(item4);

        looperPager.startCycle(datas);
    }
}
