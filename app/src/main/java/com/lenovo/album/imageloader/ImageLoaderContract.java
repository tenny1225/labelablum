package com.lenovo.album.imageloader;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

/**
 * Created by noahkong on 17-6-8.
 */

public interface ImageLoaderContract {

    void display(ImageView iv,String url);
    void displayQuality(ImageView iv,String url);
    void display(ImageView iv,int id);
    void display(ImageView iv,int defaultId,String url);
    void display(ImageView iv,int defaultId,int errorId,String url);
}
