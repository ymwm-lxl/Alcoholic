package com.example.alcoholic.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.alcoholic.R;
import com.example.alcoholic.bean.ContactsBean;
import com.example.alcoholic.db.DbContactsHelper;
import com.example.alcoholic.utils.MMKVUserUtils;
import com.example.base.BaseDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by
 * Description: 联系人同步dialog
 * on 2020/11/18.
 */
public class SyncContactsDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>{

        private TextView mTvContent;
        private TextView mTvSchedule;
        private View mLineButton;
        private TextView mBtnOver;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.dialog_contacts_sync);
            setCanceledOnTouchOutside(false);

            mTvContent = findViewById(R.id.dialog_contactsSync_tv_content);
            mTvSchedule = findViewById(R.id.dialog_contactsSync_tv_schedule);
            mLineButton = findViewById(R.id.dialog_contactsSync_line_button);
            mBtnOver = findViewById(R.id.dialog_contactsSync_btn_over);
            mLineButton.setVisibility(View.GONE);
            mBtnOver.setVisibility(View.GONE);
            hideButton();

            setOnClickListener(mBtnOver);


            addOnShowListener(new BaseDialog.OnShowListener() {
                @Override
                public void onShow(BaseDialog dialog) {

                    sync();
                }
            });

        }

        /**
         * 开始同步
         */
        private void sync(){

            Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                    List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    List<String> blacks = EMClient.getInstance().contactManager().getBlackListFromServer();

                    //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
//                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
//                    EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(roomId)


                    int contactsSize = contacts.size();
                    int blackSize = blacks.size();
                    for (int i = 0 ; i < contactsSize; i++) {
                        emitter.onNext((i+1)+" / "+ (contactsSize+blackSize));
                        //存储一个新联系人
                        DbContactsHelper.insetContacts(DbContactsHelper.createContact(contacts.get(i),true));
                    }


                    for (int i = 0; i < blackSize; i++) {
                        emitter.onNext((contactsSize+i+1)+" / "+ (contactsSize+blackSize));
                        //存储一个新联系人
                        ContactsBean contactsBean = DbContactsHelper.createContact(contacts.get(i),true);
                        contactsBean.setIsBlack(true);
                        DbContactsHelper.insetContacts(contactsBean);
                    }

                    emitter.onComplete();
                }
            });

            //创建观察者
            Observer<String> observer = new Observer<String>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull String s) {
                    mTvSchedule.setText(s);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {
                    //结束
                    mTvContent.setText("已同步完成");
                    mLineButton.setVisibility(View.VISIBLE);
                    mBtnOver.setVisibility(View.VISIBLE);
                }
            };

            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);



        }





        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_contactsSync_btn_over:
                    autoDismiss();

                    break;
                case R.id.tv_ui_confirm:
//                    autoDismiss();

                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    break;
                default:
                    break;
            }
        }




    }
}
