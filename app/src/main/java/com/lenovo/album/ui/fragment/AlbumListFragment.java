package com.lenovo.album.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.contract.FolderAlbumScannerContract;
import com.lenovo.album.presenter.FolderAlbumScannerPresenter;
import com.lenovo.album.ui.adapter.FolderAlbumShowAdapter;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.util.CommonUtil;

/**
 * Created by noahkong on 17-6-7.
 */

public class AlbumListFragment extends BaseFragment implements FolderAlbumScannerContract.View, BaseRecyclerAdapter.ListItemClickListener<FolderAlbumEntity> {

    public static AlbumListFragment newInstance() {

        AlbumListFragment fragment = new AlbumListFragment();
        return fragment;
    }

    private TextView tvAlbumTypeName;
    private TextView tvAlbumEdit;
    private RecyclerView recycler;

    private FolderAlbumShowAdapter folderAlbumShowAdapter;

    private FolderAlbumScannerContract.Presenter presenter;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_album_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        presenter = new FolderAlbumScannerPresenter(activity, this);
        presenter.start();
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        tvAlbumTypeName = (TextView) rootView.findViewById(R.id.tv_album_type_name);

        tvAlbumEdit = (TextView) rootView.findViewById(R.id.tv_album_edit);

        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recycler.setLayoutManager(new GridLayoutManager(activity, 2));

        folderAlbumShowAdapter = new FolderAlbumShowAdapter(presenter.getFolderAlbumEntityList());
        folderAlbumShowAdapter.setListItemClickListener(this);

        recycler.setAdapter(folderAlbumShowAdapter);
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity,R.attr.colorPrimary);
    }

    @Override
    protected void initSync() {
        super.initSync();
        presenter.searchAllAlbum();
    }

    @Override
    public void refreshListView() {
        folderAlbumShowAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(FolderAlbumEntity folderAlbumEntity) {
        AlbumShowFragment fragment = AlbumShowFragment.newInstance(new BundleEntity<FolderAlbumEntity>(activity,folderAlbumEntity));
        fragment.setAlbumEntity(folderAlbumEntity);
        start(fragment);
    }

    @Override
    public void onItemLongClick(FolderAlbumEntity folderAlbumEntity) {

    }
}
