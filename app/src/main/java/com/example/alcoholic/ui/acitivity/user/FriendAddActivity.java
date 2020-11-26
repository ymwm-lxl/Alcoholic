package com.example.alcoholic.ui.acitivity.user;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.helper.InputTextHelper;
import com.example.alcoholic.im.SendMsg;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by
 * Description:添加好友页面
 * on 2020/11/18.
 */
public class FriendAddActivity extends MyActivity {

    private EditText mEtAccount;
    private EditText mEtWhy;
    private TextView mBtnAdd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_add;
    }

    @Override
    protected void initView() {
        mEtAccount = findViewById(R.id.friendAdd_et_account);
        mEtWhy = findViewById(R.id.friendAdd_et_why);
        mBtnAdd = findViewById(R.id.friendAdd_btn_add);


        setOnClickListener(mBtnAdd);


        InputTextHelper.with(this)
                .setMain(mBtnAdd)
                .addView(mEtAccount)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return getInputAccount().length() >= 6;
                    }
                })
                .build();

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friendAdd_btn_add:
                addFriend();

                break;
        }
    }


    private void addFriend(){
        showDialog();

        SendMsg.sendFriendAdd(getInputAccount(), getInputWhy(), new EMCallBack() {
            @Override
            public void onSuccess() {
                hideDialog();
                toast("申请好友成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mEtAccount.setText("");
                        mEtWhy.setText("");
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                hideDialog();
                toast("申请好友失败："+code+" - "+error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });


    }

    /**
     * 获取输入的账户信息
     */
    private String getInputAccount(){
        return mEtAccount.getText().toString().trim().toLowerCase();
    }

    /**
     * 获取输入的理由
     */
    private String getInputWhy(){
        return mEtWhy.getText().toString().trim();
    }

}
