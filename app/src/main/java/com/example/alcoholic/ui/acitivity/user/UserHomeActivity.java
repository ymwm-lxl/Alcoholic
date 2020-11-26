package com.example.alcoholic.ui.acitivity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.ContactListenerBean;
import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.JumpDataContants;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.im.SendMsg;
import com.example.alcoholic.ui.acitivity.ChatActivity;
import com.example.alcoholic.ui.dialog.InputDialog;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.base.BaseDialog;
import com.example.widget.layout.SettingBar;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.Call;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_CHAT_TYPE;
import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:用户主页
 * on 2020/11/18.
 */
public class UserHomeActivity extends MyActivity {


    private ImageView mIvHeadImg;
    private TextView mTvName;
    private TextView mTvCodeName;
    private TextView mTvAccount;
    private TextView mTvIntro;

    private SettingBar mBarSetCodeName;
    private SettingBar mBarOpenChat;
    private SettingBar mBarAddFriend;
    private SettingBar mBarDeleteFriend;
    private SettingBar mBarAddBlack;
    private SettingBar mBarDeleteBlack;

    private String mUserAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_home;
    }

    @Override
    protected void initView() {
        mIvHeadImg = findViewById(R.id.userHome_iv_headImg);
        mTvName = findViewById(R.id.userHome_tv_name);
        mTvCodeName = findViewById(R.id.userHome_tv_codename);
        mTvAccount = findViewById(R.id.userHome_tv_account);
        mTvIntro = findViewById(R.id.userHome_tv_intro);
        mBarSetCodeName = findViewById(R.id.userHome_bar_setCodeName);
        mBarOpenChat = findViewById(R.id.userHome_bar_openChat);
        mBarAddFriend = findViewById(R.id.userHome_bar_addFriend);
        mBarDeleteFriend = findViewById(R.id.userHome_bar_deleteFriend);
        mBarAddBlack = findViewById(R.id.userHome_bar_addBlack);
        mBarDeleteBlack = findViewById(R.id.userHome_bar_deleteBlack);

        setOnClickListener(mBarSetCodeName,mBarOpenChat,mBarAddFriend
                ,mBarDeleteFriend,mBarAddBlack,mBarDeleteBlack);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取到新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFriendChange(ContactListenerBean contactListenerBean) {
        if (contactListenerBean.getUsername().equals(mUserAccount)){
            initUserInfo();
        }
    };


    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras() != null){
            mUserAccount = getIntent().getExtras().getString(JumpDataContants.JUMP_DATA_USER_ACCOUNT,"");
        }
        if (EmptyUtils.isEmpty(mUserAccount) ){
            toast("找不到该用户");
            finish();
        }

        initUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userHome_bar_setCodeName:
                //设置备注
                setCodeName();
                break;
            case R.id.userHome_bar_openChat:
                //发消息
                Intent intent = new Intent(getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,mUserAccount);
                bundle.putInt(JUMP_DATA_CHAT_TYPE, IMConstant.CHAT_TYPE_CHAT);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.userHome_bar_addFriend:
                //加好友
                addFriend();
                break;
            case R.id.userHome_bar_deleteFriend:
                //删除好友
                deleteFriend();
                break;
            case R.id.userHome_bar_addBlack:
                //加入黑名单

                break;
            case R.id.userHome_bar_deleteBlack:
                //移出黑名单

                break;

        }
    }

    /**
     * 展示用户信息
     */
    private void initUserInfo(){

        UserInfoBean userInfoBean = DbUserHelper.queryUser2Account(mUserAccount);

        ContactsBean contactsBean = DbContactsHelper.queryContacts2Account(mUserAccount);

        if (contactsBean != null) {
            //有通讯录关系
            if (EmptyUtils.isNotEmpty(contactsBean.getCodeName())) {
                mTvCodeName.setText(String.format("备注：%s", contactsBean.getCodeName()));
            }
        }
        //根据状态显示操作按钮
        barStatusIsShow(contactsBean);


        mTvAccount.setText(String.format("账号：%s",mUserAccount));

        if (userInfoBean == null){
            GlideApp.with(this)
                    .load(R.drawable.icon)
                    .circleCrop()
                    .into(mIvHeadImg);

            mTvName.setText(mUserAccount);

            return;
        }

        GlideApp.with(this)
                .load(userInfoBean.getUserHead())
                .circleCrop()
                .into(mIvHeadImg);

        mTvName.setText(userInfoBean.getUserName());
        mTvIntro.setText(userInfoBean.getUserIntro());

    }


    /**
     * 重新根据状态展示操作按钮
     * @param contactsBean
     */
    private void barStatusIsShow(ContactsBean contactsBean){
        if (contactsBean != null) {

            if (contactsBean.getIsFriend()){
                //好友关系
                if (contactsBean.getIsBlack()){
                    //拉黑
                    mBarOpenChat.setVisibility(View.GONE);
                    mBarAddFriend.setVisibility(View.GONE);
                    mBarDeleteFriend.setVisibility(View.GONE);
                    mBarAddBlack.setVisibility(View.GONE);
                    mBarDeleteBlack.setVisibility(View.VISIBLE);
                }else {
                    //正常好友
                    mBarOpenChat.setVisibility(View.VISIBLE);
                    mBarAddFriend.setVisibility(View.GONE);
                    mBarDeleteFriend.setVisibility(View.VISIBLE);
                    mBarAddBlack.setVisibility(View.VISIBLE);
                    mBarDeleteBlack.setVisibility(View.GONE);
                }
            }else {
                mBarOpenChat.setVisibility(View.GONE);
                mBarAddFriend.setVisibility(View.VISIBLE);
                mBarDeleteFriend.setVisibility(View.GONE);
                mBarAddBlack.setVisibility(View.GONE);
                mBarDeleteBlack.setVisibility(View.GONE);
            }

        }else {
            //没有通讯录关系
            mBarOpenChat.setVisibility(View.GONE);
            mBarAddFriend.setVisibility(View.VISIBLE);
            mBarDeleteFriend.setVisibility(View.GONE);
            mBarAddBlack.setVisibility(View.GONE);
            mBarDeleteBlack.setVisibility(View.GONE);

        }



    }


    /**
     * 设置备注
     */
    private void setCodeName(){

        new InputDialog.Builder(this)
                .setTitle("设置备注")
                .setHint("请输入要给你好友设置的备注")
                .setContent(DbContactsHelper.queryCodeName2Account(mUserAccount))
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length() == 0 || content.trim().length() >16){
                            toast("备注长度限制为 0 - 16 位。");
                            return;
                        }

                        DbContactsHelper.updateUserCodeName(mUserAccount,content.trim());

                        initUserInfo();
                    }
                })
                .show();

    }

    /**
     * 加好友
     */
    private void addFriend(){
        SendMsg.sendFriendAdd(mUserAccount, "", new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新页面
                        initUserInfo();
                        toast("好友请求已发送，如果对方在线，则可自动成为好友");
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                toast("好友请求发送失败："+code+"-"+error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 删除好友
     */
    private void deleteFriend(){
        SendMsg.deleteFriend(mUserAccount, new EMCallBack() {
            @Override
            public void onSuccess() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //修改数据库中的好友关系
                        DbContactsHelper.upDateAccount2Friend(mUserAccount,false);
                        toast("好友已删除");
                        initUserInfo();
                    }
                });
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }


}
