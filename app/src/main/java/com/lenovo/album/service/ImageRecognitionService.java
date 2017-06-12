package com.lenovo.album.service;

import android.app.Service;
import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;


import com.lenovo.album.contract.ImageRecognitionContract;
import com.lenovo.album.model.ImageRecognitionModel;
import com.lenovo.album.base.Constant;

/**
 * 识别service
 */

public class ImageRecognitionService extends Service implements Constant {

    private ImageRecognitionContract.Model model;

    @Override
    public void onCreate() {
        super.onCreate();
        model = new ImageRecognitionModel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        start();
        return null;
    }

    private void start() {
        model.startRecognition(this);
    }


}
