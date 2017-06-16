package com.lenovo.album.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.ui.activity.StartActivity;
import com.lenovo.album.ui.adapter.AlbumImageShowAdapter;
import com.lenovo.album.ui.adapter.helper.GridItemDecoration;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.util.CommonUtil;

/**
 * Created by noahkong on 17-6-8.
 */

public class AlbumShowFragment extends BaseFragment implements BaseRecyclerAdapter.ListItemHolderClickListener<ImageEntity> {

    private final static String ALBUM_PARAM = "param1";

    private final static String ALBUM_NAME = "param2";

    public static AlbumShowFragment newInstance(BundleEntity<AlbumEntity> albumEntity, String name) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_PARAM, albumEntity);
        args.putString(ALBUM_NAME, name);
        AlbumShowFragment fragment = new AlbumShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView ivBack;
    private TextView tvAlbumName;

    private RecyclerView recycler;
    private AlbumImageShowAdapter adapter;

    private AlbumEntity albumEntity;

    private String albumName;

    public void setAlbumEntity(AlbumEntity albumEntity) {
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
        Bundle bundle = getArguments();
        BundleEntity<AlbumEntity> bundleEntity = (BundleEntity<AlbumEntity>) bundle.getSerializable(ALBUM_PARAM);
        albumName = bundle.getString(ALBUM_NAME);

        if (albumEntity == null) {
            bundleEntity.getData(activity, AlbumEntity.class,new BundleEntity.DataResponse<AlbumEntity>() {
                @Override
                public void onSuccess(AlbumEntity entity) {
                    if(entity!=null){
                        albumEntity = entity;
                        initWidget(rootView);
                    }else{
                        startActivity(new Intent(activity, StartActivity.class));
                        activity.finish();
                    }
                }
            });
        }

    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        if (albumEntity == null) {
            return;
        }

        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        tvAlbumName = (TextView) rootView.findViewById(R.id.tv_album_type_name);
        tvAlbumName.setText(albumName);


        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(activity, 3));
        recycler.addItemDecoration(new GridItemDecoration());


        adapter = new AlbumImageShowAdapter(albumEntity.imageList);
        adapter.setListItemHolderClickListener(this);

        recycler.setAdapter(adapter);
    }

    @Override
    public void onItemHolderClick(BaseRecyclerAdapter.VH holder, ImageEntity imageEntity) {


        albumEntity.currentIndex = albumEntity.imageList.indexOf(imageEntity);


        ImageShowFragment fragment = ImageShowFragment.newInstance(new BundleEntity<>(activity, albumEntity));
        fragment.setAlbumEntity(albumEntity);

        start(fragment);

    }

    @Override
    public void onItemHolderLongClick(BaseRecyclerAdapter.VH holder, ImageEntity imageEntity) {

    }
}
