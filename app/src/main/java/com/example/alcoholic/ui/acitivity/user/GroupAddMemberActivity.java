package com.example.alcoholic.ui.acitivity.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.JumpDataContants;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.ui.adapter.ContactsAdapter;
import com.example.alcoholic.utils.EmptyUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:群组添加新成员
 * on 2020/11/20.
 */
public class GroupAddMemberActivity extends MyActivity {

    private RecyclerView mRvUser;


    private ContactsAdapter mContactsAdapter;

    private String mGroupId;
    /* 暂存数据 */
    private List<ContactsBean> mContactsBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_add_member;
    }

    @Override
    protected void initView() {
        mRvUser = findViewById(R.id.groupAddMember_rv_user);


        initList();
    }


    private void initList(){


        // 侧滑监听
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                inviteUser(pos);
                Logger.d(mContactsBeanList.get(pos).getContactsAccount());
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(GroupAddMemberActivity.this, R.color.colorAccent));
            }
        };


        mContactsAdapter = new ContactsAdapter();
        mContactsAdapter.getDraggableModule().setSwipeEnabled(true);
        mContactsAdapter.getDraggableModule().setOnItemSwipeListener(onItemSwipeListener);
        mContactsAdapter.getDraggableModule().getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        mRvUser.setLayoutManager(new LinearLayoutManager(this));
        mRvUser.setAdapter(mContactsAdapter);
        mContactsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {


            }
        });

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

        showAddGroupMember();
    }



    /**
     * 显示可以加入群组的人
     */
    private void showAddGroupMember(){

        mContactsBeanList.addAll(DbContactsHelper.queryContactsAll());


        //如果群成员较多，需要多次从服务器获取完成
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<String> memberList = new ArrayList<>();
                EMCursorResult<String> result = null;
                final int pageSize = 20;
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
                        int size = mContactsBeanList.size();
                        for (int i = size-1; i >= 0 ; i--) {
                            if (memberList.contains(mContactsBeanList.get(i).getContactsAccount())){
                                mContactsBeanList.remove(i);
                            }

                        }


                        mContactsAdapter.addData(mContactsBeanList);
                    }
                });
            }
        }).start();

    }


    /**
     * 邀请进群
     */
    private void inviteUser(int pos){

        //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
        String[] newmembers = new String[]{mContactsBeanList.get(pos).getContactsAccount()};
        EMClient.getInstance().groupManager().asyncInviteUser(mGroupId, newmembers, null, new EMCallBack() {
            @Override
            public void onSuccess() {
                toast("邀请成功");
                mContactsBeanList.remove(pos);
                setResult(Activity.RESULT_OK);
            }

            @Override
            public void onError(int code, String error) {

                toast("邀请失败："+code+"-"+error);
                mContactsAdapter.addData(pos,mContactsBeanList.get(pos));
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }


}
