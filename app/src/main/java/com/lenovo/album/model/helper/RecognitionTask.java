package com.lenovo.album.model.helper;

import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.AsyncExecutorQueueManager;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;
import com.lenovo.recognitionalgorithm.CImageRecognitionImpl;
import com.lenovo.recognitionalgorithm.IRecognition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by noahkong on 17-6-6.
 */

public class RecognitionTask extends AsyncExecutorQueueManager.SimpleTask {
    public static Lock imageLock = new ReentrantLock(false);
    public static Lock labelLock = new ReentrantLock(false);
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (file == null || !file.exists()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            InputStream inputStream = new FileInputStream(file);
            int n = 0;
            while ((n = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.updated = file.lastModified();
        imageEntity.path = file.getAbsolutePath();
        imageEntity.name = file.getName();
        imageEntity.size = file.length();
        imageEntity.setUniqueString();

        //图片信息已经保存到了数据库，证明已经识别过了
        imageLock.lock();
        if (MyApplication.getInstances().getDaoSession().getImageEntityDao().queryBuilder().where(
                ImageEntityDao.Properties.UniqueString.eq(imageEntity.uniqueString)
        ).count() > 0) {
            imageLock.unlock();
            return null;
        }

        MyApplication.getInstances().getDaoSession().getImageEntityDao().insert(imageEntity);

        imageLock.unlock();



        IRecognition recognition = new CImageRecognitionImpl();
        String[] tags = recognition.importing(out.toByteArray());
        for (String tag : tags) {
            LabelEntity labelEntity = new LabelEntity();
            labelEntity.name = tag;
            labelLock.lock();
            if (MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder().where(
                    LabelEntityDao.Properties.Name.eq(tag)).count() > 0) {
                labelEntity = MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder().where(
                        LabelEntityDao.Properties.Name.eq(tag)
                ).unique();
            } else {
                MyApplication.getInstances().getDaoSession().getLabelEntityDao().insert(labelEntity);
            }
            labelLock.unlock();

            //构建图片和标签的多对多映射
            JoinImageLabelEntity joinImageLabelEntity = new JoinImageLabelEntity();
            joinImageLabelEntity.imageId = imageEntity.id;
            joinImageLabelEntity.labelId = labelEntity.id;

            MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().insert(joinImageLabelEntity);

        }

        return null;

    }
}
