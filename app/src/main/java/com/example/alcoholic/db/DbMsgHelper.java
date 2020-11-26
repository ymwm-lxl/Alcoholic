package com.example.alcoholic.db;

import android.database.Cursor;

import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.constant.Constants;
import com.example.alcoholic.gen.MessageBeanDao;
import com.example.alcoholic.im.ImBeanChangeHelper;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.List;

/**
 * Created by
 * Description: 消息数据库帮助类
 * on 2020/11/17.
 */
public class DbMsgHelper {

    /**
     * 插入一条消息
     */
    public static void insertMsg(MessageBean msgBean){
        DbMsgManager.getInstance().getmDaoSession().insertOrReplace(msgBean);
    }


    /**
     * 查询消息 - 所有
     */
    public static List<MessageBean> queryMsgAll(){
        return DbMsgManager.getInstance().getmDaoSession().loadAll(MessageBean.class);
    }

    /**
     * 查询消息 - 根据用户账户
     */
    public static List<MessageBean> queryMsg2User(int pageNum,String userAccount){
        List<MessageBean> messageBeans;

        messageBeans = DbMsgManager.getInstance()
                .getmDaoSession()
                .queryBuilder(MessageBean.class)
//                .whereOr(MessageBeanDao.Properties.Attribute.eq(userAccount),MessageBeanDao.Properties.FromAccount.eq(userAccount))
                .where(MessageBeanDao.Properties.Attribute.eq(MMKVUserUtils.getInstance().getUserAccount())
                        ,MessageBeanDao.Properties.OtherSide.eq(userAccount))
                .orderDesc(MessageBeanDao.Properties.MsgTime)
                .limit(Constants.LOAD_LIMIT)
                .offset(pageNum*Constants.LOAD_LIMIT)
                .build()
                .list();


        Collections.reverse(messageBeans);
        return messageBeans;
    }

    /**
     * 查询会话列表
     */
    public static List<MessageBean> queryChat2Me(){
        List<MessageBean> messageBeans;

        Cursor cursor = DbMsgManager.getInstance().getmDaoSession().getDatabase().rawQuery("SELECT * FROM "
                +MessageBeanDao.TABLENAME + " where ("
                +MessageBeanDao.Properties.OtherSide.columnName + ","
                +MessageBeanDao.Properties.MsgTime.columnName + ") in (SELECT "
                +MessageBeanDao.Properties.OtherSide.columnName + ",max("
                +MessageBeanDao.Properties.MsgTime.columnName+") FROM "
                +MessageBeanDao.TABLENAME + " WHERE "
                +MessageBeanDao.Properties.Attribute.columnName +" ='"
                +MMKVUserUtils.getInstance().getUserAccount() + "' group by "
                +MessageBeanDao.Properties.OtherSide.columnName + ") group by "
                +MessageBeanDao.Properties.OtherSide.columnName + " order by "
                +MessageBeanDao.Properties.MsgTime.columnName + " desc",null);


        messageBeans = ImBeanChangeHelper.cursorMsgToList(cursor,MessageBeanDao.Properties.class);


        return messageBeans;
    }

}
