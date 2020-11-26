package com.example.alcoholic.ui.acitivity.image;

import android.content.Context;
import android.content.Intent;

import com.example.alcoholic.R;
import com.example.alcoholic.aop.CheckNet;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.JumpDataContants;
import com.example.alcoholic.ui.page.ImagePagerAdapter;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/03/05
 *    desc   : 查看大图
 */
public final class ImagePreviewActivity extends MyActivity {

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, ArrayList<String> urls) {
        start(context, urls, 0);
    }

    @CheckNet
    public static void start(Context context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(JumpDataContants.IMAGE, urls);
        intent.putExtra(JumpDataContants.INDEX, index);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private PageIndicatorView mIndicatorView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
        mIndicatorView = findViewById(R.id.pv_image_preview_indicator);
        mIndicatorView.setViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        ArrayList<String> images = getStringArrayList(JumpDataContants.IMAGE);
        int index = getInt(JumpDataContants.INDEX);
        if (images != null && images.size() > 0) {
            mViewPager.setAdapter(new ImagePagerAdapter(this, images));
            if (index != 0 && index <= images.size()) {
                mViewPager.setCurrentItem(index);
            }
        } else {
            finish();
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return false;
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}