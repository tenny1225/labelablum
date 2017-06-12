package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.ImageLabelScannerContract;
import com.lenovo.album.model.ImageLabelScannerModel;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class ImageLabelScannerPresenter implements ImageLabelScannerContract.Presenter {
    private Context context;
    private ImageLabelScannerContract.View view;

    private ImageLabelScannerContract.Model model;

    public ImageLabelScannerPresenter(Context context, ImageLabelScannerContract.View view) {
        this.context = context;
        this.view = view;
        model = new ImageLabelScannerModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void queryLabels(String uniqueString) {
        model.queryLabels(uniqueString, new BaseContract.BaseModel.ModelResponse<List<LabelEntity>>() {
            @Override
            public void onSuccess(List<LabelEntity> data) {
                if(view!=null&&data!=null){
                    view.showLabels(data);
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
