package com.lenovo.album.contract;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.FolderAlbumEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public interface FolderAlbumScannerContract {
    interface View extends BaseContract.BaseView {
        /**
         * 刷新界面
         */
        void refreshListView();
    }

    interface Presenter extends BaseContract.BasePresenter {
        /**
         * 获取所有文件夹相册list
         * @return
         */
        List<FolderAlbumEntity> getFolderAlbumEntityList();

        void searchAllAlbum();

    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 搜索所有本机所有相册
         * @param ctx
         * @param listener 结果监听
         */
        void searchAllAlbum(Context ctx,BaseContract.BaseModel.ModelResponse<List<FolderAlbumEntity>> listener);
        void cancelSearch();
    }
}
