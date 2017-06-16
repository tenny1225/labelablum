package com.lenovo.album.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.common.entity.FolderAlbumEntity;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by noahkong on 17-6-7.
 */

public class FolderAlbumShowAdapter extends BaseRecyclerAdapter<FolderAlbumEntity> {
    public FolderAlbumShowAdapter(List<FolderAlbumEntity> itemList) {
        super(itemList);
    }

    @Override
    protected View getItemView(ViewGroup parent, int viewType) {

        View v =   LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_folder_album,parent,false);
        return v;
    }

    @Override
    protected void onBind(VH holder, FolderAlbumEntity obj) {
        ImageLoaderFactory.getLoader().display(
                holder.<ImageView>$(R.id.iv_album_cover),
                obj.imageList.get(0).path
        );

        holder.<TextView>$(R.id.tv_album_name).setText(obj.name);
        //holder.<FilletView>$(R.id.fv_image_count).setText(String.format(holder.<View>$(R.id.fv_image_count).getContext().getResources().getString(R.string.image_count_format).toString(),obj.imageList.size()));

    }
}
