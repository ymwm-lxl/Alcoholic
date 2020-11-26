package com.example.alcoholic.utils;

import com.tencent.mmkv.MMKV;

/**
 * Created by
 * Description:
 * on 2020/11/16.
 */
public class MMKVUserUtils {

    private static MMKVUserUtils mInstance;
    private static MMKV mUserKv;

    /**
     * 信息 TAG ，可以理解为用户信息的版本号 Alcoholic+时间戳
     * 账户
     * 密码
     * 昵称
     * 头像
     * 签名
     */
    private static final String USER_TAG = "userTag";
    private static final String USER_ACCOUNT = "userAccount";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USER_NAME = "userName";
    private static final String USER_HEAD = "userHead";
    private static final String USER_INTRO = "userIntro";

    public MMKVUserUtils() {
        mUserKv = MMKV.mmkvWithID("userInfo");
    }

    /**
     * 单例
     */
    public static MMKVUserUtils getInstance(){
        if (mInstance == null) {
            synchronized (MMKVUserUtils.class) {
                if (mInstance == null) {
                    mInstance = new MMKVUserUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 刷新信息 TAG
     */
    public void reUserInfoTag(){
        mUserKv.encode(USER_TAG, createTag());
    }

    /**
     * 生成一个TAG
     */
    public String createTag(){
        return "Alcoholic"+ TimeUtils.getNowMills();
    }

    /**
     * 获取信息 TAG
     */
    public String getUserInfoTag(){
        return mUserKv.decodeString(USER_TAG,"");
    }


    /**
     * 判断是否登录
     */
    public boolean isLogin(){
        if (EmptyUtils.isEmpty(getUserAccount())){
            return false;
        }

        if (EmptyUtils.isEmpty(getUserPsd())){
            return false;
        }

        return true;
    }

    /**
     * 保存账户信息
     */
    public void saveUserAccount(String userAccount){
        mUserKv.encode(USER_ACCOUNT,userAccount);
        reUserInfoTag();
    }

    /**
     * 读取用户账户
     */
    public String getUserAccount(){
        return mUserKv.decodeString(USER_ACCOUNT,"");
    }

    /**
     * 保存用户密码
     */
    public void saveUserPsd(String password){
        mUserKv.encode(USER_PASSWORD,password);
        reUserInfoTag();
    }

    /**
     * 读取用户密码
     */
    public String getUserPsd(){
        return mUserKv.decodeString(USER_PASSWORD,"");
    }

    /**
     * 保存用户昵称
     */
    public void saveUserName(String name){
        mUserKv.encode(USER_NAME,name);
        reUserInfoTag();
    }

    /**
     * 读取用户昵称
     */
    public String getUserName(){
        return mUserKv.decodeString(USER_NAME,"");
    }

    /**
     * 保存用户头像
     */
    public void saveUserHead(String head){
        mUserKv.encode(USER_HEAD,head);
        reUserInfoTag();
    }

    /**
     * 读取用户头像
     */
    public String getUserHead(){
        return mUserKv.decodeString(USER_HEAD,"");
    }

    /**
     * 保存用户简介
     */
    public void saveUserIntro(String intro){
        mUserKv.encode(USER_INTRO,intro);
        reUserInfoTag();
    }

    /**
     * 读取用户简介
     */
    public String getUserIntro(){
        return mUserKv.decodeString(USER_INTRO,"");
    }

    /**
     * 清除所有数据
     */
    public void clearAll(){
        mUserKv.clearAll();
    }


}
