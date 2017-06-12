package com.lenovo.album.ui.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseRecyclerAdapter;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelTitleProvider extends ItemViewBinder<String, BaseRecyclerAdapter.VH> {
    @NonNull
    @Override
    protected BaseRecyclerAdapter.VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        return new BaseRecyclerAdapter.VH(inflater.inflate(R.layout.list_item_label_title, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseRecyclerAdapter.VH holder, @NonNull String item) {
        holder.<TextView>$(R.id.tv_lable_title).setText(item);
    }
}
