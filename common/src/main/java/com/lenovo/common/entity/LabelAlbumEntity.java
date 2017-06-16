package com.lenovo.common.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.lenovo.greendao.gen.DaoSession;
import com.lenovo.greendao.gen.LabelAlbumEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

/**
 * 标签相册类
 */
@Entity
public class LabelAlbumEntity extends AlbumEntity {
    @Id(autoincrement = true)
    public Long id;

    public String name;//相册名字

    public String alias;//相册别名

    public long created;//创建时间

    public long updated;//更新时间

    public long sortIndex;


    @Transient
    public List<ImageEntity> covers;//封面


    @ToMany
    @JoinEntity(
            entity = JoinLabelAllbumEntity.class,
            sourceProperty = "labelAlbumId",
            targetProperty = "labelId"
    )
    public List<LabelEntity> labelEntityList;//相册标签


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
    @Generated(hash = 465841822)
    public List<LabelEntity> getLabelEntityList() {
        if (labelEntityList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelEntityDao targetDao = daoSession.getLabelEntityDao();
            List<LabelEntity> labelEntityListNew = targetDao._queryLabelAlbumEntity_LabelEntityList(id);
            synchronized (this) {
                if(labelEntityList == null) {
                    labelEntityList = labelEntityListNew;
                }
            }
        }
        return labelEntityList;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 412015278)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelAlbumEntityDao() : null;
    }


    /** Used for active entity operations. */
    @Generated(hash = 914741807)
    private transient LabelAlbumEntityDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;


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


    public String getAlias() {
        return this.alias;
    }


    public void setAlias(String alias) {
        this.alias = alias;
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


    public long getSortIndex() {
        return this.sortIndex;
    }


    public void setSortIndex(long sortIndex) {
        this.sortIndex = sortIndex;
    }


    @Generated(hash = 185654918)
    public LabelAlbumEntity(Long id, String name, String alias, long created, long updated,
            long sortIndex) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.created = created;
        this.updated = updated;
        this.sortIndex = sortIndex;
    }


    @Generated(hash = 391934765)
    public LabelAlbumEntity() {
    }
}
