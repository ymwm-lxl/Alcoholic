package com.example.alcoholic.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.alcoholic.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by
 * Description:
 * on 2020/11/27.
 */
public class PhotoFindAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public PhotoFindAdapter() {
        super(R.layout.item_photo_find);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Integer integer) {

        ImageView ivPhoto = holder.getView(R.id.item_photoFind_iv_photo);

        ivPhoto.setImageResource(integer);

    }
}
