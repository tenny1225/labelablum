package com.lenovo.album.base;

import android.provider.MediaStore;

/**
 * Created by noahkong on 17-6-5.
 */

public interface Constant {
    int COVERS_COUNT = 1;




    String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件名，包括后缀名
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };
    int IMAGE_LOADER_ID = 1000;
}
