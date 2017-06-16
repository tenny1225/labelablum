package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.RecogniationContract;
import com.lenovo.album.model.RecognitionModel;
import com.lenovo.common.entity.LabelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-15.
 */

public class RecognitionPresenter implements RecogniationContract.Presenter {
    private Context context;
    private RecogniationContract.View view;

    private List<LabelEntity> labelEntities;
    private RecogniationContract.Model model;

    public RecognitionPresenter(Context context, RecogniationContract.View view) {
        this.context = context;
        this.view = view;
        labelEntities = new ArrayList<>();
        model = new RecognitionModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public List<LabelEntity> getLabels() {
        return labelEntities;
    }

    @Override
    public void recognition(String path, boolean isRemote) {
        model.recognition(isRemote, path, new BaseContract.BaseModel.ModelResponse<List<LabelEntity>>() {
            @Override
            public void onSuccess(List<LabelEntity> data) {
                if(data==null){
                    return;
                }
                labelEntities.clear();
                labelEntities.addAll(data);
                if (view != null) {
                    view.showLabels();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
