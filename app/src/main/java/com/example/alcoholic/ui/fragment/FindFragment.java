package com.example.alcoholic.ui.fragment;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.ui.acitivity.HomeActivity;

import androidx.fragment.app.Fragment;

/**
 * Created by
 * Description:探索页面
 * on 2020/11/13.
 */
public final class FindFragment extends MyFragment<HomeActivity> {

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
