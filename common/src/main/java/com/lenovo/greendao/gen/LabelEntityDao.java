package com.lenovo.greendao.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.JoinLabelAllbumEntity;

import com.lenovo.common.entity.LabelEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LABEL_ENTITY".
*/
public class LabelEntityDao extends AbstractDao<LabelEntity, Long> {

    public static final String TABLENAME = "LABEL_ENTITY";

    /**
     * Properties of entity LabelEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Alias = new Property(2, String.class, "alias", false, "ALIAS");
        public final static Property Selected = new Property(3, boolean.class, "selected", false, "SELECTED");
        public final static Property OrderIndex = new Property(4, long.class, "orderIndex", false, "ORDER_INDEX");
        public final static Property Deleted = new Property(5, boolean.class, "deleted", false, "DELETED");
    };

    private DaoSession daoSession;

    private Query<LabelEntity> imageEntity_LabelEntityListQuery;
    private Query<LabelEntity> labelAlbumEntity_LabelEntityListQuery;

    public LabelEntityDao(DaoConfig config) {
        super(config);
    }
    
    public LabelEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LABEL_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT UNIQUE ," + // 1: name
                "\"ALIAS\" TEXT," + // 2: alias
                "\"SELECTED\" INTEGER NOT NULL ," + // 3: selected
                "\"ORDER_INDEX\" INTEGER NOT NULL ," + // 4: orderIndex
                "\"DELETED\" INTEGER NOT NULL );"); // 5: deleted
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LABEL_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LabelEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String alias = entity.getAlias();
        if (alias != null) {
            stmt.bindString(3, alias);
        }
        stmt.bindLong(4, entity.getSelected() ? 1L: 0L);
        stmt.bindLong(5, entity.getOrderIndex());
        stmt.bindLong(6, entity.getDeleted() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LabelEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String alias = entity.getAlias();
        if (alias != null) {
            stmt.bindString(3, alias);
        }
        stmt.bindLong(4, entity.getSelected() ? 1L: 0L);
        stmt.bindLong(5, entity.getOrderIndex());
        stmt.bindLong(6, entity.getDeleted() ? 1L: 0L);
    }

    @Override
    protected final void attachEntity(LabelEntity entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LabelEntity readEntity(Cursor cursor, int offset) {
        LabelEntity entity = new LabelEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // alias
            cursor.getShort(offset + 3) != 0, // selected
            cursor.getLong(offset + 4), // orderIndex
            cursor.getShort(offset + 5) != 0 // deleted
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LabelEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAlias(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSelected(cursor.getShort(offset + 3) != 0);
        entity.setOrderIndex(cursor.getLong(offset + 4));
        entity.setDeleted(cursor.getShort(offset + 5) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LabelEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LabelEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "labelEntityList" to-many relationship of ImageEntity. */
    public List<LabelEntity> _queryImageEntity_LabelEntityList(Long imageId) {
        synchronized (this) {
            if (imageEntity_LabelEntityListQuery == null) {
                QueryBuilder<LabelEntity> queryBuilder = queryBuilder();
                queryBuilder.join(JoinImageLabelEntity.class, JoinImageLabelEntityDao.Properties.LabelId)
                    .where(JoinImageLabelEntityDao.Properties.ImageId.eq(imageId));
                imageEntity_LabelEntityListQuery = queryBuilder.build();
            }
        }
        Query<LabelEntity> query = imageEntity_LabelEntityListQuery.forCurrentThread();
        query.setParameter(0, imageId);
        return query.list();
    }

    /** Internal query to resolve the "labelEntityList" to-many relationship of LabelAlbumEntity. */
    public List<LabelEntity> _queryLabelAlbumEntity_LabelEntityList(Long labelAlbumId) {
        synchronized (this) {
            if (labelAlbumEntity_LabelEntityListQuery == null) {
                QueryBuilder<LabelEntity> queryBuilder = queryBuilder();
                queryBuilder.join(JoinLabelAllbumEntity.class, JoinLabelAllbumEntityDao.Properties.LabelId)
                    .where(JoinLabelAllbumEntityDao.Properties.LabelAlbumId.eq(labelAlbumId));
                labelAlbumEntity_LabelEntityListQuery = queryBuilder.build();
            }
        }
        Query<LabelEntity> query = labelAlbumEntity_LabelEntityListQuery.forCurrentThread();
        query.setParameter(0, labelAlbumId);
        return query.list();
    }

}
