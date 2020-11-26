package com.example.alcoholic.db;

import com.example.alcoholic.common.MyApplication;
import com.example.alcoholic.gen.DaoMaster;
import com.example.alcoholic.gen.DaoSession;

/**
 * Created by
 * Description: 消息存库管理类
 * on 2020/11/17.
 */
public class DbMsgManager {
    private static final String db_name = "message_db";

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DbMsgManager() {
        init();
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DbMsgManager INSTANCE = new DbMsgManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static DbMsgManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
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
