package com.example.alcoholic.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.db.DbMsgHelper;
import com.example.alcoholic.ui.acitivity.ChatActivity;
import com.example.alcoholic.ui.acitivity.HomeActivity;
import com.example.alcoholic.ui.adapter.ChatListAdapter;
import com.example.alcoholic.widget.decoration.ChatListDecoration;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_CHAT_TYPE;
import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description: 消息页面
 * on 2020/11/13.
 */
public final class MessageFragment extends MyFragment<HomeActivity> implements OnRefreshListener {

    private RecyclerView mRvChat;
    private SmartRefreshLayout mRefreshLayout;

    private ChatListAdapter mAdapter;



    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }



    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mRefreshLayout = findViewById(R.id.f_message_refresh_chat);
        mRvChat = findViewById(R.id.f_message_rv_chat);

        initList();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


    /**
     * 获取到新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNewMessage(MessageBean newMsgBean) {
        initData();
    };

    @Override
    protected void initData() {

        mAdapter.setNewData(DbMsgHelper.queryChat2Me());

        if (mRefreshLayout.isRefreshing()){
            mRefreshLayout.finishRefresh();
        }
    }

    private void initList(){
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mAdapter = new ChatListAdapter();
        mRvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvChat.addItemDecoration(new ChatListDecoration(getContext()));
        mRvChat.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.layout_empty);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,mAdapter.getItem(position).getOtherSide());
                bundle.putInt(JUMP_DATA_CHAT_TYPE,mAdapter.getItem(position).getChatType());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    /**
     * 刷新
     * @param refreshLayout
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }
}
