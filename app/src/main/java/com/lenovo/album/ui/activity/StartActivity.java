package com.lenovo.album.ui.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseActivity;
import com.lenovo.album.event.PermissionGatedEvent;
import com.lenovo.album.service.ImageRecognitionService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by noahkong on 17-6-7.
 */

public class StartActivity extends BaseActivity {
    private final static int REQUEST_CODE = 3000;

    @Override
    protected int getRootResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            startService();
        }


    }

    private void startService() {
        EventBus.getDefault().post(new PermissionGatedEvent());

        MainActivity.startSelf(this);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService();
            } else {
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
