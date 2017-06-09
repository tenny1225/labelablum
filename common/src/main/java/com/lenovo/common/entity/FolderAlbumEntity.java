package com.lenovo.common.entity;

import java.util.List;

/**
 * 文件夹相册类
 */

public class FolderAlbumEntity extends AlbumEntity {
    public String name;//相册名字

    public long created;//创建时间

    public long updated;//更新时间

    public String path;//相册路径

    public List<ImageEntity> covers;//封面


}
