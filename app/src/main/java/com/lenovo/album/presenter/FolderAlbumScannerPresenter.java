package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.FolderAlbumScannerContract;
import com.lenovo.album.model.FolderAlbumScannerModel;
import com.lenovo.common.entity.FolderAlbumEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-8.
 */

public class FolderAlbumScannerPresenter implements FolderAlbumScannerContract.Presenter {
    private Context context;


    private FolderAlbumScannerContract.View view;
    private FolderAlbumScannerContract.Model model;

    private List<FolderAlbumEntity> folderAlbumEntityList;

    public FolderAlbumScannerPresenter(Context context, FolderAlbumScannerContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void start() {
        folderAlbumEntityList = new ArrayList<>();
        model = new FolderAlbumScannerModel();
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public List<FolderAlbumEntity> getFolderAlbumEntityList() {
        return folderAlbumEntityList;
    }

    @Override
    public void searchAllAlbum() {
        model.searchAllAlbum(context, new BaseContract.BaseModel.ModelResponse<List<FolderAlbumEntity>>() {
            @Override
            public void onSuccess(List<FolderAlbumEntity> data) {
                folderAlbumEntityList.clear();
                folderAlbumEntityList.addAll(data);
                if(view!=null){
                    view.refreshListView();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
