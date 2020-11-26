package com.example.alcoholic.utils;

import android.text.format.Time;

import com.tencent.mmkv.MMKV;

/**
 * Created by
 * Description:mmkv 系统信息
 * on 2020/11/16.
 */
public class MMKVStytemUtils {

    private static MMKVStytemUtils mInstance;

    private static MMKV mKv;

    /**
     * 密文密钥
     * 危机时刻：存储当前时间戳，如果在当前时间 +1 天内，则处于危急时刻
     */
    private static final String STYTEM_AES_KEY = "stytemAesKey";
    private static final String STYTEM_ALARM_TIME = "stytemAlarmTime";


    public MMKVStytemUtils() {
        mKv = MMKV.defaultMMKV();
    }

    /**
     * 单例
     */
    public static MMKVStytemUtils getInstance(){
        if (mInstance == null) {
            synchronized (MMKVStytemUtils.class) {
                if (mInstance == null) {
                    mInstance = new MMKVStytemUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 保存当前的密文密钥
     */
    public void saveAesKey(String key){
        mKv.encode(STYTEM_AES_KEY,key);
    }

    /**
     * 获取当前的密文密钥
     */
    public String getAesKey(){
        return mKv.decodeString(STYTEM_AES_KEY,"1234567890123456");
    }

    /**
     * 开启危机时刻
     */
    public void openAlarmTime(){
        mKv.encode(STYTEM_ALARM_TIME, TimeUtils.getNowMills());
    }

    /**
     * 关闭危机时刻,(时间清零)
     */
    public void closeAlarmTime(){
        mKv.encode(STYTEM_ALARM_TIME,0);
    }

    /**
     * 获取是否处于危急时刻
     */
    public boolean isAlarmTime(){
        long diff = TimeUtils.getNowMills() - mKv.decodeLong(STYTEM_ALARM_TIME,0);
        if (diff <= TimeUtils.TimeConstants.DAY){
            return true;
        }
        return false;
    }



}
