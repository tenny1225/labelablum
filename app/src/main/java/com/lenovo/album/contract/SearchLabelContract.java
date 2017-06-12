package com.lenovo.album.contract;

import com.lenovo.album.base.BaseContract;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public interface SearchLabelContract {
    interface View extends BaseContract.BaseView {
        void refresh();
    }
    interface Presenter extends BaseContract.BasePresenter {
        List<LabelEntity> getLabelEntities();
        void query(String str);
    }
    interface Model extends BaseContract.BaseModel {
       void query(List<String> str,ModelResponse<List<LabelEntity>> response);

    }


}
