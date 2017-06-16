package com.lenovo.album.model.helper;

import android.content.Context;
import android.os.Environment;

import com.lenovo.album.MyApplication;
import com.lenovo.album.R;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.JoinLabelAllbumEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.AsyncExecutorQueueManager;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.JoinImageLabelEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import org.tensorflow.demo.IRecognition;
import org.tensorflow.demo.RecognitionFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by noahkong on 17-6-6.
 */

public class DemoRecognitionTask extends AsyncExecutorQueueManager.SimpleTask {
    public static Lock imageLock = new ReentrantLock(false);
    public static Lock labelLock = new ReentrantLock(false);

    private final static String DEMO1 = "demo1.jpg";
    private final static String DEMO2 = "demo2.jpg";
    private final static String DEMO3 = "demo3.jpg";

    public interface TaskDoneLiteneter {
        void onDone();
    }

    private Context context;

    public void setData(Context context) {

        this.context = context.getApplicationContext();
    }

    private TaskDoneLiteneter taskDoneLiteneter;

    public void setTaskDoneLiteneter(TaskDoneLiteneter taskDoneLiteneter) {
        this.taskDoneLiteneter = taskDoneLiteneter;
    }

    @Override
    protected Void doInBackground(Void... params) {

        List<File> list = new ArrayList<>();
        list.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DEMO1));
        list.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DEMO2));
        list.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DEMO3));

        Map<Long, LabelEntity> labelEntityMap = new HashMap<>();
        imageLock.lock();
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            if (!file.exists()) {
                try {
                    copyBigDataToSD(context, file.getName(), file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.updated = file.lastModified();
            imageEntity.path = file.getAbsolutePath();
            imageEntity.name = file.getName();
            imageEntity.size = file.length();
            imageEntity.setUniqueString();

            //图片信息已经保存到了数据库，证明已经识别过了
            ImageEntity image = MyApplication.getInstances().getDaoSession().getImageEntityDao().queryBuilder().where(
                    ImageEntityDao.Properties.UniqueString.eq(imageEntity.uniqueString)
            ).unique();
            if (image != null&&image.id!=null) {
                imageEntity.id = image.id;
            } else {
                MyApplication.getInstances().getDaoSession().getImageEntityDao().insert(imageEntity);
            }


            IRecognition recognition = RecognitionFactory.getRecognition(context);

            String[] tags = recognition.importing(ImageLoaderFactory.getLoader().getBitmap(context, file.getAbsolutePath()));


            for (String tag : tags) {
                LabelEntity labelEntity = new LabelEntity();
                labelEntity.name = tag;

                if (MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder().where(
                        LabelEntityDao.Properties.Name.eq(tag)).count() > 0) {
                    labelEntity = MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder().where(
                            LabelEntityDao.Properties.Name.eq(tag)
                    ).unique();
                } else {
                    MyApplication.getInstances().getDaoSession().getLabelEntityDao().insert(labelEntity);
                }

                if (MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().queryBuilder()
                        .where(JoinImageLabelEntityDao.Properties.ImageId.eq(imageEntity.id),
                                JoinImageLabelEntityDao.Properties.LabelId.eq(labelEntity.id)).count() == 0) {
                    //构建图片和标签的多对多映射
                    JoinImageLabelEntity joinImageLabelEntity = new JoinImageLabelEntity();
                    joinImageLabelEntity.imageId = imageEntity.id;
                    joinImageLabelEntity.labelId = labelEntity.id;

                    MyApplication.getInstances().getDaoSession().getJoinImageLabelEntityDao().insert(joinImageLabelEntity);
                }
                labelEntityMap.put(labelEntity.id, labelEntity);

            }


        }

        LabelAlbumEntity labelAlbumEntity = new LabelAlbumEntity();
        labelAlbumEntity.name = context.getResources().getString(R.string.demo);
        MyApplication.getInstances().getDaoSession().getLabelAlbumEntityDao().insert(labelAlbumEntity);

        for (LabelEntity entity : labelEntityMap.values()) {
            JoinLabelAllbumEntity join = new JoinLabelAllbumEntity();
            join.labelId = entity.id;
            join.labelAlbumId = labelAlbumEntity.id;
            MyApplication.getInstances().getDaoSession().getJoinLabelAllbumEntityDao().insert(join);
        }
        imageLock.unlock();

        return null;

    }

    @Override
    protected void onPostExecute(Void t) {
        super.onPostExecute(t);
        if (taskDoneLiteneter != null) {
            taskDoneLiteneter.onDone();
        }
    }

    private void copyBigDataToSD(Context context, String fileName, String strOutFileName) throws Exception {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open(fileName);

        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}
