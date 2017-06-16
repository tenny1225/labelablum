package com.lenovo.album.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by noahkong on 17-6-15.
 */

public class InitService extends IntentService {
    public InitService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

}
