package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.album.model.SearchLabelModel;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class SearchLabelPresenter implements SearchLabelContract.Presenter {
    private Context context;
    private SearchLabelContract.View view;

    private List<LabelEntity> labelEntities;

    private List<ImageEntity> imageEntityList;

    private SearchLabelContract.Model model;

    public SearchLabelPresenter(Context context, SearchLabelContract.View view) {
        this.context = context;
        this.view = view;
        labelEntities = new ArrayList<>();
        imageEntityList = new ArrayList<>();
        model = new SearchLabelModel();
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public List<LabelEntity> getLabelEntities() {
        return labelEntities;
    }

    @Override
    public List<ImageEntity> getImageEntities() {
        return imageEntityList;
    }


    @Override
    public void queryCandidate(String str) {
        String[] strArray = str.trim().split(" ");
        List<String> list = new ArrayList<>();
        if (str.endsWith(" ")) {
            list.add("");
        } else {
            list.add(strArray[strArray.length - 1]);
        }
        model.queryCandidate(list, new BaseContract.BaseModel.ModelResponse<List<LabelEntity>>() {
            @Override
            public void onSuccess(List<LabelEntity> data) {
                labelEntities.clear();
                labelEntities.addAll(data);
                if (view != null) {
                    view.refresh();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void queryImages(String str) {
        String[] strArray = str.trim().split(" ");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strArray.length; i++) {
            if (!strArray[i].trim().equals("")) {
                list.add(strArray[i].trim());
            }

        }
        model.queryImages(list, new BaseContract.BaseModel.ModelResponse<List<ImageEntity>>() {
            @Override
            public void onSuccess(List<ImageEntity> data) {
                if (data == null) {
                    return;
                }
                imageEntityList.clear();
                imageEntityList.addAll(data);
                if (view != null) {
                    view.querySuccess();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
