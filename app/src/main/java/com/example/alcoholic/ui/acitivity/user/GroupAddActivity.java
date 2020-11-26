package com.example.alcoholic.ui.acitivity.user;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.helper.InputTextHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by
 * Description:加入群组
 * on 2020/11/24.
 */
public class GroupAddActivity extends MyActivity {

    private EditText mEtInput;
    private TextView mBtnAdd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_add;
    }

    @Override
    protected void initView() {
        mEtInput = findViewById(R.id.groupAdd_et_groupId);
        mBtnAdd = findViewById(R.id.groupAdd_btn_add);

        setOnClickListener(mBtnAdd);

        InputTextHelper.with(this)
                .setMain(mBtnAdd)
                .addView(mEtInput)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return getEtInput().length() >= 6;
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
            case R.id.groupAdd_btn_add:
                addGroup();
            break;
        }
    }

    /**
     * 添加群组
     */
    private void addGroup(){
        showDialog();

        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
        EMClient.getInstance().groupManager().asyncJoinGroup(getEtInput(), new EMCallBack() {
            @Override
            public void onSuccess() {
                hideDialog();
                toast("申请群组成功");
                setResult(Activity.RESULT_OK);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mEtInput.setText("");
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                hideDialog();
                toast("申请群组失败："+code+" - "+error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });//需异步处理

    }

    public String getEtInput() {
        return mEtInput.getText().toString();
    }
}
