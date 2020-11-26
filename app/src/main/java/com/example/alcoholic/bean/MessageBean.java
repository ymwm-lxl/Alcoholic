package com.example.alcoholic.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 消息实体类，把他的消息再本地存一下
 * Description:
 * on 2020/11/16.
 */
@Entity
public class MessageBean {
    /* 数据库id */
    @Id(autoincrement = true)
    private Long id;
    /* 消息id */
    @Unique
    private String msgId;
    /* 消息主体用户/属 */
    private String attribute;
    /* 消息对象 */
    private String otherSide;
    /* 发送者信息 Tag */
    private String fromUserTag;
    /* 发送者账户 */
    private String fromAccount;
    /* 发送者昵称 */
    private String fromName;
    /* 发送者头像 */
    private String fromHead;
    /* 接收者信息tag */
    private String toUserTag;
    /* 接收者账户 */
    private String toAccount;
    /* 接收者昵称 */
    private String toName;
    /* 接收者头像 */
    private String toHead;
    /* 消息内容 */
    private String msgTextContent;
    /* 消息图片内容 */
    private String msgImgContent;
    /* 消息时间 */
    private long msgTime;
    /* 消息被读的人数 */
    private int groupAckCount;
    /* 聊天类型 */
    private int chatType;
    /* 消息类型 */
    private int msgType;
    /* 消息状态 */
    private int status;
    /* 方向 */
    private int direction;

    @Generated(hash = 1294650196)
    public MessageBean(Long id, String msgId, String attribute, String otherSide,
            String fromUserTag, String fromAccount, String fromName,
            String fromHead, String toUserTag, String toAccount, String toName,
            String toHead, String msgTextContent, String msgImgContent,
            long msgTime, int groupAckCount, int chatType, int msgType, int status,
            int direction) {
        this.id = id;
        this.msgId = msgId;
        this.attribute = attribute;
        this.otherSide = otherSide;
        this.fromUserTag = fromUserTag;
        this.fromAccount = fromAccount;
        this.fromName = fromName;
        this.fromHead = fromHead;
        this.toUserTag = toUserTag;
        this.toAccount = toAccount;
        this.toName = toName;
        this.toHead = toHead;
        this.msgTextContent = msgTextContent;
        this.msgImgContent = msgImgContent;
        this.msgTime = msgTime;
        this.groupAckCount = groupAckCount;
        this.chatType = chatType;
        this.msgType = msgType;
        this.status = status;
        this.direction = direction;
    }

    @Generated(hash = 1588632019)
    public MessageBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(String otherSide) {
        this.otherSide = otherSide;
    }

    public String getFromUserTag() {
        return fromUserTag;
    }

    public void setFromUserTag(String fromUserTag) {
        this.fromUserTag = fromUserTag;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromHead() {
        return fromHead;
    }

    public void setFromHead(String fromHead) {
        this.fromHead = fromHead;
    }

    public String getToUserTag() {
        return toUserTag;
    }

    public void setToUserTag(String toUserTag) {
        this.toUserTag = toUserTag;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToHead() {
        return toHead;
    }

    public void setToHead(String toHead) {
        this.toHead = toHead;
    }

    public String getMsgTextContent() {
        return msgTextContent;
    }

    public void setMsgTextContent(String msgTextContent) {
        this.msgTextContent = msgTextContent;
    }

    public String getMsgImgContent() {
        return msgImgContent;
    }

    public void setMsgImgContent(String msgImgContent) {
        this.msgImgContent = msgImgContent;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public int getGroupAckCount() {
        return groupAckCount;
    }

    public void setGroupAckCount(int groupAckCount) {
        this.groupAckCount = groupAckCount;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
