package com.forezp.newframe.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.forezp.newframe.R;
import com.forezp.newframe.adapter.FragmentAdapter;
import com.forezp.newframe.common.Constants;
import com.forezp.newframe.view.CustomViewPager;
import com.nineoldandroids.view.ViewHelper;
import com.trilink.androidlib.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by b508a on 2016/3/31.
 */
public class MainActivity2 extends BaseActivity {


    @Bind(R.id.viewpager)
    CustomViewPager viewpager;
    @Bind(R.id.main_tab_home)
    RadioButton mainTabHome;
    @Bind(R.id.main_tab_product)
    RadioButton mainTabProduct;
    @Bind(R.id.main_tab_life)
    RadioButton mainTabLife;
    @Bind(R.id.main_tab_group)
    RadioGroup mainTabGroup;

    private DrawerLayout mDrawerLayout;


    public FragmentAdapter adapter;
    private RadioButton[] rbs;
    private static int[] selected_resID = new int[]{R.drawable.rb_weather_home, R.drawable.rb_weather_product, R.drawable.rb_weather_life};
    private static int[] unselected_resID = new int[]{R.drawable.rb_weather_home, R.drawable.rb_weather_product, R.drawable.rb_weather_life};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        /**
         * rationButton--viewpager
         */
        rbs = new RadioButton[]{mainTabHome, mainTabProduct, mainTabLife};
        // ll_parent = (LinearLayout) findViewById(R.id.ll_parent);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        addListener();
        rbClick(mainTabHome);

        /**
         * drawableLayout
         */
        initView();
        initEvents();

    }

    private void addListener() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case 0:
                        mainTabHome.setChecked(true);
                        break;
                    case 1:
                        mainTabProduct.setChecked(true);
                        break;
                    case 2:
                        mainTabLife.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }



    private void setRBstyle(int resID) {
        for (int i = 0; i < 3; i++) {
            if (rbs[i].getId() == resID) {
                Drawable drawable = getResources().getDrawable(selected_resID[i]);
                rbs[i].setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                rbs[i].setTextColor(getResources().getColor(R.color.white));
                rbs[i].setBackgroundResource(R.color.botton_ratiobutton);
            } else {
                Drawable drawable = getResources().getDrawable(unselected_resID[i]);
                rbs[i].setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                rbs[i].setTextColor(getResources().getColor(R.color.white));
                rbs[i].setBackgroundResource(R.color.transparent);
            }
        }
    }


    public void OpenRightMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
    }


    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                Gravity.RIGHT);
    }

    @OnClick({R.id.main_tab_home, R.id.main_tab_product, R.id.main_tab_life})
    public void onClick(View view) {
        setRBstyle(view.getId());
        switch (view.getId()) {
            case R.id.main_tab_home:
                Constants.current_fragment_index = 0;
                // tvTitle.setVisibility(View.GONE);
                //llTitleCenter.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.GONE);
                viewpager.setCurrentItem(0, false);
                break;
            case R.id.main_tab_product:
                Constants.current_fragment_index = 1;
                // tvTitle.setVisibility(View.GONE);
                //llTitleCenter.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.GONE);
                viewpager.setCurrentItem(1, false);
                break;
            case R.id.main_tab_life:
                Constants.current_fragment_index = 2;
                // tvTitle.setVisibility(View.GONE);
                //llTitleCenter.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.GONE);
                viewpager.setCurrentItem(3, false);
                break;
        }
    }

    public void rbClick(View v) {
        setRBstyle(v.getId());
        switch (v.getId()) {
            case R.id.main_tab_home:
                Constants.current_fragment_index = 0;
                // tvTitle.setVisibility(View.GONE);
                //llTitleCenter.setVisibility(View.VISIBLE);
                //divider.setVisibility(View.GONE);
                viewpager.setCurrentItem(0, false);
                break;
            case R.id.main_tab_product:
                Constants.current_fragment_index = 1;
                // llTitleCenter.setVisibility(View.GONE);
                // tvTitle.setText(R.string.menu2);
                // tvTitle.setVisibility(View.VISIBLE);
                // divider.setVisibility(View.VISIBLE);
                viewpager.setCurrentItem(1, false);
                break;
            case R.id.main_tab_life:
                Constants.current_fragment_index = 2;
                //llTitleCenter.setVisibility(View.GONE);
                // tvTitle.setText(R.string.menu3);
                //  tvTitle.setVisibility(View.VISIBLE);
                //    divider.setVisibility(View.VISIBLE);
                //viewPager.removeAllViews();
                //viewPager.setAdapter(adapter);
                viewpager.setCurrentItem(2, false);
                break;
            default:
                break;
        }
    }

}
