package com.lenovo.common.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by noahkong on 17-6-5.
 */
@Entity
public class JoinLabelAllbumEntity {

    @Id(autoincrement = true)
    public Long id;

    public Long labelAlbumId;

    public Long labelId;

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getLabelAlbumId() {
        return this.labelAlbumId;
    }

    public void setLabelAlbumId(Long labelAlbumId) {
        this.labelAlbumId = labelAlbumId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 2027449160)
    public JoinLabelAllbumEntity(Long id, Long labelAlbumId, Long labelId) {
        this.id = id;
        this.labelAlbumId = labelAlbumId;
        this.labelId = labelId;
    }

    @Generated(hash = 1322324498)
    public JoinLabelAllbumEntity() {
    }


}
