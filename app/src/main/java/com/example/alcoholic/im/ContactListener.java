package com.example.alcoholic.im;

import android.content.Intent;

import com.example.alcoholic.bean.ContactListenerBean;
import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.bean.MessageEncryptBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.constant.PConstant;
import com.example.alcoholic.constant.RandomConstant;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.base.BaseActivity;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;

/**
 * Created by
 * Description: 联系人监听
 * on 2020/11/18.
 */
public class ContactListener implements EMContactListener {
    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
//        Logger.d("增加了联系人");
        DbContactsHelper.upDateAccount2Friend(username,true);

        EventBus.getDefault().post(new ContactListenerBean(username,true));
        SendMsg.sendText("你好！很高兴和你成为朋友。",username);
    }

    @Override
    public void onContactDeleted(String username) {
        //被删除时回调此方法
//        Logger.d("被删除时");
        DbContactsHelper.upDateAccount2Friend(username,false);
        EventBus.getDefault().post(new ContactListenerBean(username,false));
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请,直接同意
//        Logger.d("收到好友请求");
        EMClient.getInstance().contactManager().asyncAcceptInvitation(username, new EMCallBack() {
            @Override
            public void onSuccess() {
                contactAdd(username,reason);
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    public void onFriendRequestAccepted(String username) {
        //好友请求被同意
//        Logger.d("好友请求被同意");
        DbContactsHelper.upDateAccount2Friend(username,true);
        EventBus.getDefault().post(new ContactListenerBean(username,true));
    }

    @Override
    public void onFriendRequestDeclined(String username) {
        //好友请求被拒绝
//        Logger.d("好友请求被拒绝");
        DbContactsHelper.upDateAccount2Friend(username,false);
        EventBus.getDefault().post(new ContactListenerBean(username,false));
    }


    private void contactAdd(String username, String reason){
        MessageEncryptBean messageEncryptBean = new MessageEncryptBean();

        try {
            String msgJson = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),reason);
            messageEncryptBean = new Gson().fromJson(msgJson, MessageEncryptBean.class);
        } catch (Exception e) {

            messageEncryptBean.setUserTag("");
            messageEncryptBean.setUserName(username+" - 信息丢失");
            messageEncryptBean.setUserHead(RandomConstant.randomHeadImg());
            messageEncryptBean.setUserIntro("该用户消息丢失在酒杯里~");
            messageEncryptBean.setTextMsgContent(PConstant.fartQuotes());

            e.printStackTrace();
        }

        //存储用户信息
        UserInfoBean userInfoBean= new UserInfoBean();
        userInfoBean.setUserTag(messageEncryptBean.getUserTag());
        userInfoBean.setUserAccount(username);
        userInfoBean.setUserName(messageEncryptBean.getUserName());
        userInfoBean.setUserHead(messageEncryptBean.getUserHead());
        userInfoBean.setUserIntro(messageEncryptBean.getUserIntro());
        userInfoBean.setIsGroup(false);
        DbUserHelper.insertUser(userInfoBean);

        //存储联系人信息
        DbContactsHelper.insetContacts(DbContactsHelper.createContact(username,true));
    }




}
