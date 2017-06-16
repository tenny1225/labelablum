package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public interface LabelEditContract {

    interface View extends BaseContract.BaseView {
        void refresh();

    }

    interface Presenter extends BaseContract.BasePresenter {
        List<Object> getData();

        void getAllLabels();

        void updateLabels();

    }
   public static class LabelsResult{
        public List<LabelEntity> selectedLabels;public List<LabelEntity> notSelectLabels;
    }

    interface Model extends BaseContract.BaseModel {

        void getAllLabels( ModelResponse<LabelsResult> response);

        void updateLabels(List<LabelEntity> labels, ModelResponse<String> response);
    }
}
