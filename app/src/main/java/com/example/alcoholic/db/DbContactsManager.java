package com.example.alcoholic.db;

import com.example.alcoholic.common.MyApplication;
import com.example.alcoholic.gen.DaoMaster;
import com.example.alcoholic.gen.DaoSession;

/**
 * Created by
 * Description:联系人关系管理库
 * on 2020/11/18.
 */
public class DbContactsManager {
    private static final String db_name = "Contacts_db";

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DbContactsManager() {
        init();
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final DbContactsManager INSTANCE = new DbContactsManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static DbContactsManager getInstance() {
        return DbContactsManager.SingleInstanceHolder.INSTANCE;
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
