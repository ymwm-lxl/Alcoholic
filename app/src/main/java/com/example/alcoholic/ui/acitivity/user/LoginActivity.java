package com.example.alcoholic.ui.acitivity.user;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.db.DbUserManager;
import com.example.alcoholic.helper.InputTextHelper;
import com.example.alcoholic.ui.acitivity.HomeActivity;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by
 * Description:登录页面
 * on 2020/11/13.
 */
public class LoginActivity extends MyActivity {

    private EditText mEtAccount;
    private EditText mEtPsd;
    private TextView mBtnRegister;
    private TextView mBtnLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_login;
    }

    @Override
    protected void initView() {
        mEtAccount = findViewById(R.id.userLogin_et_account);
        mEtPsd = findViewById(R.id.userLogin_et_psd);
        mBtnRegister = findViewById(R.id.userLogin_btn_register);
        mBtnLogin = findViewById(R.id.userLogin_btn_login);

        setOnClickListener(mBtnRegister, mBtnLogin);


        InputTextHelper.with(this)
                .setMain(mBtnLogin)
                .addView(mEtAccount)
                .addView(mEtPsd)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return getEtAccount().length() >= 6
                                && getEtPsd().length() >= 6;
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
            case R.id.userLogin_btn_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.userLogin_btn_login:
                //登录
                login();
                break;
        }
    }

    public String getEtAccount() {
        return mEtAccount.getText().toString().toLowerCase();
    }

    public String getEtPsd() {
        return mEtPsd.getText().toString().toLowerCase();
    }

    /**
     * 登录方法
     */
    public void login(){
        EMClient.getInstance().login(getEtAccount(),getEtPsd(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                Log.d("main", "登录聊天服务器成功！");

                toast("登陆成功");

                //保存用户信息
                UserInfoBean userInfoBean = DbUserHelper.queryUserIsLogin(getEtAccount());

                MMKVUserUtils.getInstance().saveUserAccount(userInfoBean.getUserAccount());
                MMKVUserUtils.getInstance().saveUserPsd(getEtPsd());
                MMKVUserUtils.getInstance().saveUserName(userInfoBean.getUserName());
                MMKVUserUtils.getInstance().saveUserHead(userInfoBean.getUserHead());
                MMKVUserUtils.getInstance().saveUserIntro(userInfoBean.getUserIntro());

                DbUserHelper.reUserTag(userInfoBean.getUserAccount());

                startActivity(HomeActivity.class);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                toast("登录失败："+message);
                Log.d("main", "登录聊天服务器失败！");
            }
        });
    }



}
