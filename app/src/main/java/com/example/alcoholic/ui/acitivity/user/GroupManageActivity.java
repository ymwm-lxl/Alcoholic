package com.example.alcoholic.ui.acitivity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.GroupDescriptionBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.RandomConstant;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:群组管理页面
 * on 2020/11/19.
 */
public class GroupManageActivity extends MyActivity {

    private LinearLayout mLayoutCreate;
    private LinearLayout mLayoutAdd;
    private RecyclerView mRvGroup;

    private BaseQuickAdapter<EMGroup,BaseViewHolder> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manage;

    }

    @Override
    protected void initView() {
        mLayoutCreate = findViewById(R.id.groupManage_layout_createGroup);
        mLayoutAdd = findViewById(R.id.groupManage_layout_addGroup);
        mRvGroup = findViewById(R.id.groupManage_rv_group);

        setOnClickListener(mLayoutCreate,mLayoutAdd);

        initList();
    }

    @Override
    protected void initData() {

        //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
        EMClient.getInstance().groupManager().asyncGetJoinedGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {
            @Override
            public void onSuccess(List<EMGroup> value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //有数据
                        mAdapter.setNewData(value);

                        for (EMGroup emGroup:value) {
                            DbUserHelper.insertGroup2Bean(emGroup);
                        }


                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                Logger.e(error+"-"+errorMsg);
            }
        });

    }


    private void initList(){
        mAdapter = new BaseQuickAdapter<EMGroup,BaseViewHolder>(R.layout.item_contacts) {
            @Override
            protected void convert(@NotNull BaseViewHolder holder, EMGroup emGroup) {
                ImageView ivHead = holder.getView(R.id.item_contacts_iv_head);
                //头像
                try {
                    String description = description = AESUtils.decrypt(MMKVStytemUtils.getInstance().getAesKey(),emGroup.getDescription().replaceAll(" ","+"));
                    GroupDescriptionBean groupDescription = new Gson().fromJson(description, GroupDescriptionBean.class);

                    GlideApp.with(getActivity())
                            .load(groupDescription.getHeadImg())
                            .circleCrop()
                            .into(ivHead);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                holder.setText(R.id.item_contacts_tv_name,emGroup.getGroupName()+"（"+emGroup.getMemberCount()+" 人）");

            }
        };
        mRvGroup.setLayoutManager(new LinearLayoutManager(this));
        mRvGroup.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GroupManageActivity.this, GroupHomeActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,mAdapter.getItem(position).getGroupId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupManage_layout_createGroup:
                //创建群组
                GroupDescriptionBean groupDescriptionBean = new GroupDescriptionBean();
                groupDescriptionBean.setHeadImg(RandomConstant.randomHeadImg());
                groupDescriptionBean.setInfoTag(MMKVUserUtils.getInstance().createTag());
                groupDescriptionBean.setIntro("这是一条新的简介");

                try {
                    String aa = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(groupDescriptionBean));

                    Logger.d(aa);
                    EMClient.getInstance().groupManager().asyncChangeGroupDescription("132497543331841", aa, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Logger.d("修改成功");
                        }
                        @Override
                        public void onError(int code, String error) {

                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.groupManage_layout_addGroup:

                startActivityForResult(GroupAddActivity.class, new OnActivityCallback() {
                    @Override
                    public void onActivityResult(int resultCode, @Nullable Intent data) {
                        //如果添加，则重新刷新列表
                        if (resultCode == Activity.RESULT_OK) initData();
                    }
                });
                break;
        }
    }



}
