package com.example.alcoholic.bean;

/**
 * Created by
 * Description: 联系人变化- 用于eventbus通知页面有新联系人变化
 * on 2020/11/19.
 */
public class ContactListenerBean {
    private boolean isFriend;
    private String username;


    public ContactListenerBean(String username,boolean isFriend) {
        this.username = username;
        this.isFriend = isFriend;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
