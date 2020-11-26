package com.example.alcoholic.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by
 * Description: 联系人实体类
 * on 2020/11/18.
 */
@Entity
public class ContactsBean {
    /* 数据库id */
    @Id(autoincrement = true)
    private Long id;
    /* 标识，代表关系唯一 */
    @Unique
    private String contactsTag;
    /* 主体用户/属 */
    private String attribute;
    /* 联系人 */
    private String contactsAccount;
    /* 备注 */
    private String codeName;
    /* 是否是好友 */
    private boolean isFriend;
    /* 是否屏蔽 */
    private boolean isShield;
    /* 是否拉黑 */
    private boolean isBlack;
    @Generated(hash = 147408448)
    public ContactsBean(Long id, String contactsTag, String attribute,
            String contactsAccount, String codeName, boolean isFriend,
            boolean isShield, boolean isBlack) {
        this.id = id;
        this.contactsTag = contactsTag;
        this.attribute = attribute;
        this.contactsAccount = contactsAccount;
        this.codeName = codeName;
        this.isFriend = isFriend;
        this.isShield = isShield;
        this.isBlack = isBlack;
    }
    @Generated(hash = 747317112)
    public ContactsBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContactsTag() {
        return this.contactsTag;
    }
    public void setContactsTag(String contactsTag) {
        this.contactsTag = contactsTag;
    }
    public String getAttribute() {
        return this.attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    public String getContactsAccount() {
        return this.contactsAccount;
    }
    public void setContactsAccount(String contactsAccount) {
        this.contactsAccount = contactsAccount;
    }
    public String getCodeName() {
        return this.codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    public boolean getIsFriend() {
        return this.isFriend;
    }
    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }
    public boolean getIsShield() {
        return this.isShield;
    }
    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }
    public boolean getIsBlack() {
        return this.isBlack;
    }
    public void setIsBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }


    
    
}
