package com.lenovo.album.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelItemProvider extends ItemViewBinder<LabelEntity, BaseRecyclerAdapter.VH> {
    public interface ItemActionListener{
        void onItemClick(LabelEntity item,View v);
        void onLongClick(LabelEntity item,View v);
    }
    private ItemActionListener itemActionListener;

    public void setItemActionListener(ItemActionListener itemActionListener) {
        this.itemActionListener = itemActionListener;
    }

    @NonNull
    @Override
    protected BaseRecyclerAdapter.VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new BaseRecyclerAdapter.VH(inflater.inflate(R.layout.list_item_label,parent,false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final BaseRecyclerAdapter.VH holder, @NonNull final LabelEntity item) {
        holder.<FilletView>$(R.id.fv_label).setText(item.alias==null?item.name:item.alias);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemActionListener!=null){
                    itemActionListener.onItemClick(item,v);
                }
            }
        });
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(itemActionListener!=null){
                    itemActionListener.onLongClick(item,v);
                }
                return false;
            }
        });
    }


}
