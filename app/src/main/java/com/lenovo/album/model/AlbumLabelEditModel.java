package com.lenovo.album.model;

import android.os.AsyncTask;

import com.lenovo.album.MyApplication;
import com.lenovo.album.contract.AlbumLabelEditContract;
import com.lenovo.common.entity.JoinLabelAllbumEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.JoinLabelAllbumEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by noahkong on 17-6-14.
 */

public class AlbumLabelEditModel implements AlbumLabelEditContract.Model {
    @Override
    public void saveAlbum(final LabelAlbumEntity entity, final ModelResponse<String> response) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (entity.id == null||entity.id.longValue()==0) {
                    MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().insert(entity);
                }else{
                    entity.updated = System.currentTimeMillis();
                    MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().update(entity);
                }

                JoinLabelAllbumEntityDao joinLabelAllbumEntityDao = MyApplication.getInstances().getDaoSession().getJoinLabelAllbumEntityDao();
                List<JoinLabelAllbumEntity> joinLabelAllbumEntityList = new ArrayList<>();
                for (LabelEntity labelEntity : entity.labelEntityList) {
                    JoinLabelAllbumEntity e = new JoinLabelAllbumEntity();
                    e.labelAlbumId = entity.id;
                    e.labelId = labelEntity.id;
                    joinLabelAllbumEntityList.add(e);
                }
                joinLabelAllbumEntityDao.insertInTx(joinLabelAllbumEntityList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(SUCCESS);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());


    }

    @Override
    public void deleteAlbum(final LabelAlbumEntity entity, final ModelResponse<String> response) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().delete(entity);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(SUCCESS);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void queryLabelAlbum(final long id, final ModelResponse<LabelAlbumAndLabels> response) {
        new AsyncTask<Void, Void, LabelAlbumAndLabels>() {
            @Override
            protected LabelAlbumAndLabels doInBackground(Void... params) {

                LabelAlbumEntity labelAlbumEntity;
                if(id==0){
                    labelAlbumEntity = new LabelAlbumEntity();
                    labelAlbumEntity.labelEntityList = new ArrayList<LabelEntity>();
                }else{
                    labelAlbumEntity= MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().load(id);
                }


                List<LabelEntity> labelEntityList = MyApplication.getInstances().getDaoSession().getLabelEntityDao()
                        .queryBuilder()
                        .where(LabelEntityDao.Properties.Deleted.eq(false)).list();

                LabelAlbumAndLabels labelAlbumAndLabels = new LabelAlbumAndLabels();
                labelAlbumAndLabels.labelAlbumEntity = labelAlbumEntity;

                Iterator<LabelEntity> iterator =  labelAlbumEntity.getLabelEntityList().iterator();
                while(iterator.hasNext()){
                    LabelEntity entity = iterator.next();
                    if(entity.deleted){
                        iterator.remove();
                    }
                }

                labelAlbumAndLabels.labelEntities = labelEntityList;
                return labelAlbumAndLabels;
            }

            @Override
            protected void onPostExecute(LabelAlbumAndLabels aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(aVoid);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }
}
