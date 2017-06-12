package com.lenovo.album.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.Constant;
import com.lenovo.album.contract.LabelAlbumScannerContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.LabelEntityDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-6.
 */

public class LabelAlbumScannerModel implements LabelAlbumScannerContract.Model {
    @Override
    public void searchAllAlbum(Context ctx, final ModelResponse<List<LabelAlbumEntity>> listener) {
        new AsyncTask<Void, Void, List<LabelAlbumEntity>>() {
            @Override
            protected List<LabelAlbumEntity> doInBackground(Void... params) {
                List<LabelAlbumEntity> labelAlbumEntityArrayList = new ArrayList<>();

                List<LabelEntity> labelEntityList = MyApplication
                        .getInstances()
                        .getDaoSession()
                        .getLabelEntityDao()
                        .queryBuilder()
                        .where(LabelEntityDao.Properties.Selected.eq(true))
                        .orderDesc(LabelEntityDao.Properties.OrderIndex)
                        .list();
                Log.e("xz","list size "+labelEntityList.size());

                for (LabelEntity labelEntity : labelEntityList) {

                    LabelAlbumEntity labelAlbumEntity = new LabelAlbumEntity();

                    List<ImageEntity> imageEntityList = labelEntity.getImageEntityList();

                    labelAlbumEntity.imageList = imageEntityList;
                    labelAlbumEntity.name = labelEntity.name;
                    labelAlbumEntity.alias = labelEntity.alias;

                    List<ImageEntity> covers = new ArrayList<ImageEntity>();
                    for (int i = 0; i < Constant.COVERS_COUNT; i++) {
                        if (i >= imageEntityList.size()) {
                            break;
                        }
                        covers.add(imageEntityList.get(i));

                    }

                    labelAlbumEntity.covers = covers;
                    labelAlbumEntity.sortImageListByUpdatedDate();
                    labelAlbumEntityArrayList.add(labelAlbumEntity);

                }
                return labelAlbumEntityArrayList;
            }

            @Override
            protected void onPostExecute(List<LabelAlbumEntity> labelAlbumEntities) {
                super.onPostExecute(labelAlbumEntities);
                if(listener!=null){
                    listener.onSuccess(labelAlbumEntities);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());

    }

    @Override
    public void cancelSearch() {

    }
}
