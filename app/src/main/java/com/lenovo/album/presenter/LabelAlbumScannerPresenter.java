package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.LabelAlbumScannerContract;
import com.lenovo.album.model.LabelAlbumScannerModel;
import com.lenovo.common.entity.LabelAlbumEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelAlbumScannerPresenter implements LabelAlbumScannerContract.Presenter {
    private List<LabelAlbumEntity> labelAlbumEntityList;

    private Context context;
    private LabelAlbumScannerContract.View view;
    private LabelAlbumScannerContract.Model model;

    public LabelAlbumScannerPresenter(Context context, LabelAlbumScannerContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void start() {
        labelAlbumEntityList = new ArrayList<>();
        model = new LabelAlbumScannerModel();
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public List<LabelAlbumEntity> getLabelAlbumEntityList() {
        return labelAlbumEntityList;
    }

    @Override
    public void searchAllAlbum() {
        model.searchAllAlbum(context, new BaseContract.BaseModel.ModelResponse<List<LabelAlbumEntity>>() {
            @Override
            public void onSuccess(List<LabelAlbumEntity> data) {

                labelAlbumEntityList.clear();
                labelAlbumEntityList.addAll(data);
                if (view != null) {
                    view.refreshListView();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
