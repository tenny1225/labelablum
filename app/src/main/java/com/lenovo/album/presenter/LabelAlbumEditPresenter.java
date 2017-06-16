package com.lenovo.album.presenter;

import android.content.Context;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.AlbumLabelEditContract;
import com.lenovo.album.model.AlbumLabelEditModel;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noahkong on 17-6-14.
 */

public class LabelAlbumEditPresenter implements AlbumLabelEditContract.Presenter {
    private Context context;
    private AlbumLabelEditContract.View view;

    private AlbumLabelEditContract.Model model;

    private List<Object> list;

    private LabelAlbumEntity labelAlbumEntity;

    public LabelAlbumEditPresenter(Context context, AlbumLabelEditContract.View view) {
        this.context = context;
        this.view = view;
        model = new AlbumLabelEditModel();

        list = new ArrayList<>();

        list.add(context.getResources().getString(R.string.selected_label));

        list.add(context.getResources().getString(R.string.not_select_label));
    }


    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void saveAlbum() {
        if (labelAlbumEntity == null) {
            return;
        }
        List<LabelEntity> labelEntityList = new ArrayList<>();
        int n = 0;
        for (Object obj : list) {
            if (obj instanceof String) {
                n++;
                if (n == 2) {
                    break;
                }
            } else {
                labelEntityList.add((LabelEntity) obj);
            }
        }
        if (labelEntityList.size() == 0) {
            return;
        }
        if (labelAlbumEntity.labelEntityList == null) {
            labelAlbumEntity = new LabelAlbumEntity();
        }
        labelAlbumEntity.labelEntityList.clear();
        labelAlbumEntity.labelEntityList.addAll(labelEntityList);
        model.saveAlbum(labelAlbumEntity, new BaseContract.BaseModel.ModelResponse<String>() {
            @Override
            public void onSuccess(String data) {
                if (view != null) {
                    view.editAlbumSuccess();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void deleteAlbum() {
        model.deleteAlbum(labelAlbumEntity, new BaseContract.BaseModel.ModelResponse<String>() {
            @Override
            public void onSuccess(String data) {
                if (view != null) {
                    view.editAlbumSuccess();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void setLabelAlbum(long id) {

        model.queryLabelAlbum(id, new BaseContract.BaseModel.ModelResponse<AlbumLabelEditContract.Model.LabelAlbumAndLabels>() {
            @Override
            public void onSuccess(AlbumLabelEditContract.Model.LabelAlbumAndLabels data) {
                if(data.labelAlbumEntity==null){
                    return;
                }
                labelAlbumEntity = data.labelAlbumEntity;
                List<LabelEntity> labelEntityList = labelAlbumEntity.getLabelEntityList();
                if (labelEntityList == null) {
                    labelEntityList = new ArrayList<LabelEntity>();
                }
                Map<Long, Long> spares = new HashMap<Long, Long>();
                for (LabelEntity label : labelEntityList) {
                    spares.put(label.id, label.id);
                }
                list.clear();
                list.add(context.getResources().getString(R.string.selected_label));
                list.addAll(labelEntityList);
                list.add(context.getResources().getString(R.string.not_select_label));

                for (LabelEntity entity : data.labelEntities) {
                    if (!spares.containsKey(entity.id)) {
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
    public LabelAlbumEntity getLabelAlbum() {
        return labelAlbumEntity;
    }

    @Override
    public List<Object> getData() {
        return list;
    }
}
