package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.album.model.SearchLabelModel;
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
    private SearchLabelContract.Model model;

    public SearchLabelPresenter(Context context, SearchLabelContract.View view) {
        this.context = context;
        this.view = view;
        labelEntities = new ArrayList<>();
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
    public void query(String str) {
        String[] strArray = str.trim().split(" ");
        List<String> list = new ArrayList<>();
        for (String s : strArray) {
            list.add(s.trim());
        }
        model.query(list, new BaseContract.BaseModel.ModelResponse<List<LabelEntity>>() {
            @Override
            public void onSuccess(List<LabelEntity> data) {
                labelEntities.clear();
                labelEntities.addAll(data);
                if(view!=null){
                    view.refresh();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
