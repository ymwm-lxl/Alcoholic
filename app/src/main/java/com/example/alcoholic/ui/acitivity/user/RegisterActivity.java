package com.example.alcoholic.ui.acitivity.user;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.helper.InputTextHelper;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by
 * Description: 注册页面
 * 注册条件：账号 6-16位，密码6-16位，英文数字
 * on 2020/11/16.
 */
public class RegisterActivity extends MyActivity {

    private EditText mEtAccount;
    private EditText mEtPsd;
    private EditText mEtPsd2;
    private TextView mBtnRegister;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_register;
    }

    @Override
    protected void initView() {
        mEtAccount = findViewById(R.id.userRegister_et_account);
        mEtPsd = findViewById(R.id.userRegister_et_psd);
        mEtPsd2 = findViewById(R.id.userRegister_et_psd2);
        mBtnRegister = findViewById(R.id.userRegister_btn_register);

        setOnClickListener(mBtnRegister);

        InputTextHelper.with(this)
                .setMain(mBtnRegister)
                .addView(mEtAccount)
                .addView(mEtPsd)
                .addView(mEtPsd2)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return getEtAccount().length() >= 6
                                && getEtPsd().length() >= 6
                                && getEtPsd2().length() >= 6;
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
            case R.id.userRegister_btn_register:
                //点击了注册
                if (!getEtPsd().equals(getEtPsd2())){
                    toast("两次输入密码不同！");
                    break;
                }
                register();
                break;
        }
    }


    public String getEtAccount() {
        return mEtAccount.getText().toString().toLowerCase();
    }

    public String getEtPsd() {
        return mEtPsd.getText().toString().toLowerCase();
    }

    public String getEtPsd2() {
        return mEtPsd2.getText().toString().toLowerCase();
    }


    /**
     * 注册
     */
    private void register(){

        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    EMClient.getInstance().createAccount(getEtAccount(), getEtPsd());

                    toast("注册成功");
                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    toast("注册失败");
                }

            }
        }.start();

    }


}
