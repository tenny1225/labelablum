package com.lenovo.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.LabelEntity;

import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.JoinImageLabelEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig imageEntityDaoConfig;
    private final DaoConfig joinImageLabelEntityDaoConfig;
    private final DaoConfig labelEntityDaoConfig;

    private final ImageEntityDao imageEntityDao;
    private final JoinImageLabelEntityDao joinImageLabelEntityDao;
    private final LabelEntityDao labelEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        imageEntityDaoConfig = daoConfigMap.get(ImageEntityDao.class).clone();
        imageEntityDaoConfig.initIdentityScope(type);

        joinImageLabelEntityDaoConfig = daoConfigMap.get(JoinImageLabelEntityDao.class).clone();
        joinImageLabelEntityDaoConfig.initIdentityScope(type);

        labelEntityDaoConfig = daoConfigMap.get(LabelEntityDao.class).clone();
        labelEntityDaoConfig.initIdentityScope(type);

        imageEntityDao = new ImageEntityDao(imageEntityDaoConfig, this);
        joinImageLabelEntityDao = new JoinImageLabelEntityDao(joinImageLabelEntityDaoConfig, this);
        labelEntityDao = new LabelEntityDao(labelEntityDaoConfig, this);

        registerDao(ImageEntity.class, imageEntityDao);
        registerDao(JoinImageLabelEntity.class, joinImageLabelEntityDao);
        registerDao(LabelEntity.class, labelEntityDao);
    }
    
    public void clear() {
        imageEntityDaoConfig.getIdentityScope().clear();
        joinImageLabelEntityDaoConfig.getIdentityScope().clear();
        labelEntityDaoConfig.getIdentityScope().clear();
    }

    public ImageEntityDao getImageEntityDao() {
        return imageEntityDao;
    }

    public JoinImageLabelEntityDao getJoinImageLabelEntityDao() {
        return joinImageLabelEntityDao;
    }

    public LabelEntityDao getLabelEntityDao() {
        return labelEntityDao;
    }

}