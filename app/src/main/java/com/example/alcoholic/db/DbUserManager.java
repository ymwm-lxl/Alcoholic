package com.example.alcoholic.db;

import com.example.alcoholic.common.MyApplication;
import com.example.alcoholic.gen.DaoMaster;
import com.example.alcoholic.gen.DaoSession;

/**
 * Created by
 * Description:各种用户信息存储管理类
 * on 2020/11/18.
 */
public class DbUserManager {
    private static final String db_name = "user_all_db";

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DbUserManager() {
        init();
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DbUserManager INSTANCE = new DbUserManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static DbUserManager getInstance() {
        return DbUserManager.SingleInstanceHolder.INSTANCE;
    }

    /**
     * 初始化数据
     */
    private void init() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(MyApplication.getApplication(),
                db_name);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }
    public DaoSession getmDaoSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }


}
