package com.lenovo.common.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public class AlbumEntity extends BaseEntity {

    public int currentIndex;

    public List<ImageEntity> imageList;//相册图片

    public void sortImageListByUpdatedDate() {
        if (imageList == null) {
            return;
        }
        Collections.sort(imageList, new Comparator<ImageEntity>() {
            @Override
            public int compare(ImageEntity o1, ImageEntity o2) {
                if (o1.updated < o2.updated) {
                    return 1;
                }
                return -1;
            }
        });
    }
}
