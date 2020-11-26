package com.example.alcoholic.ui.acitivity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.AnnouncementBean;
import com.example.alcoholic.bean.GroupDescriptionBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.JumpDataContants;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.ui.acitivity.ChatActivity;
import com.example.alcoholic.ui.adapter.GroupMembersSimpleAdapter;
import com.example.alcoholic.ui.dialog.InputDialog;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.base.BaseDialog;
import com.example.widget.layout.SettingBar;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_CHAT_TYPE;
import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:群租首页
 * on 2020/11/19.
 */
public class GroupHomeActivity extends MyActivity {

    private ImageView mIvHead;
    private TextView mTvName;
    private TextView mTvId;
    private TextView mTvIntro;
    private TextView mTvMemberSize;
    private TextView mTvAnnouncement;
    private SettingBar mBarInfo;
    private SettingBar mBarSync;
    private SettingBar mBarAnnouncement;
    private SettingBar mBarOpenChat;
    private RecyclerView mRvMembers;
    private LinearLayout mLayoutAddMember;

    private GroupMembersSimpleAdapter mMembersAdapter;

    private String mGroupId;
    /* 是否是群主 */
    private boolean isOwner = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_home;
    }

    @Override
    protected void initView() {
        mIvHead = findViewById(R.id.groupHome_iv_headImg);
        mTvName = findViewById(R.id.groupHome_tv_name);
        mTvId = findViewById(R.id.groupHome_tv_id);
        mTvIntro = findViewById(R.id.groupHome_tv_intro);
        mTvMemberSize = findViewById(R.id.groupHome_layout_memberSize);
        mBarInfo = findViewById(R.id.groupHome_bar_info);
        mBarSync = findViewById(R.id.groupHome_bar_sync);
        mBarAnnouncement = findViewById(R.id.groupHome_bar_announcement);
        mBarOpenChat = findViewById(R.id.groupHome_bar_openChat);
        mRvMembers = findViewById(R.id.groupHome_rv_members);
        mLayoutAddMember = findViewById(R.id.groupHome_layout_addMember);
        mTvAnnouncement = findViewById(R.id.groupHome_tv_announcement);

        //群成员列表
        initMembersList();

        setOnClickListener(mBarInfo,mBarSync,mBarAnnouncement,mBarOpenChat,mLayoutAddMember);
    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras()!= null){
            mGroupId = getIntent().getExtras().getString(JumpDataContants.JUMP_DATA_USER_ACCOUNT,mGroupId);
        }
        if (EmptyUtils.isEmpty(mGroupId)){
            toast("找不到该群组");
            finish();
        }

        loadGroupInfo();


    }

    private void initMembersList(){
        mMembersAdapter = new GroupMembersSimpleAdapter();

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvMembers.setLayoutManager(layoutManager);
        mRvMembers.setAdapter(mMembersAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupHome_bar_info:
                //群组信息
                enterGroupInfo();
                break;
            case R.id.groupHome_bar_sync:
                //同步
                loadGroupInfo();
                break;
            case R.id.groupHome_bar_announcement:
                //群公告
                if (isOwner){
                    //群主可以修改
                    updateGroupAnnouncement();
                }
                break;
            case R.id.groupHome_layout_addMember:
                //添加成员
                enterAddGroupMember();
                break;
            case R.id.groupHome_bar_openChat:
                Intent intent = new Intent(getContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,mGroupId);
                bundle.putInt(JUMP_DATA_CHAT_TYPE, IMConstant.CHAT_TYPE_GROUPCHAT);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 加载群组信息
     */
    private void loadGroupInfo(){
        showDialog();

        EMClient.getInstance().groupManager().asyncGetGroupFromServer(mGroupId, new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showGroupInfo(value);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });
    }

    /**
     * 展示群组信息
     */
    private void showGroupInfo(EMGroup emGroup){
        //更新群组数据
        DbUserHelper.insertGroup2Bean(emGroup);

        mTvName.setText(emGroup.getGroupName());
        mTvId.setText(String.format("id：%s",mGroupId));

        //aes解密部分
        try {
            String introGson = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),emGroup.getDescription().replaceAll(" ","+"));
            GroupDescriptionBean groupDescriptionBean = new Gson().fromJson(introGson,GroupDescriptionBean.class);

            GlideApp.with(this)
                    .load(groupDescriptionBean.getHeadImg())
                    .circleCrop()
                    .into(mIvHead);
            mTvIntro.setText(groupDescriptionBean.getIntro());


            //群公告
            if (EmptyUtils.isNotEmpty(emGroup.getAnnouncement())){
                String announcementJson = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),emGroup.getAnnouncement().replaceAll(" ","+"));
                AnnouncementBean announcementBean = new Gson().fromJson(announcementJson,AnnouncementBean.class);
                mTvAnnouncement.setText(announcementBean.getAnnouncement());
            }else {
                mTvAnnouncement.setText("暂无");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //群成员
        mTvMemberSize.setText("群成员（"+emGroup.getMemberCount()+"/"+emGroup.getMaxUserCount()+"）");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> memberList = new ArrayList<>();
                EMCursorResult<String> result = null;
                final int pageSize = 20;

                //先插入群主
                memberList.add(emGroup.getOwner());

                //保存是否是群主
                isOwner = emGroup.getOwner().equals(MMKVUserUtils.getInstance().getUserAccount());

                do {
                    try {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(mGroupId,
                                result != null ? result.getCursor() : "", pageSize);
                        memberList.addAll(result.getData());
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMembersAdapter.setNewData(memberList);

                        //控制项
                        if (!isOwner){
                            //非群主则不可以修改信息
                            mBarAnnouncement.setRightIcon(null);
                        }


                        if (isShowDialog()){
                            hideDialog();
                        }
                    }
                });

            }
        }).start();








    }


    /**
     * 进入群组信息
     */
    private void enterGroupInfo(){

        Intent intent = new Intent(this,GroupInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(JumpDataContants.JUMP_DATA_USER_ACCOUNT,mGroupId);
        bundle.putBoolean(JumpDataContants.JUMP_DATA_IS_OWNER,isOwner);
        intent.putExtras(bundle);
        startActivityForResult(intent, new OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                if (resultCode == Activity.RESULT_OK) loadGroupInfo();
            }
        });
    }

    /**
     * 进入添加成员
     */
    private void enterAddGroupMember(){
        Intent intent = new Intent(this,GroupAddMemberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(JumpDataContants.JUMP_DATA_USER_ACCOUNT,mGroupId);
        bundle.putBoolean(JumpDataContants.JUMP_DATA_IS_OWNER,isOwner);
        intent.putExtras(bundle);
        startActivityForResult(intent, new OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                if (resultCode == Activity.RESULT_OK) loadGroupInfo();
            }
        });
    }

    /**
     * 修改群公告
     */
    private void updateGroupAnnouncement(){
        new InputDialog.Builder(this)
                .setTitle("发布群公告")
                .setHint("请输入将要发布的群公告")
                .setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length() <=0 || content.trim().length() > 100){
                            toast("群公告字数限制 0-100 字");
                            return;
                        }

                        AnnouncementBean announcementBean = new AnnouncementBean();
                        announcementBean.setAnnouncement(content);

                        try {
                            String announcement = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(announcementBean));

                            EMClient.getInstance().groupManager().asyncUpdateGroupAnnouncement(mGroupId, announcement, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    loadGroupInfo();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(int code, String error) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
