package com.lenovo.album.model;

import android.os.AsyncTask;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.ImageLabelScannerContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.ImageEntityDao;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class ImageLabelScannerModel implements ImageLabelScannerContract.Model {
    @Override
    public void queryLabels(final String uniqueString, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void,Void,List<LabelEntity>>(){
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {

                ImageEntity imageEntity = MyApplication.getInstances().getDaoSession().getImageEntityDao()
                        .queryBuilder()
                        .where(ImageEntityDao.Properties.UniqueString.eq(uniqueString)).unique();
                if(imageEntity!=null){
                    return imageEntity.getLabelEntityList();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<LabelEntity> labelEntityList) {
                super.onPostExecute(labelEntityList);
                if(response!=null){
                    response.onSuccess(labelEntityList);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());


    }
}
