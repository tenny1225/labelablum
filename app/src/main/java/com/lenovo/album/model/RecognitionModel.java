package com.lenovo.album.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.RecogniationContract;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by noahkong on 17-6-15.
 */

public class RecognitionModel implements RecogniationContract.Model {
    @Override
    public void recognition(boolean isNetwork, String path, ModelResponse<List<LabelEntity>> response) {
       /* if (isNetwork) {
            remote(path, response);
        } else {
            local(path, response);
        }*/

        local(path, response);
    }

    private void local(final String path, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void, Void, List<LabelEntity>>() {
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {


                IRecognition recognition = RecognitionFactory.getRecognition(MyApplication.getInstances().getApplicationContext());

                Bitmap bm = ImageLoaderFactory.getLoader().getBitmap(MyApplication.getInstances().getApplicationContext(), path);

                String[] strings = recognition.importing(bm);

                List<LabelEntity> labelEntityList = new ArrayList<LabelEntity>();
                for (String str : strings) {
                    LabelEntity labelEntity = new LabelEntity();
                    labelEntity.name = str;
                    labelEntityList.add(labelEntity);
                }


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

    private void remote(final String path, final ModelResponse<List<LabelEntity>> response) {

    }
}
