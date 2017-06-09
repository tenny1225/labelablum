package com.lenovo.common.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by noahkong on 17-6-5.
 */
@Entity
public class JoinImageLabelEntity {

    @Id(autoincrement = true)
    public Long id;

    public Long imageId;

    public Long labelId;

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getImageId() {
        return this.imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 701989728)
    public JoinImageLabelEntity(Long id, Long imageId, Long labelId) {
        this.id = id;
        this.imageId = imageId;
        this.labelId = labelId;
    }

    @Generated(hash = 1154792109)
    public JoinImageLabelEntity() {
    }
}
