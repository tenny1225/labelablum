package com.lenovo.album.model;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.lenovo.album.base.Constant;
import com.lenovo.album.contract.ImageRecognitionContract;
import com.lenovo.album.event.LabelDataChangeEvent;
import com.lenovo.album.event.RecognitionCompetedEvent;
import com.lenovo.album.model.helper.DemoRecognitionTask;
import com.lenovo.album.model.helper.ImageCursorLoader;
import com.lenovo.album.model.helper.RecognitionTask;
import com.lenovo.common.manager.AsyncExecutorQueueManager;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.common.util.CommonUtil;
import com.lenovo.common.util.SpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by noahkong on 17-6-6.
 */

public class ImageRecognitionModel implements ImageRecognitionContract.Model, Constant {
    private ImageCursorLoader imageCursorLoader;

    @Override
    public void startRecognition(final Context context) {
        if (SpUtil.isFirstStart(context)) {
            DemoRecognitionTask task = new DemoRecognitionTask();
            task.setData(context);
            task.setTaskDoneLiteneter(new DemoRecognitionTask.TaskDoneLiteneter() {
                @Override
                public void onDone() {
                    start(context);
                }
            });
            task.executeOnExecutor(ExecutorServiceFactory.createAlgorithmExecutor());
        } else {
            start(context);
        }


    }

    private void start(final Context context) {
        imageCursorLoader = new ImageCursorLoader(context);

        imageCursorLoader.registerListener(IMAGE_LOADER_ID, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                if (data == null || data.getCount() == 0) {
                    return;

                }
                AsyncExecutorQueueManager asyncExecutorQueueManager = new AsyncExecutorQueueManager();

                int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);

                while (data.moveToNext()) {
                    File imageFile = new File(data.getString(dataColumnIndex));//图片文件
                    // TODO: 开始进行图片识别
                    RecognitionTask task = new RecognitionTask();
                    task.setData(context, imageFile);
                    asyncExecutorQueueManager.addTask(task);
                }
                asyncExecutorQueueManager.addTask(new AsyncExecutorQueueManager.SimpleTask() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        EventBus.getDefault().post(new LabelDataChangeEvent());
                        return null;
                    }

                });
                asyncExecutorQueueManager.start();

            }

        });


        imageCursorLoader.startLoading();
    }

}
