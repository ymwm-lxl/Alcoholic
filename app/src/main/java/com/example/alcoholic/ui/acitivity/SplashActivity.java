package com.example.alcoholic.ui.acitivity;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.ui.acitivity.user.LoginActivity;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.chat.EMClient;

import androidx.annotation.NonNull;

/**
 * Created by
 * Description:闪屏页面
 * on 2020/11/16.
 */
public class SplashActivity extends MyActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {



        if (MMKVUserUtils.getInstance().isLogin()){
            //已登录
            startActivity(HomeActivity.class);
            finish();
        }else {
            //没有登录
            startActivity(LoginActivity.class);
            finish();
        }


    }

    @Override
    protected void initData() {

    }


    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

}
