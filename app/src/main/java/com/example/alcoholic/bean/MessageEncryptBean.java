package com.example.alcoholic.bean;

/**
 * Created by
 * Description:消息体-加密处理
 * on 2020/11/16.
 */
public class MessageEncryptBean {
    /* 用户信息 TAG */
    private String userTag;
    /* 用户昵称 */
    private String userName;
    /* 用户头像 */
    private String userHead;
    /* 用户个签 */
    private String userIntro;
    /* 文字消息内容 */
    private String textMsgContent;
    /* 图片消息内容 */
    private String imgMsgContent;

    public String getUserTag() {
        return userTag;
    }

    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public String getUserIntro() {
        return userIntro;
    }

    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }

    public String getTextMsgContent() {
        return textMsgContent;
    }

    public void setTextMsgContent(String textMsgContent) {
        this.textMsgContent = textMsgContent;
    }

    public String getImgMsgContent() {
        return imgMsgContent;
    }

    public void setImgMsgContent(String imgMsgContent) {
        this.imgMsgContent = imgMsgContent;
    }
}
