package com.trilink.androidlib.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.trilink.androidlib.Constant;
import com.trilink.androidlib.R;
import com.trilink.androidlib.adapter.ViewPagerAdapter;
import com.trilink.androidlib.base.BaseActivity;
import com.trilink.androidlib.utils.SPUtils;

import java.util.ArrayList;

/**
 * 需要重写跳转界面。
 * Created by b508a on 2016/1/26.
 */
public   class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    // 定义ViewPager对象
    private ViewPager viewPager;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 定义各个界面View对象
    private View view1, view2, view3;
    // 定义开始按钮对象
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // 实例化各个界面的布局对象
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.guide_view1, null);
        view2 = mLi.inflate(R.layout.guide_view2, null);
        view3 = mLi.inflate(R.layout.guide_view3, null);
        //	view4 = mLi.inflate(R.layout.guide_view4, null);
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        // 将要分页显示的View装入数组中
        views.add(view1);
        views.add(view2);
        views.add(view3);
        //	views.add(view4);

        // 设置监听
        viewPager.setOnPageChangeListener(this);
        // 设置适配器数据
        viewPager.setAdapter(new ViewPagerAdapter(views));

        // 实例化开始按钮
        btnStart = (Button) view3.findViewById(R.id.startBtn);
        // 给开始按钮设置监听
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(GuideActivity.this, Constant.GUIDE_NAME,Constant.GUIDE_VERSION);
                // SplashActivity.sp.edit()
                //        .putInt("VERSION", SplashActivity.VERSION).commit();
                //startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                // finish();
                toLoginActivity();
                finish();

            }

        });
    }

    /**
     *
     */
    public   void  toLoginActivity(){

    }




    /**
     * 滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    /**
     * 当前页面滑动时调用
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 新的页面被选中时调用
     */
    @Override
    public void onPageSelected(int arg0) {
    }

}
