package com.example.alcoholic.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by
 * Description:用户信息实体嘞
 * on 2020/11/18.
 */
@Entity
public class UserInfoBean {
    /* 数据库id */
    @Id(autoincrement = true)
    private Long id;
    /* 用户 Tag */
    private String userTag;
    /* 用户账户 */
    @Unique
    private String userAccount;
    /* 用户昵称 */
    private String userName;
    /* 用户头像 */
    private String userHead;
    /* 用户签名 */
    private String userIntro;
    /* 用户聊天背景 */
    private String chatBgImg;
    /* 用户是否是群组 */
    private boolean isGroup;
    @Generated(hash = 1985144718)
    public UserInfoBean(Long id, String userTag, String userAccount,
            String userName, String userHead, String userIntro, String chatBgImg,
            boolean isGroup) {
        this.id = id;
        this.userTag = userTag;
        this.userAccount = userAccount;
        this.userName = userName;
        this.userHead = userHead;
        this.userIntro = userIntro;
        this.chatBgImg = chatBgImg;
        this.isGroup = isGroup;
    }
    @Generated(hash = 1818808915)
    public UserInfoBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserTag() {
        return this.userTag;
    }
    public void setUserTag(String userTag) {
        this.userTag = userTag;
    }
    public String getUserAccount() {
        return this.userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserHead() {
        return this.userHead;
    }
    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
    public String getUserIntro() {
        return this.userIntro;
    }
    public void setUserIntro(String userIntro) {
        this.userIntro = userIntro;
    }
    public String getChatBgImg() {
        return this.chatBgImg;
    }
    public void setChatBgImg(String chatBgImg) {
        this.chatBgImg = chatBgImg;
    }
    public boolean getIsGroup() {
        return this.isGroup;
    }
    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    


}
