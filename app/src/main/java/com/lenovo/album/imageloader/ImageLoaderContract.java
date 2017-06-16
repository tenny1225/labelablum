package com.lenovo.album.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by noahkong on 17-6-8.
 */

public interface ImageLoaderContract {

    void display(ImageView iv,String url);
    void displayCircle(ImageView iv,String url);
    void displayQuality(ImageView iv,String url);
    void display(ImageView iv,int id);
    void displayCircle(ImageView iv,int id);
    void display(ImageView iv,int defaultId,String url);
    void display(ImageView iv,int defaultId,int errorId,String url);
    Bitmap getBitmap(Context context,String url);
    void clear(View v);
    void clearMemory(Context context);
    void trimMemory(Context context,int level);
}
