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
import com.lenovo.greendao.gen.LabelEntityDao;
import com.lenovo.greendao.gen.ImageEntityDao;

/**
 * 标签类
 */
@Entity
public class LabelEntity extends BaseEntity {
    @Id(autoincrement = true)
    public Long id;
    @Unique
    public String name;//标签名称
    public String alias;//标签别名
    public boolean selected;//是否显示
    public long orderIndex;//排序
    @ToMany
    @JoinEntity(
            entity = JoinImageLabelEntity.class,
            sourceProperty = "labelId",
            targetProperty = "imageId"
    )
    public List<ImageEntity> imageEntityList;
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
    @Generated(hash = 109694882)
    public synchronized void resetImageEntityList() {
        imageEntityList = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 428220030)
    public List<ImageEntity> getImageEntityList() {
        if (imageEntityList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ImageEntityDao targetDao = daoSession.getImageEntityDao();
            List<ImageEntity> imageEntityListNew = targetDao._queryLabelEntity_ImageEntityList(id);
            synchronized (this) {
                if(imageEntityList == null) {
                    imageEntityList = imageEntityListNew;
                }
            }
        }
        return imageEntityList;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 162841144)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelEntityDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 957726387)
    private transient LabelEntityDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public boolean getSelected() {
        return this.selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
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
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public long getOrderIndex() {
        return this.orderIndex;
    }
    public void setOrderIndex(long orderIndex) {
        this.orderIndex = orderIndex;
    }
    @Generated(hash = 390614855)
    public LabelEntity(Long id, String name, String alias, boolean selected, long orderIndex) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.selected = selected;
        this.orderIndex = orderIndex;
    }
    @Generated(hash = 1645598186)
    public LabelEntity() {
    }
}
