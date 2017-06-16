package com.lenovo.album.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.base.Constant;
import com.lenovo.album.contract.LabelAlbumScannerContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.LabelAlbumEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by noahkong on 17-6-6.
 */

public class LabelAlbumScannerModel implements LabelAlbumScannerContract.Model {
    @Override
    public void searchAllAlbum(Context ctx, final ModelResponse<List<LabelAlbumEntity>> listener) {
        new AsyncTask<Void, Void, List<LabelAlbumEntity>>() {
            @Override
            protected List<LabelAlbumEntity> doInBackground(Void... params) {



                List<LabelAlbumEntity> labelAlbumEntityArrayList = MyApplication
                        .getInstances()
                        .getDaoSession()
                        .getLabelAlbumEntityDao()
                        .queryBuilder()
                        .orderDesc(LabelAlbumEntityDao.Properties.SortIndex)
                        .list();

                for (LabelAlbumEntity entity : labelAlbumEntityArrayList) {
                    entity.imageList = new ArrayList<ImageEntity>();
                    for (LabelEntity labelEntity : entity.getLabelEntityList()) {
                        labelEntity.resetImageEntityList();
                        entity.imageList.addAll(labelEntity.getImageEntityList());
                        labelEntity.imageEntityList.clear();
                    }
                    Iterator<ImageEntity> iterator =  entity.imageList.iterator();
                    Set<String> set = new HashSet<String>();
                    while(iterator.hasNext()){
                        ImageEntity imageEntity = iterator.next();
                        File file = new File(imageEntity.path);
                        if(set.contains(imageEntity.uniqueString)){
                            iterator.remove();
                            continue;
                        }
                        if(!file.exists()){
                            iterator.remove();
                            continue;
                        }
                        imageEntity.getLabelEntityList().clear();
                        set.add(imageEntity.uniqueString);
                    }
                    List<ImageEntity> covers = new ArrayList<ImageEntity>();
                    for (int i = 0; i < Constant.COVERS_COUNT; i++) {
                        if (i >=  entity.imageList.size()) {
                            break;
                        }
                        covers.add( entity.imageList.get(i));

                    }

                    entity.covers = covers;
                    entity.sortImageListByUpdatedDate();
                }

                return labelAlbumEntityArrayList;
            }

            @Override
            protected void onPostExecute(List<LabelAlbumEntity> labelAlbumEntities) {
                super.onPostExecute(labelAlbumEntities);
                if (listener != null) {
                    listener.onSuccess(labelAlbumEntities);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());

    }

    @Override
    public void updateLabelAlbum(final List<LabelAlbumEntity> labelAlbumEntities,final  ModelResponse<String> listener) {
        new AsyncTask<Void, Void,String>() {
            @Override
            protected String doInBackground(Void... params) {
                MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().updateInTx(labelAlbumEntities);
                return SUCCESS;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (listener != null) {
                    listener.onSuccess(s);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void cancelSearch() {

    }
}
