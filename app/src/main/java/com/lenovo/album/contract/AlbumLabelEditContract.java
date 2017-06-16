package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-14.
 */

public interface AlbumLabelEditContract {
    interface View extends BaseContract.BaseView {
        void editAlbumSuccess();

        void refresh();
    }

    interface Presenter extends BaseContract.BasePresenter {
        void saveAlbum();

        void deleteAlbum();

        void setLabelAlbum(long id);

        LabelAlbumEntity getLabelAlbum();

        List<Object> getData();


    }

    interface Model extends BaseContract.BaseModel {
        class LabelAlbumAndLabels {
            public LabelAlbumEntity labelAlbumEntity;
            public List<LabelEntity> labelEntities;
        }

        void queryLabelAlbum(long id, ModelResponse<LabelAlbumAndLabels> response);

        void saveAlbum(LabelAlbumEntity entity, ModelResponse<String> response);

        void deleteAlbum(LabelAlbumEntity entity, ModelResponse<String> response);
    }
}
