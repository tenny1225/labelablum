package com.lenovo.common.entity;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by noahkong on 17-6-9.
 */

public class BundleEntity<T> implements Serializable {
    public interface DataResponse<S> {
        void onSuccess(S s);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    private Object extra;


    public BundleEntity(Context context, T data) {
        saveData(context, data);
    }

    public void saveData(Context context, final T data) {
        File file = context.getFilesDir();
        key = "snap_" + data.hashCode();
        final File dataFile = new File(file.getAbsoluteFile() + "/" + key);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
                    outputStream.writeObject(data);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void getData(Context context, final DataResponse<T> response) {
        File file = context.getFilesDir();
        final File dataFile = new File(file.getAbsoluteFile() + "/" + key);
        if (dataFile.exists()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile));
                        Object o = inputStream.readObject();
                        inputStream.close();
                        response.onSuccess((T) o);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    response.onSuccess(null);
                }
            }).start();

        }
        response.onSuccess(null);

    }
}
