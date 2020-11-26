package com.example.alcoholic.ui.fragment;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.ui.acitivity.HomeActivity;

/**
 * Created by
 * Description: 首页
 * on 2020/11/13.
 */
public final class HomeFragment extends MyFragment<HomeActivity> {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
