package com.example.alcoholic.ui.acitivity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.ui.adapter.ContactsAdapter;
import com.example.alcoholic.ui.dialog.MessageDialog;
import com.example.alcoholic.ui.dialog.SyncContactsDialog;
import com.example.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.alcoholic.constant.JumpDataContants.JUMP_DATA_USER_ACCOUNT;

/**
 * Created by
 * Description: 联系人页面
 * on 2020/11/18.
 */
public class ContactsActivity extends MyActivity {

    private LinearLayout mLayoutAddFriend;
    private LinearLayout mLayoutGroup;
    private LinearLayout mLayoutBlack;
    private RecyclerView mRvContacts;

    private ContactsAdapter mContactsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        mLayoutAddFriend = findViewById(R.id.contacts_layout_addFriend);
        mLayoutGroup = findViewById(R.id.contacts_layout_group);
        mLayoutBlack = findViewById(R.id.contacts_layout_black);
        mRvContacts = findViewById(R.id.contacts_rv_contacts);


        setOnClickListener(mLayoutAddFriend,mLayoutBlack,mLayoutGroup);

        initList();
    }

    @Override
    protected void initData() {

        mContactsAdapter.setNewData(DbContactsHelper.queryContactsAll());

    }

    private void initList(){
        mContactsAdapter = new ContactsAdapter();
        mRvContacts.setLayoutManager(new LinearLayoutManager(this));
        mRvContacts.setAdapter(mContactsAdapter);
        mContactsAdapter.setEmptyView(R.layout.layout_empty);

        mContactsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(),UserHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(JUMP_DATA_USER_ACCOUNT,mContactsAdapter.getItem(position).getContactsAccount());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contacts_layout_addFriend:
                //添加好友
                addFriend();
                break;
            case R.id.contacts_layout_group:
                //群组
                startActivity(GroupManageActivity.class);
                break;
            case R.id.contacts_layout_black:
                //黑名单
                break;
        }
    }

    @Override
    public void onRightClick(View v) {

        new MessageDialog.Builder(this).setMessage("是否要同步联系人信息？")
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        new SyncContactsDialog.Builder(getActivity()).show();
                    }
                })
                .show();

    }

    /**
     * 添加好友
     */
    private void addFriend(){

        startActivityForResult(FriendAddActivity.class, new OnActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {
                initData();
            }
        });

    }


}
