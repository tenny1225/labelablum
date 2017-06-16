package com.lenovo.album.model;

import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.JoinImageLabelEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.ImageEntityDao;
import com.lenovo.greendao.gen.JoinImageLabelEntityDao;
import com.lenovo.greendao.gen.LabelEntityDao;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by noahkong on 17-6-12.
 */

public class SearchLabelModel implements SearchLabelContract.Model {
    @Override
    public void queryCandidate(final List<String> str, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void, Void, List<LabelEntity>>() {
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {
                QueryBuilder<LabelEntity> qb = MyApplication.getInstances().getDaoSession().getLabelEntityDao()
                        .queryBuilder();

                WhereCondition[] conditions = new WhereCondition[str.size() * 2];


                for (int i = 0; i < conditions.length; i += 2) {

                    conditions[i] = LabelEntityDao.Properties.Name.like("%" + str.get(i % str.size()) + "%");

                }
                for (int i = 1; i < conditions.length; i += 2) {

                    conditions[i] = LabelEntityDao.Properties.Alias.like("%" + str.get((i - 1) % str.size()) + "%");

                }


                List<LabelEntity> list = whereOr(qb, conditions).list();
                for (LabelEntity entity : list) {
                    entity.getImageEntityList();
                    entity.sortImageListByUpdatedDate();
                }
                return list;

            }

            @Override
            protected void onPostExecute(List<LabelEntity> labelEntities) {
                super.onPostExecute(labelEntities);

                if (response != null) {
                    response.onSuccess(labelEntities);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }


    @Override
    public void queryImages(final List<String> str, final ModelResponse<List<ImageEntity>> response) {
        new AsyncTask<Void, Void, List<ImageEntity>>() {
            @Override
            protected List<ImageEntity> doInBackground(Void... params) {

                Set<String> set = new HashSet<String>();
                set.addAll(str);

                List<ImageEntity> imageEntities = new ArrayList<ImageEntity>();
                List<ImageEntity> list = MyApplication.getInstances().getDaoSession().getImageEntityDao()
                        .loadAll();
                Iterator<ImageEntity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    ImageEntity entity = iterator.next();
                    File f = new File(entity.path);
                    if (!f.exists()) {
                        iterator.remove();
                        continue;
                    }
                    int n = 0;
                    entity.resetLabelEntityList();
                    for (LabelEntity labelEntity : entity.getLabelEntityList()) {
                        if (set.contains(labelEntity.name) || set.contains(labelEntity.alias)) {
                            n++;
                        }
                    }
                    if (n == str.size()) {

                        entity.getLabelEntityList().clear();

                        imageEntities.add(entity);
                    }
                }
                set = null;
                return imageEntities;

            }

            @Override
            protected void onPostExecute(List<ImageEntity> labelEntities) {
                super.onPostExecute(labelEntities);

                if (response != null) {
                    response.onSuccess(labelEntities);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    public static QueryBuilder whereOr(QueryBuilder queryBuilder, WhereCondition[] whereConditions) {
        if (whereConditions == null && whereConditions.length == 0)
            return queryBuilder.where(null);
        else if (whereConditions.length == 1)
            return queryBuilder.where(whereConditions[0]);
        else
            return queryBuilder.whereOr(
                    whereConditions[0],
                    whereConditions[1],
                    Arrays.copyOfRange(whereConditions, 2, whereConditions.length)
            );
    }
}
