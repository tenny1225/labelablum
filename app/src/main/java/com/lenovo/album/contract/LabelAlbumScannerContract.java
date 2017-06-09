package com.lenovo.album.contract;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelAlbumEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public interface LabelAlbumScannerContract {
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
        List<LabelAlbumEntity> getLabelAlbumEntityList();

        void searchAllAlbum();

    }

    interface Model extends BaseContract.BaseModel {
        /**
         * 搜索所有所有标签相册
         * @param ctx
         * @param listener 结果监听
         */
        void searchAllAlbum(Context ctx, BaseContract.BaseModel.ModelResponse<List<LabelAlbumEntity>> listener);
        void cancelSearch();
    }
}
