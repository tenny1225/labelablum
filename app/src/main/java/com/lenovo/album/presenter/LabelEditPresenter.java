package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.LabelEditContract;
import com.lenovo.album.event.LabelDataChangeEvent;
import com.lenovo.album.model.LabelEditModel;
import com.lenovo.common.entity.LabelEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelEditPresenter implements LabelEditContract.Presenter {
    private Context context;
    private LabelEditContract.View view;

    private List<Object> list;

    private LabelEditContract.Model model;


    private List<LabelEntity> notSelectLabels;
    private List<LabelEntity> selectedLabels;

    public LabelEditPresenter(Context context, LabelEditContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void start() {
        list = new ArrayList<>();
        model = new LabelEditModel();
        notSelectLabels = new ArrayList<>();
        selectedLabels = new ArrayList<>();
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public List<Object> getData() {
        return list;
    }

    @Override
    public void getAllLabels() {
        model.getAllLabels(new BaseContract.BaseModel.ModelResponse<LabelEditContract.LabelsResult>() {
            @Override
            public void onSuccess(LabelEditContract.LabelsResult data) {
                notSelectLabels.clear();
                if (data.notSelectLabels != null) {
                    notSelectLabels.addAll(data.notSelectLabels);
                }

                selectedLabels.clear();
                if (data.selectedLabels != null) {
                    selectedLabels.addAll(data.selectedLabels);
                }

                list.add(context.getResources().getString(R.string.selected_label));
                list.addAll(selectedLabels);
                list.add(context.getResources().getString(R.string.not_select_label));
                list.addAll(notSelectLabels);

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
    public void updateLabels() {
        List<LabelEntity> labelEntityList = new ArrayList<>();

       for(int i=0;i<list.size();i++){
           Object o = list.get(i);
           if(o instanceof LabelEntity){
               ((LabelEntity) o).orderIndex = list.size()-i;
               labelEntityList.add((LabelEntity) o);
           }
       }

        model.updateLabels(labelEntityList, new BaseContract.BaseModel.ModelResponse<String>() {
            @Override
            public void onSuccess(String data) {
                EventBus.getDefault().post(new LabelDataChangeEvent());
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
