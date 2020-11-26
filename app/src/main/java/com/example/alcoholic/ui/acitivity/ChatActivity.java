package com.example.alcoholic.ui.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnUpFetchListener;
import com.chad.library.adapter.base.listener.UpFetchListenerImp;
import com.chad.library.adapter.base.module.BaseUpFetchModule;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.common.ImActivity;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.Constants;
import com.example.alcoholic.db.DbMsgHelper;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.helper.InputTextHelper;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.im.SendMsg;
import com.example.alcoholic.ui.acitivity.user.GroupHomeActivity;
import com.example.alcoholic.ui.acitivity.user.UserHomeActivity;
import com.example.alcoholic.ui.adapter.MsgListAdapter;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_CHAT_TYPE;
import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:
 * on 2020/11/13.
 */
public class ChatActivity extends ImActivity {

    private RecyclerView mRvMsg;
    private EditText mEtInput;
    private TextView mBtnSend;

    private MsgListAdapter mMsgAdapter;
    private LinearLayoutManager mLayoutManager;

    private int pageNum = 0;
    private String mToUserAccount;
    /* 会话类型 */
    private int mChatType = IMConstant.CHAT_TYPE_CHAT;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        getStatusBarConfig().keyboardEnable(true).init();

        mRvMsg = findViewById(R.id.chat_rv_msgList);
        mEtInput = findViewById(R.id.chat_et_input);
        mBtnSend = findViewById(R.id.chat_btn_send);

        setOnClickListener(mBtnSend);


        initList();

        //输入框按钮监听
        InputTextHelper.with(this).setMain(mBtnSend).addView(mEtInput).build();

    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras() != null){
            mToUserAccount = getIntent().getExtras().getString(JUMP_DATA_USER_ACCOUNT,"");
            mChatType = getIntent().getExtras().getInt(JUMP_DATA_CHAT_TYPE,IMConstant.CHAT_TYPE_CHAT);
            mMsgAdapter.setChatType(mChatType);
        }
        if (EmptyUtils.isEmpty(mToUserAccount)){
            toast("会话异常");
            finish();
        }


        //标题栏
        setTitle(DbUserHelper.getUserShowName(mToUserAccount,DbUserHelper.getUserName(mToUserAccount)));


        mMsgAdapter.addData(DbMsgHelper.queryMsg2User(pageNum,mToUserAccount));
        scrollBottom(true,false);
    }

    /**
     * 接收到新消息
     */
    @Override
    protected void onReNewMsg(MessageBean newMsgBean) {
        if (newMsgBean.getOtherSide().equals(mToUserAccount)){
            mMsgAdapter.addData(newMsgBean);
            scrollBottom();
        }
    }

    private void initList(){
        mMsgAdapter = new MsgListAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        mRvMsg.setLayoutManager(mLayoutManager);
        mRvMsg.setAdapter(mMsgAdapter);


        //软键盘弹出列表下滑
        mRvMsg.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    scrollBottom(false,false);//滑动到底部
                }
            }
        });

        //加载监听
        mMsgAdapter.getUpFetchModule().setUpFetchEnable(true);
        mMsgAdapter.getUpFetchModule().setStartUpFetchPosition((int) (Constants.LOAD_LIMIT * 0.9));
        mMsgAdapter.getUpFetchModule().setOnUpFetchListener(new OnUpFetchListener() {
            @Override
            public void onUpFetch() {
                //当数据加载完之后再加载
                if (mMsgAdapter.getItemCount() >= Constants.LOAD_LIMIT) {
                    loadMoreData();
                }

            }
        });
    }

    @Override
    public void onRightClick(View v) {
        Intent intent;
        if (mChatType == IMConstant.CHAT_TYPE_CHAT){
            intent = new Intent(this,UserHomeActivity.class);
        }else {
            intent = new Intent(this, GroupHomeActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString(JUMP_DATA_USER_ACCOUNT,mToUserAccount);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_btn_send:
                //发送消息
                if (EmptyUtils.isEmpty(getEtInput())){
                    toast("请不要发送空消息");
                }
                sendMsg();
                break;
        }
    }

    /**
     * 发送消息
     */
    private void sendMsg(){
        EventBus.getDefault().post(SendMsg.sendText(getEtInput(),mToUserAccount,mChatType));

        mEtInput.setText("");
        scrollBottom();
    }

    /**
     * 页面加载
     */
    private void loadMoreData(){
        pageNum++;
        //开启加载动画
        mMsgAdapter.getUpFetchModule().setUpFetching(true);
        mRvMsg.postDelayed(new Runnable() {
            @Override
            public void run() {

                List moreMsg = DbMsgHelper.queryMsg2User(pageNum,mToUserAccount);

                mMsgAdapter.addData(0,moreMsg);

                //暂停加载动画
                mMsgAdapter.getUpFetchModule().setUpFetching(false);


                if (moreMsg.size() < Constants.LOAD_LIMIT){
                    mMsgAdapter.getUpFetchModule().setUpFetchEnable(false);
                }
            }
        },300);


    }



    /**
     * 获取输入的消息内容
     */
    private String getEtInput() {
        return mEtInput.getText().toString().trim();
    }


    /**
     * 列表滚动到底部
     *
     */
    private void scrollBottom(){
        scrollBottom(false,true);
    }

    private void scrollBottom(boolean isGoDown){
        scrollBottom(isGoDown,true);
    }

    /**
     * 列表滚动到底部
     * @param isGoDown 是否直接到底部
     */
    private void scrollBottom(boolean isGoDown,boolean isAnim){
        if (mRvMsg == null) return;

        if ((mLayoutManager.findLastVisibleItemPosition() < (mMsgAdapter.getItemCount() - 5)) && !isGoDown) {
            return;
        }

        mRvMsg.post(new Runnable() {
            @Override
            public void run() {
                if (mMsgAdapter.getItemCount() > 0) {
                    //是否平滑移动
                    if (isAnim){
                        mRvMsg.smoothScrollToPosition(mMsgAdapter.getItemCount() - 1);
                    }else {
                        mRvMsg.scrollToPosition(mMsgAdapter.getItemCount() - 1);
                    }
                }
            }
        });

    }
}

