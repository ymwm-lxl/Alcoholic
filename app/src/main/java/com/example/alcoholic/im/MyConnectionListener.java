package com.example.alcoholic.im;

import android.content.Intent;

import com.example.alcoholic.ui.acitivity.MainActivity;
import com.hjq.toast.ToastUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.util.EMLog;
import com.orhanobut.logger.Logger;

/**
 * Created by
 * Description:链接状态监听
 * on 2020/11/23.
 */
public class MyConnectionListener implements EMConnectionListener {


    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(int errorCode) {
        ToastUtils.show("链接已断开，将导致无法正常使用，请检查网络配置后重新启动 App");
    }



}
