package com.example.alcoholic.common;

import com.example.alcoholic.bean.MessageBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by
 * Description:
 * on 2020/11/16.
 */
public abstract class ImActivity extends MyActivity{

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取到新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNewMessage(MessageBean newMsgBean) {
        onReNewMsg(newMsgBean);
    };


    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 获取到新消息
     */
    protected abstract void onReNewMsg(MessageBean newMsgBean);


}
