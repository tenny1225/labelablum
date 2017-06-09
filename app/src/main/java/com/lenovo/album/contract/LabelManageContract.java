package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public interface LabelManageContract {
    interface View extends BaseContract.BaseView {
        /**
         * 刷新界面
         */
        void refreshListView();
    }

    interface Presenter extends BaseContract.BasePresenter {
        List<LabelEntity> getLabelEntityList();

        void updateLabelEntity(LabelEntity labelEntity);
    }

    interface Model extends BaseContract.BaseModel {
        void updateLabelEntity(LabelEntity labelEntity, ModelResponse<String> listener);
    }
}
