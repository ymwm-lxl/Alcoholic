package com.example.alcoholic.im;

import com.hyphenate.chat.EMMessage;

/**
 * Created by
 * Description: im的相关常量类
 * on 2020/11/16.
 */
public class IMConstant {

    //聊天类型
    public static final int CHAT_TYPE_CHAT = 1 ; //单聊
    public static final int CHAT_TYPE_GROUPCHAT = 2 ; //群聊
    public static final int CHAT_TYPE_CHATROOM = 3 ; //聊天室

    //消息类型
    public static final int MESSAGE_TYPE_TXT = 1;
    public static final int MESSAGE_TYPE_IMAGE = 2;
    public static final int MESSAGE_TYPE_VIDEO = 3;
    public static final int MESSAGE_TYPE_LOCATION = 4;
    public static final int MESSAGE_TYPE_VOICE = 5;
    public static final int MESSAGE_TYPE_FILE = 6;
    public static final int MESSAGE_TYPE_CMD = 7;
    public static final int MESSAGE_TYPE_CUSTOM = 8;

    //消息方向
    public static final int MESSAGE_DIRECT_SEND = 1;
    public static final int MESSAGE_DIRECT_RECEIVE = 2;

    //消息状态
    public static final int MESSAGE_STATUS_SUCCESS = 1;//成功
    public static final int MESSAGE_STATUS_FAIL = 2;//失败
    public static final int MESSAGE_STATUS_INPROGRESS = 3;//发送/接收过程中
    public static final int MESSAGE_STATUS_CREATE = 4;//创建成功待发送


    /**
     * 获取聊天类型
     */
    public static int makeChatType(EMMessage.ChatType chatType){
        switch (chatType){
            case Chat:
                return CHAT_TYPE_CHAT;
            case GroupChat:
                return CHAT_TYPE_GROUPCHAT;
            case ChatRoom:
                return CHAT_TYPE_CHATROOM;
            default:
                return CHAT_TYPE_CHAT;
        }
    }

    /**
     * 获取消息类型
     */
    public static int makeMessageType(EMMessage.Type type){
        switch (type){
            case TXT:
                return MESSAGE_TYPE_TXT;
            case IMAGE:
                return MESSAGE_TYPE_IMAGE;
            case VIDEO:
                return MESSAGE_TYPE_VIDEO;
            case LOCATION:
                return MESSAGE_TYPE_LOCATION;
            case VOICE:
                return MESSAGE_TYPE_VOICE;
            case FILE:
                return MESSAGE_TYPE_FILE;
            case CMD:
                return MESSAGE_TYPE_CMD;
            case CUSTOM:
                return MESSAGE_TYPE_CUSTOM;
            default:
                return MESSAGE_TYPE_TXT;
        }
    }

    /**
     * 获取消息的方向类型
     */
    public static int makeMsgDirect(EMMessage.Direct direct){
        switch (direct){
            case SEND:
                return MESSAGE_DIRECT_SEND;
            case RECEIVE:
                return MESSAGE_DIRECT_RECEIVE;
            default:
                return MESSAGE_DIRECT_SEND;
        }
    }

    /**
     * 获取消息状态
     */
    public static int makeMsgStatus(EMMessage.Status status){
        switch (status){
            case SUCCESS:
                return MESSAGE_STATUS_SUCCESS;
            case FAIL:
                return MESSAGE_STATUS_FAIL;
            case INPROGRESS:
                return MESSAGE_STATUS_INPROGRESS;
            case CREATE:
                return MESSAGE_STATUS_CREATE;
            default:
                return MESSAGE_STATUS_SUCCESS;
        }
    }

}
