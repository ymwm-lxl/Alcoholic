package com.example.alcoholic.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.MessageBean;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.im.IMConstant;
import com.example.alcoholic.utils.EmptyUtils;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

/**
 * Created by
 * Description: 聊天列表适配器
 * on 2020/11/13.
 */
public class ChatListAdapter extends BaseQuickAdapter<MessageBean, BaseViewHolder> {
    public ChatListAdapter() {
        super(R.layout.item_chat_list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MessageBean bean) {

        ImageView ivHead = holder.getView(R.id.item_chatList_iv_head);


        if (bean.getDirection() == IMConstant.MESSAGE_DIRECT_SEND){
            GlideApp.with(getContext())
                    .load(EmptyUtils.isEmpty(bean.getToHead())?R.drawable.icon:bean.getToHead())
                    .circleCrop()
                    .into(ivHead);
            if (EmptyUtils.isEmpty(bean.getToName())){
                holder.setText(R.id.item_chatList_tv_name,bean.getToAccount());
            }else {
                holder.setText(R.id.item_chatList_tv_name,DbUserHelper.getUserShowName(bean.getOtherSide(),bean.getToName()));
            }
        }else {
            if (bean.getChatType() == IMConstant.CHAT_TYPE_CHAT){
                //普通会话
                GlideApp.with(getContext())
                        .load(EmptyUtils.isEmpty(bean.getFromHead())?R.drawable.icon:bean.getFromHead())
                        .circleCrop()
                        .into(ivHead);
                holder.setText(R.id.item_chatList_tv_name, DbUserHelper.getUserShowName(bean.getOtherSide(),bean.getFromName()));
            }else {
                //群聊
                GlideApp.with(getContext())
                        .load(EmptyUtils.isEmpty(bean.getToHead())?R.drawable.icon:bean.getToHead())
                        .circleCrop()
                        .into(ivHead);

                holder.setText(R.id.item_chatList_tv_name, DbUserHelper.getUserShowName(bean.getOtherSide(),bean.getToName()));
            }

        }

        holder.setText(R.id.item_chatList_tv_content,bean.getMsgTextContent());


    }



}
