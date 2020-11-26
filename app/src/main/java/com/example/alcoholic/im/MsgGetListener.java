package com.example.alcoholic.im;

import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.db.DbMsgHelper;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by
 * Description:
 * on 2020/11/16.
 */
public class MsgGetListener implements EMMessageListener {
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        //收到消息

        new Thread(){
            @Override
            public void run() {
                super.run();
                MessageBean messageBean = ImBeanChangeHelper.msgBeanChange(messages.get(0));

                DbMsgHelper.insertMsg(messageBean);

                EventBus.getDefault().post(messageBean);
            }
        }.start();

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //收到透传消息

    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        //收到已读回执

    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        //收到已送达回执

    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        //消息被撤回

    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        //消息状态变动

    }

}

