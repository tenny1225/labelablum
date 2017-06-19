package com.lenovo.common.entity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

    public void saveData(final Context context, final T data) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = context.getFilesDir();
                key = "snap_" + System.currentTimeMillis();
                final File dataFile = new File(file.getAbsoluteFile() + "/" + key);
                if (!dataFile.exists()) {
                    try {
                        dataFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

               /* try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
                    outputStream.writeObject(data);
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

                try {
                    String json = new Gson().toJson(data);
                    OutputStream outputStream = new FileOutputStream(dataFile);
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

   /* public void getData(Context context, final DataResponse<T> response) {
        File file = context.getFilesDir();
        final File dataFile = new File(file.getAbsoluteFile() + "/" + key);
        if (!dataFile.exists()) {
            return;
        }
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {


                T o = null;

                try {
                    Log.e("xz", "restore");
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile));
                    o = (T) inputStream.readObject();
                    inputStream.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return o;
            }

            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                if (response != null)
                    response.onSuccess(t);
            }
        }.execute();

    }*/
    public void getData(Context context,final Class<T> cla, final DataResponse<T> response) {
        File file = context.getFilesDir();
        final File dataFile = new File(file.getAbsoluteFile() + "/" + key);
        if (!dataFile.exists()) {
            return;
        }
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {

                T o = null;

                try {
                    Log.e("xz", "restore");
                    InputStream inputStream = new FileInputStream(dataFile);
                    byte[] buffer = new byte[512];
                    StringBuilder builder = new StringBuilder();
                    int n = 0;
                    while ((n = inputStream.read(buffer)) != -1) {
                        builder.append(new String(buffer, 0, n));
                    }
                    String json = builder.toString();
                    inputStream.close();

                    o = deserialize(json,cla);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return o;

            }

            @Override
            protected void onPostExecute(T t) {
                super.onPostExecute(t);
                if (response != null)
                    response.onSuccess(t);
            }
        }.execute();

    }
    public static <T> T deserialize(String jsonString, Class<T> clazz) {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("MM/dd/yy HH:mm:ss");
        builder.addSerializationExclusionStrategy(new IgnoreStrategy());
        Gson gson = builder.create();
        return gson.fromJson(jsonString, clazz);
    }
}
