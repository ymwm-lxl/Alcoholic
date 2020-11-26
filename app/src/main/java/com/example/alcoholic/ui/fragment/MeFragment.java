package com.example.alcoholic.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.ui.acitivity.HomeActivity;
import com.example.alcoholic.ui.acitivity.user.ContactsActivity;
import com.example.alcoholic.ui.acitivity.user.LoginActivity;
import com.example.alcoholic.ui.acitivity.user.SettingActivity;
import com.example.alcoholic.ui.acitivity.user.UserInfoSettingActivity;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.base.BaseActivity;
import com.example.widget.layout.SettingBar;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.orhanobut.logger.Logger;

import androidx.annotation.Nullable;

/**
 * Created by
 * Description: ‘我的’页面
 * on 2020/11/13.
 */
public final class MeFragment extends MyFragment<HomeActivity> {

    private LinearLayout mLayoutUserInfo;
    private ImageView mIvHead;
    private TextView mTvName;
    private SettingBar mBarContacts;
    private SettingBar mBarSetting;

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        getStatusBarConfig().titleBar(R.id.f_me_layout_userInfo).init();

        mLayoutUserInfo = findViewById(R.id.f_me_layout_userInfo);
        mIvHead = findViewById(R.id.f_me_iv_head);
        mTvName = findViewById(R.id.f_me_tv_name);
        mBarContacts = findViewById(R.id.f_me_bar_contacts);
        mBarSetting = findViewById(R.id.f_me_bar_setting);

        setOnClickListener(mLayoutUserInfo,mBarContacts,mBarSetting);


        initUserInfo();
    }

    @Override
    protected void initData() {




    }


    private void initUserInfo(){
        GlideApp.with(getContext())
                .load(MMKVUserUtils.getInstance().getUserHead())
                .circleCrop()
                .into(mIvHead);

        mTvName.setText(MMKVUserUtils.getInstance().getUserName());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.f_me_layout_userInfo:
                startActivityForResult(UserInfoSettingActivity.class, new BaseActivity.OnActivityCallback() {
                    @Override
                    public void onActivityResult(int resultCode, @Nullable Intent data) {
                        initUserInfo();
                    }
                });
                break;
            case R.id.f_me_bar_contacts:
                //联系人
                startActivity(ContactsActivity.class);
                break;
            case R.id.f_me_bar_setting:
                //联系人
                startActivity(SettingActivity.class);
                break;
        }
    }



}
