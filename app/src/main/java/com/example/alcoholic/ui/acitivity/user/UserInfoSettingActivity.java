package com.example.alcoholic.ui.acitivity.user;

import android.view.View;
import android.widget.ImageView;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.http.request.ImgUpLoadApi;
import com.example.alcoholic.http.response.ImgUploadBean;
import com.example.alcoholic.http.server.ImgBBServer;
import com.example.alcoholic.ui.acitivity.image.ImageSelectActivity;
import com.example.alcoholic.ui.dialog.InputDialog;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.alcoholic.utils.TimeUtils;
import com.example.base.BaseActivity;
import com.example.base.BaseDialog;
import com.example.widget.layout.SettingBar;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

import androidx.lifecycle.LifecycleOwner;

/**
 * Created by
 * Description: 用户信息设置页面
 * on 2020/11/19.
 */
public class UserInfoSettingActivity extends MyActivity {

    private ImageView mIvHead;
    private SettingBar mBarHead;
    private SettingBar mBarAccount;
    private SettingBar mBarName;
    private SettingBar mBarIntro;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_setting;
    }

    @Override
    protected void initView() {
        mIvHead = findViewById(R.id.userInfoSetting_iv_head);
        mBarHead = findViewById(R.id.userInfoSetting_bar_head);
        mBarAccount = findViewById(R.id.userInfoSetting_bar_account);
        mBarName = findViewById(R.id.userInfoSetting_bar_name);
        mBarIntro = findViewById(R.id.userInfoSetting_bar_intro);


        setOnClickListener(mBarHead,mBarName,mBarIntro);
    }

    @Override
    protected void initData() {
        initUserInfo();
    }

    private void initUserInfo(){
        GlideApp.with(this)
                .load(MMKVUserUtils.getInstance().getUserHead())
                .circleCrop()
                .into(mIvHead);

        mBarAccount.setRightText(MMKVUserUtils.getInstance().getUserAccount());
        mBarName.setRightText(MMKVUserUtils.getInstance().getUserName());
        mBarIntro.setRightText(MMKVUserUtils.getInstance().getUserIntro());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userInfoSetting_bar_head:
                setHead();
                break;
            case R.id.userInfoSetting_bar_name:
                setName();
                break;
            case R.id.userInfoSetting_bar_intro:
                setIntro();
                break;
        }
    }

    /**
     * 设置头像
     */
    private void setHead(){
        ImageSelectActivity.start((BaseActivity) getActivity(), new ImageSelectActivity.OnPhotoSelectListener() {

            @Override
            public void onSelected(List<File> files) {
                showDialog();
                EasyHttp.post((BaseActivity) getActivity())
                        .server(new ImgBBServer())
                        .api(new ImgUpLoadApi()
                                .setImage(files.get(0))
                                .setName(MMKVUserUtils.getInstance().getUserAccount()+"_header_"+ TimeUtils.getNowMills()))
                        .request(new OnHttpListener<ImgUploadBean>() {
                    @Override
                    public void onSucceed(ImgUploadBean result) {
                        MMKVUserUtils.getInstance().saveUserHead(result.getData().getUrl());
                        initUserInfo();
                        hideDialog();
                    }

                    @Override
                    public void onFail(Exception e) {
                        hideDialog();
                        toast("图片上传失败，请重试");
                    }
                });

            }

            @Override
            public void onCancel() {
                toast("取消了");
            }
        });

    }

    /**
     * 设置昵称
     */
    private void setName(){
        new InputDialog.Builder(this)
                .setTitle("设置昵称")
                .setHint("请输入您的昵称")
                .setContent(MMKVUserUtils.getInstance().getUserName())
                .setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length() == 0 || content.trim().length() > 16){
                            toast("昵称长度限制为 0-16 位");
                            return;
                        }
                        MMKVUserUtils.getInstance().saveUserName(content.trim());
                        DbUserHelper.reUserInfo();//刷新数据库中的当前用户的信息
                        initUserInfo();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();

    }


    /**
     * 设置个性签名
     */
    private void setIntro(){
        new InputDialog.Builder(this)
                .setTitle("设置个性签名")
                .setHint("请输入符合您个性的签名")
                .setContent(MMKVUserUtils.getInstance().getUserIntro())
                .setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length() == 0 || content.trim().length() > 50){
                            toast("签名长度限制为 0-50 位");
                            return;
                        }
                        MMKVUserUtils.getInstance().saveUserIntro(content.trim());
                        DbUserHelper.reUserInfo();//刷新数据库中的当前用户的信息
                        initUserInfo();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
