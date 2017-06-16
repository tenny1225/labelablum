package com.lenovo.album.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.ImageLabelScannerContract;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.JoinImageLabelEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import org.tensorflow.demo.IRecognition;
import org.tensorflow.demo.RecognitionFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class ImageLabelScannerModel implements ImageLabelScannerContract.Model {
    @Override
    public void queryLabels(final String uniqueString, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void, Void, List<LabelEntity>>() {
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {
                List<LabelEntity> labelEntityList = new ArrayList<LabelEntity>();
                ImageEntity imageEntity = MyApplication.getInstances().getDaoSession().getImageEntityDao()
                        .queryBuilder()
                        .where(ImageEntityDao.Properties.UniqueString.eq(uniqueString)).unique();

                if (imageEntity != null) {
                    imageEntity.resetLabelEntityList();
                    labelEntityList.addAll(imageEntity.getLabelEntityList());
                }

                Iterator<LabelEntity> iterator = labelEntityList.iterator();
                while (iterator.hasNext()) {
                    LabelEntity entity = iterator.next();
                    if (entity.deleted) {
                        iterator.remove();
                    } else {
                        entity.resetImageEntityList();
                        ;
                        List<ImageEntity> imageEntities = entity.getImageEntityList();
                        Iterator<ImageEntity> iterator1 = imageEntities.iterator();
                        while (iterator1.hasNext()) {
                            ImageEntity imageEntity1 = iterator1.next();
                            File file = new File(imageEntity1.path);
                            if (!file.exists()) {
                                iterator1.remove();
                                continue;
                            }
                            imageEntity1.getLabelEntityList().clear();
                        }
                    }
                }
                return labelEntityList;
            }

            @Override
            protected void onPostExecute(List<LabelEntity> labelEntityList) {
                super.onPostExecute(labelEntityList);
                if (labelEntityList.size() == 0) {
                    recognition(uniqueString, response);
                    return;
                }
                if (response != null) {
                    response.onSuccess(labelEntityList);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());

    }

    private void recognition(final String uniqueString, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void, Void, List<LabelEntity>>() {
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {
                List<LabelEntity> labelEntityList = new ArrayList<LabelEntity>();

                ImageEntity imageEntity = MyApplication.getInstances().getDaoSession().getImageEntityDao()
                        .queryBuilder()
                        .where(ImageEntityDao.Properties.UniqueString.eq(uniqueString)).unique();

                if (imageEntity == null) {
                    return labelEntityList;
                }

                IRecognition recognition = RecognitionFactory.getRecognition(MyApplication.getInstances().getApplicationContext());

                Bitmap bm = ImageLoaderFactory.getLoader().getBitmap(MyApplication.getInstances().getApplicationContext(), imageEntity.path);

                String[] strings = recognition.importing(bm);
                Log.e("xz", "recognition=" + Arrays.toString(strings));


                LabelEntityDao labelEntityDao = MyApplication.getInstances().getDaoSession().getLabelEntityDao();


                for (String str : strings) {
                    LabelEntity labelEntity = labelEntityDao.queryBuilder().where(LabelEntityDao.Properties.Name.eq(str)).unique();
                    if (labelEntity == null) {
                        labelEntity = new LabelEntity();
                        labelEntity.name = str;
                        labelEntityDao.insert(labelEntity);
                        JoinImageLabelEntity joinImageLabelEntity = new JoinImageLabelEntity();
                        joinImageLabelEntity.imageId = imageEntity.id;
                        joinImageLabelEntity.labelId = labelEntity.id;
                        MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().insert(joinImageLabelEntity);

                    } else {
                        if (MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao()
                                .queryBuilder().where(
                                        JoinImageLabelEntityDao.Properties.LabelId.eq(labelEntity.id),
                                        JoinImageLabelEntityDao.Properties.ImageId.eq(imageEntity.id)
                                ).count() == 0) {
                            JoinImageLabelEntity joinImageLabelEntity = new JoinImageLabelEntity();
                            joinImageLabelEntity.imageId = imageEntity.id;
                            joinImageLabelEntity.labelId = labelEntity.id;
                            MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().insert(joinImageLabelEntity);
                        }
                    }
                }
                labelEntityList.addAll(imageEntity.getLabelEntityList());


                return labelEntityList;
            }

            @Override
            protected void onPostExecute(List<LabelEntity> labelEntityList) {
                super.onPostExecute(labelEntityList);
                if (response != null) {
                    response.onSuccess(labelEntityList);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }
}
