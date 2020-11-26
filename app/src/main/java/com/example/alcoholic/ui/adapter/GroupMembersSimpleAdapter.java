package com.example.alcoholic.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.ui.acitivity.user.UserHomeActivity;
import com.example.alcoholic.utils.EmptyUtils;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.hjq.toast.ToastUtils;
import com.tencent.mmkv.MMKV;

import org.jetbrains.annotations.NotNull;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:群组成员适配器
 * on 2020/11/19.
 */
public class GroupMembersSimpleAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GroupMembersSimpleAdapter() {
        super(R.layout.item_group_members_simple);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String userAccount) {
        ImageView ivHead = holder.getView(R.id.item_groupMembersSimple_iv_head);


        UserInfoBean userInfoBean = DbUserHelper.queryUser2Account(userAccount);

        if (EmptyUtils.isNotEmpty(userInfoBean)){

            GlideApp.with(getContext())
                    .load(DbUserHelper.queryUser2Account(userAccount).getUserHead())
                    .circleCrop()
                    .into(ivHead);
        }else {

            GlideApp.with(getContext())
                    .load(R.drawable.icon)
                    .circleCrop()
                    .into(ivHead);
        }

        if (userAccount.equals(MMKVUserUtils.getInstance().getUserAccount())){

        }else {
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), UserHomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(JUMP_DATA_USER_ACCOUNT,userAccount);
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
                }
            });
        }

        holder.setText(R.id.item_groupMembersSimple_tv_name, DbUserHelper.getUserShowName(userAccount,DbUserHelper.getUserName(userAccount)));



    }
}
