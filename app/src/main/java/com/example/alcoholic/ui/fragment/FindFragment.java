package com.example.alcoholic.ui.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.alcoholic.R;
import com.example.alcoholic.common.MyFragment;
import com.example.alcoholic.other.StaggeredGridSpaceDecoration;
import com.example.alcoholic.ui.acitivity.HomeActivity;
import com.example.alcoholic.ui.adapter.PhotoFindAdapter;
import com.example.alcoholic.utils.SizeUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by
 * Description:探索页面
 * on 2020/11/13.
 */
public final class FindFragment extends MyFragment<HomeActivity> {

    private SmartRefreshLayout mRefresh;
    private RecyclerView mRvPhoto;


    private PhotoFindAdapter mPhotoAdapter;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        mRefresh = findViewById(R.id.f_find_refrsh_data);
        mRvPhoto = findViewById(R.id.f_find_rv_photo);



        initList();


    }

    @Override
    protected void initData() {

    }

    private void initList(){
        mPhotoAdapter = new PhotoFindAdapter();
        mRvPhoto.setLayoutManager(new StaggeredGridLayoutManager(2,  StaggeredGridLayoutManager.VERTICAL));
        mRvPhoto.addItemDecoration(new StaggeredGridSpaceDecoration(getContext(), SizeUtils.dp2px(getContext(),6),2));
        mRvPhoto.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });




        mPhotoAdapter.addData(R.mipmap.bg_chat_1);
        mPhotoAdapter.addData(R.mipmap.bg_chat_0);
        mPhotoAdapter.addData(R.mipmap.bg_chat_1);
        mPhotoAdapter.addData(R.mipmap.bg_update_top);
        mPhotoAdapter.addData(R.mipmap.ic_add_circle);
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

}
