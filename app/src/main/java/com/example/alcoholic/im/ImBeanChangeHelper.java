package com.example.alcoholic.im;

import android.database.Cursor;

import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.bean.MessageEncryptBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.constant.PConstant;
import com.example.alcoholic.constant.RandomConstant;
import com.example.alcoholic.db.DbMsgHelper;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.gen.MessageBeanDao;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by
 * Description:im 实体类转换工具类
 * on 2020/11/17.
 */
public class ImBeanChangeHelper {

    /**
     * 收到消息时创建实体嘞
     */
    public static MessageBean msgBeanChange(EMMessage emBean){
        MessageBean messageBean = new MessageBean();

        MessageEncryptBean messageEncryptBean = new MessageEncryptBean();
        try {
            String msgJson = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),((EMTextMessageBody)emBean.getBody()).getMessage());
            messageEncryptBean = new Gson().fromJson(msgJson, MessageEncryptBean.class);
        } catch (Exception e) {


            messageEncryptBean.setUserTag("");
            messageEncryptBean.setUserHead(RandomConstant.randomHeadImg());
            messageEncryptBean.setTextMsgContent(PConstant.fartQuotes());
            if (emBean.getChatType() == EMMessage.ChatType.Chat){
                //单聊
                messageEncryptBean.setUserName(emBean.getFrom()+" - 信息丢失");
                messageEncryptBean.setUserIntro("该用户消息丢失在酒杯里~");
            }else {
                //群组
                messageEncryptBean.setUserName(emBean.getTo()+" - 群组信息丢失");
                messageEncryptBean.setUserIntro("该群组消息丢失在酒杯里~");
            }

            e.printStackTrace();
        }

        messageBean.setMsgId(emBean.getMsgId());
        messageBean.setAttribute(MMKVUserUtils.getInstance().getUserAccount());
        if (emBean.getChatType() == EMMessage.ChatType.Chat){
            //会话
            messageBean.setOtherSide(emBean.getFrom());
            messageBean.setToUserTag(MMKVUserUtils.getInstance().getUserInfoTag());
            messageBean.setToAccount(emBean.getTo());
            messageBean.setToName(MMKVUserUtils.getInstance().getUserName());
            messageBean.setToHead(MMKVUserUtils.getInstance().getUserHead());
        }else {
            //群聊
            UserInfoBean groupInfo = DbUserHelper.queryGroupInfo2Id(emBean.getTo());
            messageBean.setOtherSide(emBean.getTo());
            messageBean.setToAccount(emBean.getTo());

            //如果存得有该群聊的信息，则直接使用该群聊信息，
            if (EmptyUtils.isNotEmpty(groupInfo)) {
                messageBean.setToUserTag(groupInfo.getUserTag());
                messageBean.setToName(groupInfo.getUserName());
                messageBean.setToHead(groupInfo.getUserHead());
            }else {
                //没有该群聊信息
                //根据群组ID从服务器获取群组基本信息
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(emBean.getTo());

                    //更新群组数据
                    UserInfoBean newGroupInfo = DbUserHelper.insertGroup2Bean(group);
                    messageBean.setToUserTag(newGroupInfo.getUserTag());
                    messageBean.setToName(newGroupInfo.getUserName());
                    messageBean.setToHead(newGroupInfo.getUserHead());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

        }

        messageBean.setFromAccount(emBean.getUserName());
        messageBean.setFromUserTag(messageEncryptBean.getUserTag());
        messageBean.setFromName(messageEncryptBean.getUserName());
        messageBean.setFromHead(messageEncryptBean.getUserHead());
        messageBean.setMsgTextContent(messageEncryptBean.getTextMsgContent());
        messageBean.setMsgImgContent(messageEncryptBean.getImgMsgContent());
        messageBean.setMsgTime(emBean.getMsgTime());
        messageBean.setGroupAckCount(emBean.groupAckCount());
        messageBean.setChatType(IMConstant.makeChatType(emBean.getChatType()));
        messageBean.setMsgType(IMConstant.makeMessageType(emBean.getType()));
        messageBean.setStatus(IMConstant.makeMsgStatus(emBean.status()));
        messageBean.setDirection(IMConstant.makeMsgDirect(emBean.direct()));

        //顺便存储用户信息
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserTag(messageEncryptBean.getUserTag());
        userInfoBean.setUserAccount(emBean.getUserName());
        userInfoBean.setUserName(messageEncryptBean.getUserName());
        userInfoBean.setUserHead(messageEncryptBean.getUserHead());
        userInfoBean.setUserIntro(messageEncryptBean.getUserIntro());
        userInfoBean.setIsGroup(false);
        DbUserHelper.insertUser(userInfoBean);

        return messageBean;
    }

    /**
     * 发送消息时创建实体嘞
     */
    public static MessageBean sendMsgCreate(EMMessage message, MessageEncryptBean messageEncryptBean){
        UserInfoBean userInfoBean = DbUserHelper.queryUser2Account(message.getTo());

        MessageBean messageBean = new MessageBean();

        messageBean.setMsgId(message.getMsgId());
        messageBean.setAttribute(MMKVUserUtils.getInstance().getUserAccount());
        messageBean.setOtherSide(message.getTo());
        messageBean.setFromUserTag(MMKVUserUtils.getInstance().getUserInfoTag());
        messageBean.setFromAccount(MMKVUserUtils.getInstance().getUserAccount());
        messageBean.setFromName(MMKVUserUtils.getInstance().getUserName());
        messageBean.setFromHead(MMKVUserUtils.getInstance().getUserHead());
        messageBean.setToAccount(message.getTo());
        if (userInfoBean!= null){
            messageBean.setToUserTag(userInfoBean.getUserTag());
            messageBean.setToName(userInfoBean.getUserName());
            messageBean.setToHead(userInfoBean.getUserHead());
        }
        messageBean.setMsgTextContent(messageEncryptBean.getTextMsgContent());
//        messageBean.setMsgImgContent();
        messageBean.setMsgTime(message.getMsgTime());
        messageBean.setGroupAckCount(0);
        messageBean.setChatType(IMConstant.makeChatType(message.getChatType()));
        messageBean.setMsgType(IMConstant.makeMessageType(message.getType()));
        messageBean.setStatus(IMConstant.makeMsgStatus(message.status()));
        messageBean.setDirection(IMConstant.makeMsgDirect(message.direct()));

        DbMsgHelper.insertMsg(messageBean);
        return messageBean;
    }

    /**
     * 游标切换
     */
    public static ArrayList cursorMsgToList(Cursor cursor, Class objClass){

        ArrayList objList = new ArrayList();

        int idIndex = cursor.getColumnIndex(MessageBeanDao.Properties.Id.columnName);
        int msgIdIndex = cursor.getColumnIndex(MessageBeanDao.Properties.MsgId.columnName);
        int attributeIndex = cursor.getColumnIndex(MessageBeanDao.Properties.Attribute.columnName);
        int otherSideIndex = cursor.getColumnIndex(MessageBeanDao.Properties.OtherSide.columnName);
        int fromUserTagIndex = cursor.getColumnIndex(MessageBeanDao.Properties.FromUserTag.columnName);
        int fromAccountIndex = cursor.getColumnIndex(MessageBeanDao.Properties.FromAccount.columnName);
        int fromNameIndex = cursor.getColumnIndex(MessageBeanDao.Properties.FromName.columnName);
        int fromHeadIndex = cursor.getColumnIndex(MessageBeanDao.Properties.FromHead.columnName);
        int toUserTagIndex = cursor.getColumnIndex(MessageBeanDao.Properties.ToUserTag.columnName);
        int toAccountIndex = cursor.getColumnIndex(MessageBeanDao.Properties.ToAccount.columnName);
        int toNameIndex = cursor.getColumnIndex(MessageBeanDao.Properties.ToName.columnName);
        int toHeadIndex = cursor.getColumnIndex(MessageBeanDao.Properties.ToHead.columnName);
        int msgTextContentIndex = cursor.getColumnIndex(MessageBeanDao.Properties.MsgTextContent.columnName);
        int msgImgContentIndex = cursor.getColumnIndex(MessageBeanDao.Properties.MsgImgContent.columnName);
        int msgTimeIndex = cursor.getColumnIndex(MessageBeanDao.Properties.MsgTime.columnName);
        int groupAckCountIndex = cursor.getColumnIndex(MessageBeanDao.Properties.GroupAckCount.columnName);
        int chatTypeIndex = cursor.getColumnIndex(MessageBeanDao.Properties.ChatType.columnName);
        int msgTypeIndex = cursor.getColumnIndex(MessageBeanDao.Properties.MsgType.columnName);
        int statusIndex = cursor.getColumnIndex(MessageBeanDao.Properties.Status.columnName);
        int directionIndex = cursor.getColumnIndex(MessageBeanDao.Properties.Direction.columnName);

        while (cursor.moveToNext()){
            MessageBean messageBean = new MessageBean();

            messageBean.setId(cursor.getLong(idIndex));
            messageBean.setMsgId(cursor.getString(msgIdIndex));
            messageBean.setAttribute(cursor.getString(attributeIndex));
            messageBean.setOtherSide(cursor.getString(otherSideIndex));
            messageBean.setFromUserTag(cursor.getString(fromUserTagIndex));
            messageBean.setFromAccount(cursor.getString(fromAccountIndex));
            messageBean.setFromName(cursor.getString(fromNameIndex));
            messageBean.setFromHead(cursor.getString(fromHeadIndex));
            messageBean.setToUserTag(cursor.getString(toUserTagIndex));
            messageBean.setToAccount(cursor.getString(toAccountIndex));
            messageBean.setToName(cursor.getString(toNameIndex));
            messageBean.setToHead(cursor.getString(toHeadIndex));
            messageBean.setMsgTextContent(cursor.getString(msgTextContentIndex));
            messageBean.setMsgImgContent(cursor.getString(msgImgContentIndex));
            messageBean.setMsgTime(cursor.getLong(msgTimeIndex));
            messageBean.setGroupAckCount(cursor.getInt(groupAckCountIndex));
            messageBean.setChatType(cursor.getInt(chatTypeIndex));
            messageBean.setMsgType(cursor.getInt(msgTypeIndex));
            messageBean.setStatus(cursor.getInt(statusIndex));
            messageBean.setDirection(cursor.getInt(directionIndex));

            objList.add(messageBean);
        }
        return objList;
    }
}
