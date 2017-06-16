package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public interface SearchLabelContract {
    interface View extends BaseContract.BaseView {
        void refresh();
        void querySuccess();
    }
    interface Presenter extends BaseContract.BasePresenter {
        List<LabelEntity> getLabelEntities();
        List<ImageEntity> getImageEntities();
        void queryCandidate(String str);
        void queryImages(String str);
    }
    interface Model extends BaseContract.BaseModel {
        void queryCandidate(List<String> str,ModelResponse<List<LabelEntity>> response);
        void queryImages(List<String> str,ModelResponse<List<ImageEntity>> response);

    }


}
