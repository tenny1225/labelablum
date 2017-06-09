package com.lenovo.album.ui.fragment;

import android.os.Build;
import android.os.Bundle;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.ui.adapter.AlbumImageShowAdapter;
import com.lenovo.album.ui.adapter.decoration.GridItemDecoration;
import com.lenovo.album.ui.fragment.anim.DetailTransition;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.util.CommonUtil;

import java.util.List;

/**
 * Created by noahkong on 17-6-8.
 */

public class AlbumShowFragment extends BaseFragment implements BaseRecyclerAdapter.ListItemHolderClickListener<ImageEntity> {

    private final static String FOLDER_ALBUM_PARAM = "param1";

    public static AlbumShowFragment newInstance(BundleEntity<FolderAlbumEntity> albumEntity) {
        Bundle args = new Bundle();
        args.putSerializable(FOLDER_ALBUM_PARAM, albumEntity);
        AlbumShowFragment fragment = new AlbumShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recycler;
    private AlbumImageShowAdapter adapter;

    private FolderAlbumEntity albumEntity;

    public void setAlbumEntity(FolderAlbumEntity albumEntity) {
        this.albumEntity = albumEntity;
    }

    @Override
    protected int getViewResource() {
        return R.layout.fragment_album_show;
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        BundleEntity<FolderAlbumEntity> bundleEntity = (BundleEntity<FolderAlbumEntity>) getArguments().getSerializable(FOLDER_ALBUM_PARAM);
        bundleEntity.getData(activity, new BundleEntity.DataResponse<FolderAlbumEntity>() {
            @Override
            public void onSuccess(FolderAlbumEntity entity) {
                if (albumEntity == null) {
                    albumEntity = entity;
                    initWidget(rootView);
                }

            }
        });
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        if (albumEntity == null) {
            return;
        }
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(activity, 3));
        recycler.addItemDecoration(new GridItemDecoration());


        adapter = new AlbumImageShowAdapter(albumEntity.imageList);
        adapter.setListItemHolderClickListener(this);

        recycler.setAdapter(adapter);
    }

    @Override
    public void onItemHolderClick(BaseRecyclerAdapter.VH holder, ImageEntity imageEntity) {

       /* AlbumEntity entity = new AlbumEntity();
        entity.imageList = albumEntity.imageList;
        entity.currentIndex = albumEntity.imageList.indexOf(imageEntity);

        ImageShowFragment fragment = ImageShowFragment.newInstance(new BundleEntity<>(activity, entity));
        fragment.setAlbumEntity(entity);*/
        ImageShowNotScrollFragment fragment = ImageShowNotScrollFragment.newInstance(imageEntity);
        fragment.setImageEntity(imageEntity);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(new Fade());
            fragment.setEnterTransition(new Fade());
            fragment.setSharedElementReturnTransition(new DetailTransition());
            fragment.setSharedElementEnterTransition(new DetailTransition());


            fragment.transaction()
                    .addSharedElement(holder.<ImageView>$(R.id.iv_image), getResources().getString(R.string.album_share_element_name))
                    .commit();
        }
        start(fragment);

    }

    @Override
    public void onItemHolderLongClick(BaseRecyclerAdapter.VH holder, ImageEntity imageEntity) {

    }
}
