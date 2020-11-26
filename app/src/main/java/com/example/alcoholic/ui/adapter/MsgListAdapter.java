package com.example.alcoholic.ui.adapter;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseDelegateMultiAdapter;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.ui.acitivity.user.UserHomeActivity;
import com.example.alcoholic.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description:
 * on 2020/11/13.
 */
public class MsgListAdapter extends BaseDelegateMultiAdapter<MessageBean, BaseViewHolder> implements UpFetchModule {

    private int chatType = IMConstant.CHAT_TYPE_CHAT;

    public MsgListAdapter() {
        super(null);

        //Step.1
        setMultiTypeDelegate(new BaseMultiTypeDelegate<MessageBean>(){
            @Override
            public int getItemType(@NotNull List<? extends MessageBean> list, int i) {
                return list.get(i).getDirection();
            }
        });


        //Step.2
        getMultiTypeDelegate()
                .addItemType(IMConstant.MESSAGE_DIRECT_RECEIVE, R.layout.item_msg_left)
                .addItemType(IMConstant.MESSAGE_DIRECT_SEND, R.layout.item_msg_right);



    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MessageBean item) {


        switch (holder.getItemViewType()){
            case IMConstant.MESSAGE_DIRECT_RECEIVE:
                convertLeft(holder,item);
                break;
            case IMConstant.MESSAGE_DIRECT_SEND:
                convertRight(holder,item);
                break;
            default:
                break;
        }



    }


    /**
     * 左边聊天框
     */
    private void convertLeft(@NotNull BaseViewHolder holder, MessageBean item){
        ImageView ivHead = holder.getView(R.id.item_msgLeft_iv_head);

        GlideApp.with(getContext())
                .load(item.getFromHead())
                .circleCrop()
                .into(ivHead);

        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,item.getFromAccount());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        //名字
        if (chatType == IMConstant.CHAT_TYPE_CHAT){
            holder.setGone(R.id.item_msgLeft_tv_name,true);
        }else {
            holder.setGone(R.id.item_msgLeft_tv_name,false);
            holder.setText(R.id.item_msgLeft_tv_name,item.getFromName());
        }


        if (holder.getLayoutPosition() == 0){
            holder.setGone(R.id.item_msgLeft_tv_date,false);
        }else if (item.getMsgTime() - getItem(holder.getLayoutPosition()-1).getMsgTime() < TimeUtils.TimeConstants.MIN){
            holder.setGone(R.id.item_msgLeft_tv_date,false);
        }else {
            holder.setGone(R.id.item_msgLeft_tv_date,true);
        }
        //时间
        holder.setText(R.id.item_msgLeft_tv_date, TimeUtils.millis2String(item.getMsgTime()));

        //内容
        holder.setText(R.id.item_msgLeft_tv_content,item.getMsgTextContent());


    }


    /**
     * 右边聊天框
     */
    private void convertRight(@NotNull BaseViewHolder holder, MessageBean item){
        ImageView ivHead = holder.getView(R.id.item_msgRight_iv_head);

        GlideApp.with(getContext())
                .load(item.getFromHead())
                .circleCrop()
                .into(ivHead);

        //时间
        if (holder.getLayoutPosition() == 0){
            holder.setGone(R.id.item_msgRight_tv_date,false);
        }else if (item.getMsgTime() - getItem(holder.getLayoutPosition()-1).getMsgTime() < TimeUtils.TimeConstants.MIN){
            holder.setGone(R.id.item_msgRight_tv_date,false);
        }else {
            holder.setGone(R.id.item_msgRight_tv_date,true);
        }
        holder.setText(R.id.item_msgRight_tv_date, TimeUtils.millis2String(item.getMsgTime()));

        //内容
        holder.setText(R.id.item_msgRight_tv_content,item.getMsgTextContent());
    }


    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
