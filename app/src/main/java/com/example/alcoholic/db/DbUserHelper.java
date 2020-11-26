package com.example.alcoholic.db;

import android.database.sqlite.SQLiteException;
import android.service.autofill.UserData;

import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.bean.GroupDescriptionBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.constant.RandomConstant;
import com.example.alcoholic.gen.UserInfoBeanDao;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by
 * Description:用户信息库操作类
 * on 2020/11/18.
 */
public class DbUserHelper {

    /**
     * 更新用户Tag
     */
    public static void reUserTag(String account){
        UserInfoBean userInfoBean = DbUserManager.getInstance()
                .getmDaoSession()
                .queryBuilder(UserInfoBean.class)
                .where(UserInfoBeanDao.Properties.UserAccount.eq(account))
                .build().list().get(0);

        userInfoBean.setUserTag(MMKVUserUtils.getInstance().getUserInfoTag());

        DbUserManager.getInstance().getmDaoSession().update(userInfoBean);

    }

    /**
     * 更新当前用户信息
     */
    public static void reUserInfo(){
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserTag(MMKVUserUtils.getInstance().getUserInfoTag());
        userInfoBean.setUserAccount(MMKVUserUtils.getInstance().getUserAccount());
        userInfoBean.setUserName(MMKVUserUtils.getInstance().getUserName());
        userInfoBean.setUserHead(MMKVUserUtils.getInstance().getUserHead());
        userInfoBean.setUserIntro(MMKVUserUtils.getInstance().getUserIntro());
        userInfoBean.setIsGroup(false);
        DbUserManager.getInstance().getmDaoSession().insertOrReplace(userInfoBean);

    }

    /**
     * 插入一条用户信息
     */
    public static void insertUser(UserInfoBean userInfoBean){
        DbUserManager.getInstance().getmDaoSession().insertOrReplace(userInfoBean);
    }

    /**
     * 用户登录时获取用户信息
     */
    public static UserInfoBean queryUserIsLogin(String account){
        //查找该用户信息，找到了就返回，找不到则生成新数据
        UserInfoBean userInfoBean = queryUser2Account(account);
        if (userInfoBean == null){
            userInfoBean = new UserInfoBean();
            userInfoBean.setUserTag("");
            userInfoBean.setUserAccount(account);
            userInfoBean.setUserName(account);
            userInfoBean.setUserHead(RandomConstant.randomHeadImg());
            userInfoBean.setUserIntro("");
            insertUser(userInfoBean);
        }
        return userInfoBean;
    }



    /**
     * 根据用户账户，查询
     */
    public static UserInfoBean queryUser2Account(String account){

        List<UserInfoBean> userInfoBeans = DbUserManager.getInstance()
                .getmDaoSession()
                .queryBuilder(UserInfoBean.class)
                .where(UserInfoBeanDao.Properties.UserAccount.eq(account))
                .build().list();

        if (userInfoBeans.size() > 0){
            return userInfoBeans.get(0);
        }else {
            return null;
        }

    }

    /**
     * 根据用户账户，仅查询非群聊
     */
    public static UserInfoBean queryUserNoGroup2Account(String account){

        List<UserInfoBean> userInfoBeans = DbUserManager.getInstance()
                .getmDaoSession()
                .queryBuilder(UserInfoBean.class)
                .where(UserInfoBeanDao.Properties.UserAccount.eq(account), UserInfoBeanDao.Properties.IsGroup.eq(false))
                .build().list();

        if (userInfoBeans.size() > 0){
            return userInfoBeans.get(0);
        }else {
            return null;
        }

    }

    /**
     * 获取用户昵称
     */
    public static String getUserName(String account){
        UserInfoBean userInfoBean = queryUser2Account(account);

        if (userInfoBean != null){
            return userInfoBean.getUserName();
        }else {
            return account;
        }
    }


    /**
     * 判断展示用户昵称还是备注
     */
    public static String getUserShowName(String account,String name){
        String codeName = DbContactsHelper.queryCodeName2Account(account);

        if (EmptyUtils.isNotEmpty(codeName)){
            return codeName;
        }else {
            return name;
        }

    }


    /**
     * 根据群组id 获取群组数据
     */
    public static UserInfoBean queryGroupInfo2Id(String groupId){

        List<UserInfoBean> userInfoBeans = DbUserManager.getInstance()
                .getmDaoSession()
                .queryBuilder(UserInfoBean.class)
                .where(UserInfoBeanDao.Properties.UserAccount.eq(groupId),UserInfoBeanDao.Properties.IsGroup.eq(true))
                .build().list();

        UserInfoBean userInfoBean = null;
        if (userInfoBeans.size() > 0 ){
            userInfoBean = userInfoBeans.get(0);
        }

        if (userInfoBean == null || EmptyUtils.isEmpty(userInfoBean.getUserTag().trim())){
            //根据群组ID从服务器获取群组基本信息
            try {
                EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);

                userInfoBean = insertGroup2Bean(group);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userInfoBean;
    }


    /**
     * 插入群组信息
     */
    public static UserInfoBean insertGroup2Bean(EMGroup group){

        UserInfoBean userInfoBean = new UserInfoBean();


        String description;
        try {
            description = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),group.getDescription().replaceAll(" ","+"));

            GroupDescriptionBean groupDescription = new Gson().fromJson(description, GroupDescriptionBean.class);

            userInfoBean.setUserTag(groupDescription.getInfoTag());
            userInfoBean.setUserHead(groupDescription.getHeadImg());
            userInfoBean.setUserIntro(groupDescription.getIntro());

        } catch (Exception e) {
            userInfoBean.setUserTag("");
            userInfoBean.setUserHead("");
            userInfoBean.setUserIntro("");
            e.printStackTrace();
        }

        userInfoBean.setUserAccount(group.getGroupId());
        userInfoBean.setUserName(group.getGroupName());
        userInfoBean.setIsGroup(true);


        insertUser(userInfoBean);


        return userInfoBean;

    }

}
