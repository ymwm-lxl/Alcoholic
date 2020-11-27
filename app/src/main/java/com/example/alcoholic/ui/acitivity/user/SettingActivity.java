package com.example.alcoholic.ui.acitivity.user;

import android.view.View;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.UpVersionBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.helper.ActivityStackManager;
import com.example.alcoholic.http.request.GithubUpLoadApi;
import com.example.alcoholic.http.server.GithubServer;
import com.example.alcoholic.other.AppConfig;
import com.example.alcoholic.ui.dialog.InputDialog;
import com.example.alcoholic.ui.dialog.MessageDialog;
import com.example.alcoholic.ui.dialog.SyncContactsDialog;
import com.example.alcoholic.ui.dialog.UpdateDialog;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.base.BaseDialog;
import com.example.widget.layout.SettingBar;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by
 * Description:设置页面
 * on 2020/11/17.
 */
public class SettingActivity extends MyActivity {

    private TextView mBtnExitLogin;

    private SettingBar mBarAccount;
    private SettingBar mBarContactsSync;
    private SettingBar mBarSetKey;
    private SettingBar mBarHelp;
    private SettingBar mBarCheckUp;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mBarAccount = findViewById(R.id.setting_bar_account);
        mBarContactsSync = findViewById(R.id.setting_bar_contactsSync);
        mBarSetKey = findViewById(R.id.setting_bar_setKey);
        mBarHelp = findViewById(R.id.setting_bar_help);
        mBarCheckUp = findViewById(R.id.setting_bar_checkUp);
        mBtnExitLogin = findViewById(R.id.setting_btn_exitLogin);


        mBarAccount.setEnabled(false);
        setOnClickListener(mBarContactsSync,mBarSetKey,mBarHelp,mBarCheckUp,mBtnExitLogin);


    }

    @Override
    protected void initData() {
        initInfo();

    }


    private void initInfo(){
        mBarAccount.setRightText(MMKVUserUtils.getInstance().getUserAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_bar_contactsSync:
                new MessageDialog.Builder(this).setMessage("是否要同步联系人信息？")
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                new SyncContactsDialog.Builder(getActivity()).show();
                            }
                        })
                        .show();

                break;
            case R.id.setting_bar_setKey:
                //设置密钥
                setBarSetKey();
                break;
            case R.id.setting_bar_help:
                //帮助
                startActivity(HelpActivity.class);
                break;
            case R.id.setting_bar_checkUp:
                //检查更新
                checkUp();
                break;
            case R.id.setting_btn_exitLogin:
                //退出登录
                exitLogin();
                break;
        }
    }

    /**
     * 设置密钥
     */
    private void setBarSetKey(){
        new InputDialog.Builder(this)
                .setTitle("设置密钥")
                .setHint("请输入解密密钥")
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        int size = 4;
                        if (content.length() == size){
                            MMKVStytemUtils.getInstance().saveAesKey(content+content+content+content);
                            toast("修改成功");
                        }else {
                            toast("修改失败，密钥需要是 "+size+" 位");
                        }

                    }
                })
                .show();

    }

    /**
     * 检查更新
     */
    private void checkUp(){
        EasyHttp.get(this)
                .server(new GithubServer())
                .api(new GithubUpLoadApi())
                .request(new OnHttpListener<String>() {
                    @Override
                    public void onSucceed(String result) {

                        Document document = Jsoup.parse(result);
                        Element element = document.getElementsByClass("highlight").first();
                        UpVersionBean upVersionBean = new Gson().fromJson(element.text(),UpVersionBean.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                int nowVCode = AppConfig.getVersionCode();
                                if (nowVCode < upVersionBean.getvCode()){

                                    new UpdateDialog.Builder(getActivity())
                                            .setVersionName(upVersionBean.getvName())
                                            .setUpdateLog(upVersionBean.getvUpContent())
//                                            .setForceUpdate(nowVCode <= upVersionBean.getMinMustUpCode())
                                            .setDownloadUrl(upVersionBean.getvAPKDownUrl())
                                            .show();
                                }else {
                                    toast("当前已是最新版本");
                                }

                            }
                        });

                    }

                    @Override
                    public void onFail(Exception e) {

                    }

                    @Override
                    public void onStart(Call call) {
                        showDialog();
                    }

                    @Override
                    public void onEnd(Call call) {
                        hideDialog();
                    }
                });


    }

    /**
     * 退出登录
     */
    private void exitLogin(){
        MMKVUserUtils.getInstance().clearAll();

        EMClient.getInstance().logout(true);


        startActivity(LoginActivity.class);
        // 进行内存优化，销毁除登录页之外的所有界面
        ActivityStackManager.getInstance().finishAllActivities(LoginActivity.class);
    }



}
