package com.lenovo.common.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Unique;

import com.lenovo.greendao.gen.DaoSession;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

/**
 * 图片类
 */
@Entity
public class ImageEntity extends BaseEntity {
    @Id(autoincrement = true)
    public Long id;
    
    public String name;//图片名字

    public String path;//图片路径

    public long created;//创建时间

    public long updated;//更新时间

    public long size;//文件大小
    @Unique
    public String uniqueString;//图片名字和更新时期

    public void setUniqueString(){
        uniqueString = name + updated+size;
    }

    @ToMany
    @JoinEntity(
            entity = JoinImageLabelEntity.class,
            sourceProperty = "imageId",
            targetProperty = "labelId"
    )
    @Ignore
    public List<LabelEntity> labelEntityList;//图片标签


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 292757404)
    public synchronized void resetLabelEntityList() {
        labelEntityList = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 79697038)
    public List<LabelEntity> getLabelEntityList() {
        if (labelEntityList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelEntityDao targetDao = daoSession.getLabelEntityDao();
            List<LabelEntity> labelEntityListNew = targetDao._queryImageEntity_LabelEntityList(id);
            synchronized (this) {
                if(labelEntityList == null) {
                    labelEntityList = labelEntityListNew;
                }
            }
        }
        return labelEntityList;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 671944052)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getImageEntityDao() : null;
    }


    /** Used for active entity operations. */
    @Generated(hash = 1017059216)
    private transient ImageEntityDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


    public long getSize() {
        return this.size;
    }


    public void setSize(long size) {
        this.size = size;
    }


    public long getUpdated() {
        return this.updated;
    }


    public void setUpdated(long updated) {
        this.updated = updated;
    }


    public long getCreated() {
        return this.created;
    }


    public void setCreated(long created) {
        this.created = created;
    }


    public String getPath() {
        return this.path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUniqueString() {
        return this.uniqueString;
    }


    public void setUniqueString(String uniqueString) {
        this.uniqueString = uniqueString;
    }


    @Generated(hash = 1782211552)
    public ImageEntity(Long id, String name, String path, long created, long updated, long size,
            String uniqueString) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.created = created;
        this.updated = updated;
        this.size = size;
        this.uniqueString = uniqueString;
    }


    @Generated(hash = 2080458212)
    public ImageEntity() {
    }
   


}
