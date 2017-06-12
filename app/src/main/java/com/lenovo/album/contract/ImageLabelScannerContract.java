package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public interface ImageLabelScannerContract {
    interface View extends BaseContract.BaseView{
        void showLabels(List<LabelEntity>labelEntityList);
    }

    interface Presenter extends BaseContract.BasePresenter{
        void queryLabels(String uniqueString);
    }

    interface Model extends BaseContract.BaseModel{
        void queryLabels(String uniqueString, ModelResponse<List<LabelEntity>> response);
    }
}
