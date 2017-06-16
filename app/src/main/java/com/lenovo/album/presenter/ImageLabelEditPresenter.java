package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.ImageLabelEditContract;
import com.lenovo.album.model.ImageLabelEditModel;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noahkong on 17-6-14.
 */

public class ImageLabelEditPresenter implements ImageLabelEditContract.Presenter {
    private List<Object> list;

    private Context context;
    private ImageLabelEditContract.View view;

    private ImageLabelEditContract.Model model;

    private ImageEntity imageEntity;

    public ImageLabelEditPresenter(Context context, ImageLabelEditContract.View view) {
        this.context = context;
        this.view = view;
        model = new ImageLabelEditModel();
        list = new ArrayList<>();

        list.add(context.getResources().getString(R.string.selected_label));
        list.add(context.getResources().getString(R.string.not_select_label));


    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void saveLabels() {
        if (imageEntity == null) {
            return;
        }
        List<LabelEntity> labelEntityList = new ArrayList<>();
        int n = 0;
        for (Object o : list) {
            if (o instanceof String) {
                n++;
                if (n == 2) {
                    break;
                }
            } else {
                labelEntityList.add((LabelEntity) o);
            }
        }
        imageEntity.labelEntityList = labelEntityList;

        model.saveLabels(imageEntity, new BaseContract.BaseModel.ModelResponse<String>() {
            @Override
            public void onSuccess(String data) {
                if (view != null) {
                    view.editLabelsSuccess();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void deleteLabel(final LabelEntity labelEntity) {
        model.deleteLabel(labelEntity, new BaseContract.BaseModel.ModelResponse<String>() {
            @Override
            public void onSuccess(String data) {
                list.remove(labelEntity);
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
    public void setImageId(String id) {
        model.queryLabels(id, new BaseContract.BaseModel.ModelResponse<ImageLabelEditContract.Model.ImageEntityAndLabels>() {
            @Override
            public void onSuccess(ImageLabelEditContract.Model.ImageEntityAndLabels data) {
                imageEntity = data.imageEntity;
                list.clear();
                list.add(context.getResources().getString(R.string.selected_label));
                list.addAll(data.selectedLabelEntities);
                list.add(context.getResources().getString(R.string.not_select_label));
                Map<Long, Long> map = new HashMap<Long, Long>();
                for (LabelEntity entity : data.selectedLabelEntities) {
                    map.put(entity.id, entity.id);
                }

                for (LabelEntity entity : data.labelEntities) {
                    if (!map.containsKey(entity.id)) {
                        list.add(entity);
                    }
                }
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
    public List<Object> getData() {
        return list;
    }

    @Override
    public void addLabel(final LabelEntity labelEntity) {
        final Long id = labelEntity.id;
        model.addLabel(labelEntity, new BaseContract.BaseModel.ModelResponse<LabelEntity>() {
            @Override
            public void onSuccess(LabelEntity data) {
                if(id==null||id.longValue()==0){
                    list.add(data);
                }
                if (view != null) {
                    view.refresh();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }
}
