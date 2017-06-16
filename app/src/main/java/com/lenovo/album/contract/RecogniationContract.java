package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-15.
 */

public interface RecogniationContract {
    interface View extends BaseContract.BaseView {
        void showLabels();
    }

    interface Presenter extends BaseContract.BasePresenter {
        List<LabelEntity> getLabels();

        void recognition(String path,boolean isRemote);

    }

    interface Model extends BaseContract.BaseModel {
        void recognition(boolean isNetwork,String path, ModelResponse<List<LabelEntity>> response);
    }
}
