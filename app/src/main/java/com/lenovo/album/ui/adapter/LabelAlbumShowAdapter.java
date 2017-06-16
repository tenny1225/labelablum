package com.lenovo.album.ui.adapter;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.util.CommonUtil;

import java.util.List;

/**
 * Created by noahkong on 17-6-7.
 */

public class LabelAlbumShowAdapter extends BaseRecyclerAdapter<LabelAlbumEntity> {
    public static interface EditLabelAlbumListener {
        void onEdit(LabelAlbumEntity obj);
    }

    private boolean isEditState;
    private ItemTouchHelper itemTouchHelper;

    public boolean isEditState() {
        return isEditState;
    }

    public void setEditState(boolean editState) {
        if (isEditState == editState) {
            return;
        }
        isEditState = editState;

        notifyDataSetChanged();
    }


    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    private EditLabelAlbumListener editLabelAlbumListener;

    public void setEditLabelAlbumListener(EditLabelAlbumListener editLabelAlbumListener) {
        this.editLabelAlbumListener = editLabelAlbumListener;
    }

    public LabelAlbumShowAdapter(List<LabelAlbumEntity> itemList) {
        super(itemList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    protected View getItemView(ViewGroup parent, int viewType) {
        View v;// = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_label_album, parent, false);

        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_label_album_add, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_label_album, parent, false);
        }
        return v;
    }

    @Override
    protected void onBind(final VH holder, final LabelAlbumEntity obj) {
        if (obj == null) {
            ImageLoaderFactory.getLoader().displayCircle(
                    holder.<ImageView>$(R.id.iv_album_cover),
                    R.mipmap.ic_default
            );

            return;
        }
        holder.rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    if (itemTouchHelper != null && isEditState) {
                        itemTouchHelper.startDrag(holder);
                    }

                }
                return false;
            }
        });
       // if( obj.covers!=null&&obj.covers.size()>0){
            ImageLoaderFactory.getLoader().displayCircle(
                    holder.<ImageView>$(R.id.iv_album_cover),
                    obj.covers.get(0).path
            );
       /* }else{
            ImageLoaderFactory.getLoader().displayCircle(
                    holder.<ImageView>$(R.id.iv_album_cover),
                    R.mipmap.ic_default
            );
        }*/


        holder.<View>$(R.id.btn_album_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editLabelAlbumListener != null) {
                    editLabelAlbumListener.onEdit(obj);
                }
            }
        });
        holder.<View>$(R.id.btn_album_edit).setVisibility(isEditState ? View.VISIBLE : View.GONE);
        holder.<TextView>$(R.id.tv_album_name).setText(obj.name);
        //holder.<FilletView>$(R.id.fv_image_count).setText(String.format(holder.<View>$(R.id.fv_image_count).getContext().getResources().getString(R.string.image_count_format).toString(),obj.imageList.size()));

    }
}
