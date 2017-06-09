package com.lenovo.album.ui.adapter;

import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.common.entity.ImageEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-8.
 */

public class AlbumImageShowAdapter extends BaseRecyclerAdapter<ImageEntity> {
    public AlbumImageShowAdapter(List<ImageEntity> itemList) {
        super(itemList);

    }

    @Override
    protected View getItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_album_image, parent, false);
    }

    @Override
    protected void onBind(VH holder, ImageEntity obj) {
        ImageLoaderFactory.getLoader().display(holder.<ImageView>$(R.id.iv_image),obj.path);
        ViewCompat.setTransitionName(holder.<ImageView>$(R.id.iv_image), obj.uniqueString);
    }
}
