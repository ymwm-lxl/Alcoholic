package com.example.alcoholic.im;

import android.content.Context;

import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.bean.MessageEncryptBean;
import com.example.alcoholic.constant.PConstant;
import com.example.alcoholic.db.DbMsgHelper;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by
 * Description: 发送消息
 * on 2020/11/13.
 */
public class SendMsg {

    /**
     * 发送消息
     */
    public static MessageBean sendText(String content,String toChatUsername){
        return sendText(content,toChatUsername,IMConstant.CHAT_TYPE_CHAT);
    }

    /**
     * 发送消息
     */
    public static MessageBean sendText(String content,String toChatUsername,int chatType){
        MessageEncryptBean messageEncryptBean = new MessageEncryptBean();
        messageEncryptBean.setUserTag(MMKVUserUtils.getInstance().getUserInfoTag());
        messageEncryptBean.setUserName(MMKVUserUtils.getInstance().getUserName());
        messageEncryptBean.setUserHead(MMKVUserUtils.getInstance().getUserHead());
        messageEncryptBean.setUserIntro(MMKVUserUtils.getInstance().getUserIntro());
        messageEncryptBean.setTextMsgContent(content);

        String enContent;
        try {
            enContent = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(messageEncryptBean));
        } catch (Exception e) {
            enContent = PConstant.fartQuotes();
            e.printStackTrace();
        }

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(enContent, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        switch (chatType){
            case IMConstant.CHAT_TYPE_CHAT:
                message.setChatType(EMMessage.ChatType.Chat);
                break;
            case IMConstant.CHAT_TYPE_GROUPCHAT:
                message.setChatType(EMMessage.ChatType.GroupChat);
                break;
            case IMConstant.CHAT_TYPE_CHATROOM:
                message.setChatType(EMMessage.ChatType.ChatRoom);
                break;
            default:
                message.setChatType(EMMessage.ChatType.Chat);
                break;
        }
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);

        return ImBeanChangeHelper.sendMsgCreate(message,messageEncryptBean);
    }


    /**
     * 发送好友请求
     */
    public static void sendFriendAdd(String toAddUsername,String why,final EMCallBack callback){
        String reason;

        MessageEncryptBean messageEncryptBean = new MessageEncryptBean();
        messageEncryptBean.setUserTag(MMKVUserUtils.getInstance().getUserInfoTag());
        messageEncryptBean.setUserName(MMKVUserUtils.getInstance().getUserName());
        messageEncryptBean.setUserHead(MMKVUserUtils.getInstance().getUserHead());
        messageEncryptBean.setUserIntro(MMKVUserUtils.getInstance().getUserIntro());
        messageEncryptBean.setTextMsgContent(why);

        try {
            reason = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(messageEncryptBean));
        } catch (Exception e) {
            reason = PConstant.fartQuotes();
            e.printStackTrace();
        }

        //参数为要添加的好友的username和添加理由
        EMClient.getInstance().contactManager().aysncAddContact(toAddUsername,
                reason,
                callback);
    }


    /**
     * 删除好友
     */
    public static void deleteFriend(String username,final EMCallBack callback){


        EMClient.getInstance().contactManager().aysncDeleteContact(username,callback);

    }

}
