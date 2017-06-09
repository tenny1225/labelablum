package com.lenovo.album;

import android.app.Application;
import android.bluetooth.BluetoothClass;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.speech.RecognitionService;

import com.lenovo.album.service.ImageRecognitionService;
import com.lenovo.greendao.gen.DaoMaster;
import com.lenovo.greendao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by noahkong on 17-6-5.
 */

public class MyApplication extends Application {
    private DaoMaster.DevOpenHelper mHelper;
    private Database db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static MyApplication instances;


    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setDatabase();

    }

    public static MyApplication getInstances() {
        return instances;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        mHelper =  new DaoMaster.DevOpenHelper(this, "label-album", null);
        db = mHelper.getWritableDb();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public Database getDb() {
        return db;
    }




}