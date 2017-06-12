package com.lenovo.album.model;

import android.os.AsyncTask;

import com.lenovo.album.MyApplication;
import com.lenovo.album.base.BaseContract;
import com.lenovo.album.contract.LabelEditContract;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.greendao.gen.LabelEntityDao;

import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelEditModel implements LabelEditContract.Model {
    @Override
    public void getAllLabels(final ModelResponse<LabelEditContract.LabelsResult> response) {
        new AsyncTask<Void, Void, LabelEditContract.LabelsResult>() {
            @Override
            protected LabelEditContract.LabelsResult doInBackground(Void... params) {
                LabelEditContract.LabelsResult result = new LabelEditContract.LabelsResult();
                result.selectedLabels = MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder()
                        .where(LabelEntityDao.Properties.Selected.eq(true))
                        .orderDesc(LabelEntityDao.Properties.OrderIndex)
                        .list();

                result.notSelectLabels = MyApplication.getInstances().getDaoSession().getLabelEntityDao().queryBuilder()
                        .where(LabelEntityDao.Properties.Selected.eq(false))
                        .orderDesc(LabelEntityDao.Properties.OrderIndex)
                        .list();


                return result;
            }

            @Override
            protected void onPostExecute(LabelEditContract.LabelsResult labelsResult) {
                super.onPostExecute(labelsResult);
                if (response != null) {
                    response.onSuccess(labelsResult);
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }

    @Override
    public void updateLabels(final List<LabelEntity> labelEntities, final ModelResponse<String> response) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                LabelEntityDao labelEntityDao = MyApplication.getInstances().getDaoSession().getLabelEntityDao();
                labelEntityDao.updateInTx(labelEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (response != null) {
                    response.onSuccess("success");
                }
            }
        }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor());
    }
}
