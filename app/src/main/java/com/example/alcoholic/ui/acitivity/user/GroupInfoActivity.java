package com.example.alcoholic.ui.acitivity.user;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.GroupDescriptionBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.constant.JumpDataContants;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.http.request.ImgUpLoadApi;
import com.example.alcoholic.http.response.ImgUploadBean;
import com.example.alcoholic.http.server.ImgBBServer;
import com.example.alcoholic.ui.acitivity.image.ImageSelectActivity;
import com.example.alcoholic.ui.dialog.InputDialog;
import com.example.alcoholic.utils.AESUtils;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVStytemUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.alcoholic.utils.TimeUtils;
import com.example.base.BaseActivity;
import com.example.base.BaseDialog;
import com.example.widget.layout.SettingBar;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnHttpListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by
 * Description:群组信息页面
 * on 2020/11/20.
 */
public class GroupInfoActivity extends MyActivity {


    private ImageView mIvHead;
    private SettingBar mBarHead;
    private SettingBar mBarId;
    private SettingBar mBarName;
    private SettingBar mBarIntro;

    private String mGroupId;
    /* 是否是群主 */
    private boolean isOwner = false;
    /* 群组信息 - 本地 */
    private UserInfoBean mGroupInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void initView() {
        mIvHead = findViewById(R.id.groupInfo_iv_head);
        mBarHead = findViewById(R.id.groupInfo_bar_head);
        mBarId = findViewById(R.id.groupInfo_bar_id);
        mBarName = findViewById(R.id.groupInfo_bar_name);
        mBarIntro = findViewById(R.id.groupInfo_bar_intro);

        setOnClickListener(mBarHead,mBarName,mBarIntro);


    }

    @Override
    protected void initData() {
        if (getIntent() != null && getIntent().getExtras()!= null){
            mGroupId = getIntent().getExtras().getString(JumpDataContants.JUMP_DATA_USER_ACCOUNT,"");
            isOwner = getIntent().getExtras().getBoolean(JumpDataContants.JUMP_DATA_IS_OWNER,false);
        }
        if (EmptyUtils.isEmpty(mGroupId)){
            toast("找不到该群组");
            finish();
        }


        if (!isOwner){
            //非群主不能修改
            mBarName.setRightIcon(null);
            mBarIntro.setRightIcon(null);
        }

        initGroupInfo();
    }

    private void initGroupInfo(){

        mGroupInfo = DbUserHelper.queryUser2Account(mGroupId);




        if (mGroupInfo != null && EmptyUtils.isNotEmpty(mGroupInfo.getUserHead())){
            GlideApp.with(this)
                    .load(mGroupInfo.getUserHead())
                    .circleCrop()
                    .into(mIvHead);
            mBarIntro.setRightText(mGroupInfo.getUserIntro());
        }else {
            GlideApp.with(this)
                    .load(R.drawable.icon)
                    .circleCrop()
                    .into(mIvHead);
            mBarIntro.setRightText("");
        }

        mBarId.setRightText(mGroupId);
        mBarName.setRightText(mGroupInfo.getUserName());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.groupInfo_bar_head:
                //头像
                if (isOwner){
                    //群主才能修改
                    upGroupHead();
                }
                break;
            case R.id.groupInfo_bar_name:
                //群名
                if (isOwner){
                    //群主才能修改
                    upGroupName();
                }
                break;
            case R.id.groupInfo_bar_intro:
                //简介
                if (isOwner){
                    //群主才能修改
                    upGroupIntro();
                }
                break;
        }
    }

    /**
     * 修改群头像
     */
    public void upGroupHead(){
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


                                GroupDescriptionBean groupDescriptionBean = new GroupDescriptionBean();
                                groupDescriptionBean.setInfoTag(MMKVUserUtils.getInstance().createTag());
                                groupDescriptionBean.setHeadImg(result.getData().getUrl());
                                groupDescriptionBean.setIntro(mGroupInfo.getUserIntro());


                                try {
                                    String description = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(groupDescriptionBean));

                                    //修改群描述
                                    EMClient.getInstance().groupManager().asyncChangeGroupDescription(mGroupId, description, new EMCallBack() {
                                        @Override
                                        public void onSuccess() {

                                            mGroupInfo.setUserHead(result.getData().getUrl());
                                            DbUserHelper.insertUser(mGroupInfo);
                                            toast("修改群组头像成功");
                                            setResult(Activity.RESULT_OK);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hideDialog();
                                                    initGroupInfo();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(int code, String error) {
                                            toast("修改失败："+code+" - "+error);
                                        }

                                        @Override
                                        public void onProgress(int progress, String status) {

                                        }
                                    });

                                } catch (Exception e) {
                                    hideDialog();
                                    e.printStackTrace();
                                }

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
     * 修改群名
     */
    public void upGroupName(){
        new InputDialog.Builder(this)
                .setTitle("修改群组名称")
                .setHint("请输入您将要设置的群组名称吧")
                .setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length()<=0 || content.trim().length() > 16){
                            toast("群组名称长度限制 0-16 位");
                            return;
                        }
                        //修改群名称
                        EMClient.getInstance().groupManager().asyncChangeGroupName(mGroupId, content.trim(), new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                mGroupInfo.setUserName(content.trim());
                                DbUserHelper.insertUser(mGroupInfo);
                                toast("修改成功");
                                setResult(Activity.RESULT_OK);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initGroupInfo();
                                    }
                                });
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(int code, String error) {
                                toast("修改失败："+code+" - "+error);
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });//需异步处理

                    }
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 修改群组简介
     */
    private void upGroupIntro(){
        new InputDialog.Builder(this)
                .setTitle("修改群组简介")
                .setHint("请输入您将要设置的群组简介吧")
                .setAutoDismiss(false)
                .setListener(new InputDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (content.trim().length()<=0 || content.trim().length() > 16){
                            toast("群组简介长度限制 0-50 位");
                            return;
                        }
                        GroupDescriptionBean groupDescriptionBean = new GroupDescriptionBean();
                        groupDescriptionBean.setInfoTag(MMKVUserUtils.getInstance().createTag());
                        groupDescriptionBean.setHeadImg(mGroupInfo.getUserHead());
                        groupDescriptionBean.setIntro(content.trim());

                        try {
                            String description = AESUtils.encrypt(MMKVStytemUtils.getInstance().getAesKey(),new Gson().toJson(groupDescriptionBean));

                            //修改群描述
                            EMClient.getInstance().groupManager().asyncChangeGroupDescription(mGroupId, description, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    mGroupInfo.setUserIntro(content.trim());
                                    DbUserHelper.insertUser(mGroupInfo);
                                    toast("修改成功");
                                    setResult(Activity.RESULT_OK);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            initGroupInfo();
                                        }
                                    });
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(int code, String error) {
                                    toast("修改失败："+code+" - "+error);
                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
