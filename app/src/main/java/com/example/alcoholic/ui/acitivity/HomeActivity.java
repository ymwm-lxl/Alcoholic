package com.example.alcoholic.ui.acitivity;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.example.alcoholic.R;
import com.example.alcoholic.common.MyActivity;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.helper.ActivityStackManager;
import com.example.alcoholic.helper.DoubleClickHelper;
import com.example.alcoholic.im.ContactListener;
import com.example.alcoholic.im.MsgGetListener;
import com.example.alcoholic.im.MyConnectionListener;
import com.example.alcoholic.other.KeyboardWatcher;
import com.example.alcoholic.ui.fragment.FindFragment;
import com.example.alcoholic.ui.fragment.HomeFragment;
import com.example.alcoholic.ui.fragment.MeFragment;
import com.example.alcoholic.ui.fragment.MessageFragment;
import com.example.base.BaseFragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hyphenate.chat.EMClient;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by
 * Description: 主界面
 * on 2020/11/13.
 */
public class HomeActivity extends MyActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        KeyboardWatcher.SoftKeyboardStateListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    /* 链接监听 */
    private MyConnectionListener mMyConnectionListener;
    /* 消息接收监听 */
    private MsgGetListener mMsgGetListener;
    /* 好友操作监听 */
    private ContactListener mContactListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.home_pager);
        mBottomNavigationView = findViewById(R.id.home_navigation);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        KeyboardWatcher.with(this)
                .setListener(this);

        initIm();//初始化im
    }


    private void initIm(){
        mMyConnectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(mMyConnectionListener);
        mMsgGetListener = new MsgGetListener();
        EMClient.getInstance().chatManager().addMessageListener(mMsgGetListener);
        mContactListener = new ContactListener();
        EMClient.getInstance().contactManager().setContactListener(mContactListener);


        //加载群组和会话消息
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }


    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(FindFragment.newInstance());
        mPagerAdapter.addFragment(MessageFragment.newInstance());
        mPagerAdapter.addFragment(MeFragment.newInstance());

        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        mViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_found:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_message:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.home_me:
                mViewPager.setCurrentItem(3);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
        if (mPagerAdapter.getShowFragment().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {

                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        // 记得在不需要的时候移除listener，如在activity的onDestroy()时
        EMClient.getInstance().chatManager().removeMessageListener(mMsgGetListener);
        super.onDestroy();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }



}
