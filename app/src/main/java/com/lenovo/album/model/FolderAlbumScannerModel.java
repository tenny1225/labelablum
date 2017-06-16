package com.lenovo.album.model;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;


import com.lenovo.album.R;
import com.lenovo.album.contract.FolderAlbumScannerContract;
import com.lenovo.album.model.helper.ImageCursorLoader;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.manager.ExecutorServiceFactory;
import com.lenovo.album.base.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by noahkong on 17-6-5.
 */

public class FolderAlbumScannerModel implements FolderAlbumScannerContract.Model, Constant {
    private ImageCursorLoader imageCursorLoader;

    @Override
    public void searchAllAlbum(final Context ctx, final ModelResponse<List<FolderAlbumEntity>> listener) {

        imageCursorLoader = new ImageCursorLoader(ctx.getApplicationContext());


        imageCursorLoader.registerListener(IMAGE_LOADER_ID, new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, final Cursor data) {

                if (data == null || data.getCount() == 0) {
                    return;
                }
                new AsyncTask<Cursor, Void, List<FolderAlbumEntity>>() {
                    @Override
                    protected List<FolderAlbumEntity> doInBackground(Cursor... params) {
                        int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);

                        HashMap<String, ArrayList<String>> folderMap = new HashMap<String, ArrayList<String>>();
                        while (data.moveToNext()) {
                            File imageFile = new File(data.getString(dataColumnIndex));//图片文件
                            File imageFolder = imageFile.getParentFile();

                            if (folderMap.containsKey(imageFolder.getAbsolutePath())) {
                                folderMap.get(imageFolder.getAbsolutePath()).add(imageFile.getAbsolutePath());
                            } else {
                                ArrayList<String> arrayList = new ArrayList<String>();
                                arrayList.add(imageFile.getAbsolutePath());
                                folderMap.put(imageFolder.getAbsolutePath(), arrayList);
                            }
                        }

                        List<FolderAlbumEntity> folderAlbumEntityList = new ArrayList<FolderAlbumEntity>();

                        FolderAlbumEntity all = new FolderAlbumEntity();
                        all.imageList = new ArrayList<ImageEntity>();
                        all.covers = new ArrayList<ImageEntity>();

                        for (String key : folderMap.keySet()) {
                            FolderAlbumEntity fae = new FolderAlbumEntity();
                            File imageFolder = new File(key);
                            fae.path = key;
                            fae.name = imageFolder.getName();
                            fae.updated = imageFolder.lastModified();
                            List<ImageEntity> imageEntityList = new ArrayList<ImageEntity>();
                            fae.imageList = imageEntityList;

                            for (String s : folderMap.get(key)) {
                                ImageEntity imageEntity = new ImageEntity();
                                File imageFile = new File(s);
                                imageEntity.updated = imageFile.lastModified();
                                imageEntity.name = imageFile.getName();
                                imageEntity.path = s;
                                imageEntity.size = imageFile.length();
                                imageEntity.setUniqueString();

                                List<LabelEntity> labelEntityList = new ArrayList<LabelEntity>();
                                imageEntity.labelEntityList = labelEntityList;

                                imageEntityList.add(imageEntity);
                            }
                            fae.sortImageListByUpdatedDate();

                            all.imageList.addAll(fae.imageList);

                            fae.covers = new ArrayList<ImageEntity>();
                            for (int i = 0; i < Constant.COVERS_COUNT; i++) {
                                if (i >= imageEntityList.size()) {
                                    break;
                                }
                                fae.covers.add(imageEntityList.get(i));
                            }

                            folderAlbumEntityList.add(fae);
                        }

                        all.sortImageListByUpdatedDate();

                        for (int i = 0; i < Constant.COVERS_COUNT; i++) {
                            if (i >= all.imageList.size()) {
                                break;
                            }
                            all.covers.add( all.imageList.get(i));
                        }
                        all.name = ctx.getResources().getString(R.string.all);
                        folderAlbumEntityList.add(0,all);

                        return folderAlbumEntityList;
                    }

                    @Override
                    protected void onPostExecute(List<FolderAlbumEntity> folderAlbumEntities) {
                        super.onPostExecute(folderAlbumEntities);
                        listener.onSuccess(folderAlbumEntities);
                    }
                }.executeOnExecutor(ExecutorServiceFactory.createSearchExecutor(), data);

            }

        });
        imageCursorLoader.startLoading();
    }

    @Override
    public void cancelSearch() {
        if (imageCursorLoader != null) {
            imageCursorLoader.cancelLoad();
        }
    }
}
