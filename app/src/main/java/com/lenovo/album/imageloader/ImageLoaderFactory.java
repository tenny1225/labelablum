package com.lenovo.album.imageloader;

/**
 * Created by noahkong on 17-6-8.
 */

public class ImageLoaderFactory {
    private static ImageLoaderContract loaderContract;

    public static ImageLoaderContract getLoader() {
        if (loaderContract == null) {
            loaderContract = new GlideImageLoader();
        }
       /* synchronized (ImageLoaderFactory.class) {
            loaderContract = new GlideImageLoader();
        }*/
        return loaderContract;
    }
}
