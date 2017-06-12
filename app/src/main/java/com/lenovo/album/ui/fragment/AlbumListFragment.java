package com.lenovo.album.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.contract.FolderAlbumScannerContract;
import com.lenovo.album.contract.LabelAlbumScannerContract;
import com.lenovo.album.event.LabelDataChangeEvent;
import com.lenovo.album.presenter.FolderAlbumScannerPresenter;
import com.lenovo.album.presenter.LabelAlbumScannerPresenter;
import com.lenovo.album.ui.adapter.FolderAlbumShowAdapter;
import com.lenovo.album.ui.adapter.LabelAlbumShowAdapter;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by noahkong on 17-6-7.
 */

public class AlbumListFragment extends BaseFragment implements FolderAlbumScannerContract.View,
        LabelAlbumScannerContract.View {
    public final static int FOLDER_ALBUM = 0;
    public final static int LABEL_ALBUM = 1;

    public static AlbumListFragment newInstance() {

        AlbumListFragment fragment = new AlbumListFragment();
        return fragment;
    }

    private ImageView ivSearch;
    private ImageView ivSelect;
    private TextView tvAlbumTypeName;
    private TextView tvAlbumEdit;
    private RecyclerView recycler;

    private FolderAlbumShowAdapter folderAlbumShowAdapter;

    private LabelAlbumShowAdapter labelAlbumShowAdapter;

    private FolderAlbumScannerContract.Presenter folderPresenter;

    private LabelAlbumScannerContract.Presenter labelPresenter;

    private int currentShow = LABEL_ALBUM;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_album_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        EventBus.getDefault().register(this);

        folderPresenter = new FolderAlbumScannerPresenter(activity, this);
        folderPresenter.start();

        labelPresenter = new LabelAlbumScannerPresenter(activity, this);
        labelPresenter.start();
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);

        ivSearch = (ImageView) rootView.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(SearchFragment.newInstance());
            }
        });

        ivSelect = (ImageView) rootView.findViewById(R.id.iv_select);

        tvAlbumTypeName = (TextView) rootView.findViewById(R.id.tv_album_type_name);
        tvAlbumTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchAlbum();
            }
        });


        tvAlbumEdit = (TextView) rootView.findViewById(R.id.tv_album_edit);
        tvAlbumEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(LabelEditFragment.newInstance());
            }
        });

        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recycler.setLayoutManager(new GridLayoutManager(activity, 2));

        folderAlbumShowAdapter = new FolderAlbumShowAdapter(folderPresenter.getFolderAlbumEntityList());

        labelAlbumShowAdapter = new LabelAlbumShowAdapter(labelPresenter.getLabelAlbumEntityList());

        labelAlbumShowAdapter.setListItemClickListener(new BaseRecyclerAdapter.ListItemClickListener<LabelAlbumEntity>() {
            @Override
            public void onItemClick(LabelAlbumEntity labelAlbumEntity) {
                AlbumEntity entity = new AlbumEntity();
                entity.imageList = labelAlbumEntity.imageList;

                AlbumShowFragment fragment = AlbumShowFragment.newInstance(new BundleEntity<AlbumEntity>(activity, entity), labelAlbumEntity.alias == null ? labelAlbumEntity.name : labelAlbumEntity.alias);
                fragment.setAlbumEntity(entity);
                start(fragment);
            }

            @Override
            public void onItemLongClick(LabelAlbumEntity labelAlbumEntity) {

            }
        });


        folderAlbumShowAdapter.setListItemClickListener(new BaseRecyclerAdapter.ListItemClickListener<FolderAlbumEntity>() {
            @Override
            public void onItemClick(FolderAlbumEntity folderAlbumEntity) {
                AlbumEntity entity = new AlbumEntity();
                entity.imageList = folderAlbumEntity.imageList;

                AlbumShowFragment fragment = AlbumShowFragment.newInstance(new BundleEntity<AlbumEntity>(activity, entity), folderAlbumEntity.name);
                fragment.setAlbumEntity(entity);
                start(fragment);
            }

            @Override
            public void onItemLongClick(FolderAlbumEntity folderAlbumEntity) {

            }
        });
        initAlbum();


    }

    private void initAlbum() {
        if (currentShow == FOLDER_ALBUM) {
            tvAlbumTypeName.setText(R.string.normal_album);
            recycler.setAdapter(folderAlbumShowAdapter);
            folderPresenter.searchAllAlbum();
            tvAlbumEdit.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
        } else {
            tvAlbumTypeName.setText(R.string.label_album);
            recycler.setAdapter(labelAlbumShowAdapter);
            labelPresenter.searchAllAlbum();
            tvAlbumEdit.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }


    private void switchAlbum() {
        if (currentShow != FOLDER_ALBUM) {
            currentShow = FOLDER_ALBUM;

        } else {
            currentShow = LABEL_ALBUM;
        }
        initAlbum();
    }

    @Override
    public void refreshListView() {
        if (currentShow == FOLDER_ALBUM) {
            folderAlbumShowAdapter.notifyDataSetChanged();
        } else {
            labelAlbumShowAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void labelDataChange(LabelDataChangeEvent event) {
        if(currentShow==LABEL_ALBUM){
            labelPresenter.searchAllAlbum();
        }

    }


}
