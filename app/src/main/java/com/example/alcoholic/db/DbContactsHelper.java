package com.example.alcoholic.db;

import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.gen.ContactsBeanDao;
import com.example.alcoholic.utils.MMKVUserUtils;

import java.util.List;

/**
 * Created by
 * Description:
 * on 2020/11/18.
 */
public class DbContactsHelper {

    /**
     * 创建一个新的联系人
     */
    public static ContactsBean createContact(String account,boolean isfriend){
        ContactsBean contactsBean = new ContactsBean();
        contactsBean.setAttribute(MMKVUserUtils.getInstance().getUserAccount());
        contactsBean.setContactsAccount(account);
        contactsBean.setCodeName("");
        contactsBean.setIsFriend(isfriend);
        contactsBean.setIsShield(false);
        contactsBean.setIsBlack(false);
        contactsBean.setContactsTag(contactsBean.getAttribute()+"-"+contactsBean.getContactsAccount());

        return contactsBean;
    }

    /**
     * 插入一条记录
     */
    public static void insetContacts(ContactsBean contactsBean){
        DbContactsManager.getInstance().getmDaoSession().insertOrReplace(contactsBean);
    }


    /**
     * 查询所有联系人
     */
    public static List<ContactsBean> queryContactsAll(){
        return DbContactsManager.getInstance().getmDaoSession().queryBuilder(ContactsBean.class)
                .where(ContactsBeanDao.Properties.Attribute.eq(MMKVUserUtils.getInstance().getUserAccount())).build().list();
    }

    /**
     * 查询联系人 - 根据用户账户
     */
    public static ContactsBean queryContacts2Account(String account){

        List<ContactsBean> contactsBeans = DbContactsManager.getInstance().getmDaoSession()
                .queryBuilder(ContactsBean.class)
                .where(ContactsBeanDao.Properties.Attribute.eq(MMKVUserUtils.getInstance().getUserAccount())
                        ,ContactsBeanDao.Properties.ContactsAccount.eq(account)).build().list();

        if (contactsBeans.size() > 0){
            return contactsBeans.get(0);
        }else {
            return null;
        }

    }

    /**
     * 查询好友备注
     */
    public static String queryCodeName2Account(String account){
        List<ContactsBean> contactsBeans = DbContactsManager.getInstance().getmDaoSession()
                .queryBuilder(ContactsBean.class)
                .where(ContactsBeanDao.Properties.Attribute.eq(MMKVUserUtils.getInstance().getUserAccount())
                        ,ContactsBeanDao.Properties.ContactsAccount.eq(account)).build().list();

        if (contactsBeans.size() > 0){
            return contactsBeans.get(0).getCodeName();
        }else {
            return "";
        }
    }

    /**
     * 修改好友备注
     */
    public static void updateUserCodeName(String account,String codeName){
        ContactsBean contactsBean = queryContacts2Account(account);
        if (contactsBean == null){
            contactsBean = createContact(account,false);
        }

        contactsBean.setCodeName(codeName);
        DbContactsManager.getInstance().getmDaoSession().update(contactsBean);

    }

    /**
     * 根据用户id修改好友状态
     */
    public static void upDateAccount2Friend(String account,boolean isfriend){
        ContactsBean contactsBean = queryContacts2Account(account);

        if (contactsBean != null){
            contactsBean.setIsFriend(isfriend);
        }else {
            contactsBean = createContact(account,isfriend);
        }

        DbContactsManager.getInstance().getmDaoSession().insertOrReplace(contactsBean);


    }

}
