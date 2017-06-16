package com.lenovo.album.model;

import android.os.AsyncTask;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.AlbumLabelEditContract;
import com.lenovo.album.contract.ImageLabelEditContract;
import com.lenovo.album.contract.LabelEditContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.JoinImageLabelEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by noahkong on 17-6-14.
 */

public class ImageLabelEditModel implements ImageLabelEditContract.Model {
    @Override
    public void queryLabels(final String id, final ModelResponse<ImageEntityAndLabels> response) {
        new AsyncTask<Void, Void, ImageLabelEditContract.Model.ImageEntityAndLabels>() {
            @Override
            protected ImageLabelEditContract.Model.ImageEntityAndLabels doInBackground(Void... params) {

                ImageLabelEditContract.Model.ImageEntityAndLabels imageEntityAndLabels = new ImageLabelEditContract.Model.ImageEntityAndLabels();
                imageEntityAndLabels.imageEntity = MyApplication.getInstances().getDaoSession().getImageEntityDao().
                        queryBuilder()
                        .where(ImageEntityDao.Properties.UniqueString.eq(id)).unique();

                imageEntityAndLabels.imageEntity.resetLabelEntityList();
                imageEntityAndLabels.selectedLabelEntities = new ArrayList<LabelEntity>();
                imageEntityAndLabels.selectedLabelEntities.addAll( imageEntityAndLabels.imageEntity.getLabelEntityList());
                imageEntityAndLabels.imageEntity.labelEntityList.clear();
                Iterator<LabelEntity> iterator =  imageEntityAndLabels.selectedLabelEntities.iterator();
                while(iterator.hasNext()){
                    LabelEntity entity = iterator.next();
                    if(entity.deleted){
                        //imageEntityAndLabels.selectedLabelEntities.remove(entity);
                        iterator.remove();
                    }
                }

                imageEntityAndLabels.labelEntities = MyApplication.getInstances().getDaoSession().getLabelEntityDao().loadAll();

                Iterator<LabelEntity> iterator1 =   imageEntityAndLabels.labelEntities.iterator();
                while(iterator1.hasNext()){
                    LabelEntity entity = iterator1.next();
                    if(entity.deleted){
                        //imageEntityAndLabels.labelEntities.remove(entity);
                        iterator1.remove();
                    }
                }

                return imageEntityAndLabels;
            }

            @Override
            protected void onPostExecute(ImageLabelEditContract.Model.ImageEntityAndLabels aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(aVoid);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void saveLabels(final ImageEntity entity, final ModelResponse<String> response) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                List<LabelEntity> labelEntities = entity.getLabelEntityList();
                List<JoinImageLabelEntity> list = MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao()
                        .queryBuilder().where(JoinImageLabelEntityDao.Properties.ImageId.eq(entity.id)).list();
                MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().deleteInTx(list);

                list.clear();
                for (LabelEntity entity1 : labelEntities) {
                    JoinImageLabelEntity joinImageLabelEntity = new JoinImageLabelEntity();
                    joinImageLabelEntity.labelId = entity1.id;
                    joinImageLabelEntity.imageId = entity.id;
                    list.add(joinImageLabelEntity);
                }
                MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().insertInTx(list);

                return SUCCESS;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(aVoid);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void deleteLabel(final LabelEntity entity, final ModelResponse<String> response) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                entity.deleted = true;
                MyApplication.getInstances().getDaoSession().getLabelEntityDao().update(entity);
                return SUCCESS;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(aVoid);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void addLabel(final LabelEntity entity, final ModelResponse<LabelEntity> response) {
        new AsyncTask<Void, Void, LabelEntity>() {
            @Override
            protected LabelEntity doInBackground(Void... params) {
                if(entity.id!=null&&entity.id!=0){
                    MyApplication.getInstances().getDaoSession().getLabelEntityDao().update(entity);
                    return entity;
                }

                LabelEntity labelEntity = MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder()
                        .where(LabelEntityDao.Properties.Name.eq(entity.name))
                        .unique();
                if (labelEntity == null) {
                    MyApplication.getInstances().getDaoSession().getLabelEntityDao().insert(entity);
                }else{
                    labelEntity.deleted =false;
                    entity.id = labelEntity.id;
                    MyApplication.getInstances().getDaoSession().getLabelEntityDao().update(labelEntity);
                }
                return entity;
            }

            @Override
            protected void onPostExecute(LabelEntity aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess(aVoid);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }
}
