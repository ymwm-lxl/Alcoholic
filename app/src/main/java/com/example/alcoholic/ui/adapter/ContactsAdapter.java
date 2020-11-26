package com.example.alcoholic.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.DraggableModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;
import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.bean.UserInfoBean;
import com.example.alcoholic.db.DbUserHelper;
import com.example.alcoholic.http.glide.GlideApp;
import com.example.alcoholic.utils.EmptyUtils;

import org.jetbrains.annotations.NotNull;

import static com.superrtc.mediamanager.EMediaManager.getContext;

/**
 * Created by
 * Description: 通讯录列表适配器BaseItemDraggableAdapter
 * on 2020/11/18.
 */
public class ContactsAdapter extends BaseQuickAdapter<ContactsBean, BaseViewHolder> implements DraggableModule {

    public ContactsAdapter() {
        super(R.layout.item_contacts);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ContactsBean contactsBean) {
        ImageView ivHead = holder.getView(R.id.item_contacts_iv_head);

        UserInfoBean userInfoBean = DbUserHelper.queryUser2Account(contactsBean.getContactsAccount());

        if (userInfoBean != null){
            GlideApp.with(getContext())
                    .load(userInfoBean.getUserHead())
                    .circleCrop()
                    .into(ivHead);
        }else {
            GlideApp.with(getContext())
                    .load(R.drawable.icon)
                    .circleCrop()
                    .into(ivHead);
        }

        if (EmptyUtils.isEmpty(userInfoBean)){
            if (EmptyUtils.isEmpty(contactsBean.getCodeName())){
                holder.setText(R.id.item_contacts_tv_name,contactsBean.getContactsAccount() + (contactsBean.getIsFriend() ? "":" / 非好友"));
            }else {
                holder.setText(R.id.item_contacts_tv_name,contactsBean.getContactsAccount() +"（"+contactsBean.getCodeName()+"）"+ (contactsBean.getIsFriend() ? "":" / 非好友"));
            }
        }else{
            if (EmptyUtils.isEmpty(contactsBean.getCodeName())){

                holder.setText(R.id.item_contacts_tv_name,userInfoBean.getUserName()+ (contactsBean.getIsFriend() ? "":" / 非好友"));
            }else {
                holder.setText(R.id.item_contacts_tv_name,userInfoBean.getUserName()+"（"+contactsBean.getCodeName()+"）"+ (contactsBean.getIsFriend() ? "":" / 非好友"));
            }
        }


    }
}
