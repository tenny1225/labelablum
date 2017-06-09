package com.lenovo.common.entity;

import java.util.List;

/**
 * 标签相册类
 */

public class LabelAlbumEntity extends AlbumEntity {
    public String name;//相册名字

    public String alias;//相册别名

    public long created;//创建时间

    public long updated;//更新时间

    public List<ImageEntity> covers;//封面



    public List<LabelEntity> labelEntityList;//相册标签
}
