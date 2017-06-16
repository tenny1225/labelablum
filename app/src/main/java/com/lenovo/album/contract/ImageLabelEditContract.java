package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public interface ImageLabelEditContract {
    interface View extends BaseContract.BaseView {
        void editLabelsSuccess();

        void refresh();
    }

    interface Presenter extends BaseContract.BasePresenter {
        void saveLabels();

        void deleteLabel(LabelEntity labelEntity);

        void setImageId(String uniqueString);

        //LabelAlbumEntity getLabelAlbum();

        List<Object> getData();

        void addLabel(LabelEntity entity);

    }

    interface Model extends BaseContract.BaseModel {
        class ImageEntityAndLabels {
            public ImageEntity imageEntity;
            public List<LabelEntity> selectedLabelEntities;
            public List<LabelEntity> labelEntities;
        }

        void queryLabels(String uniqueString, ModelResponse<ImageEntityAndLabels> response);

        void saveLabels(ImageEntity entity, ModelResponse<String> response);

        void deleteLabel(LabelEntity entity, ModelResponse<String> response);

        void addLabel(LabelEntity entity, ModelResponse<LabelEntity> response);
    }
}
