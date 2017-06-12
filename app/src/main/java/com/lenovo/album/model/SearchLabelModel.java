package com.lenovo.album.model;

import android.os.AsyncTask;
import android.util.Log;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.LabelEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class SearchLabelModel implements SearchLabelContract.Model {
    @Override
    public void query(final List<String> str, final ModelResponse<List<LabelEntity>> response) {
        new AsyncTask<Void, Void, List<LabelEntity>>() {
            @Override
            protected List<LabelEntity> doInBackground(Void... params) {
                QueryBuilder<LabelEntity> qb = MyApplication.getInstances().getDaoSession().getLabelEntityDao()
                        .queryBuilder();

                WhereCondition[] conditions = new WhereCondition[str.size()];


                for (int i = 0; i < str.size(); i++) {

                    conditions[i]=LabelEntityDao.Properties.Name.like("%"+str.get(i)+"%");
                }

                return whereOr(qb,conditions).list();

            }

            @Override
            protected void onPostExecute(List<LabelEntity> labelEntities) {
                super.onPostExecute(labelEntities);
                Log.e("xz","llll="+labelEntities.size());
                if (response != null) {
                    response.onSuccess(labelEntities);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }
    public static QueryBuilder whereOr(QueryBuilder queryBuilder, WhereCondition[] whereConditions) {
        if (whereConditions == null) return queryBuilder.where(null);
        else if (whereConditions.length == 1) return queryBuilder.where(whereConditions[0]);
        else return queryBuilder.whereOr(whereConditions[0], whereConditions[1], Arrays.copyOfRange(whereConditions, 2, whereConditions.length));
    }
}
