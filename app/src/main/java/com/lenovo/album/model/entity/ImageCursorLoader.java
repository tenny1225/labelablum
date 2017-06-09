package com.lenovo.album.model.entity;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.lenovo.album.base.Constant;

/**
 * Created by noahkong on 17-6-6.
 */

public class ImageCursorLoader extends AsyncTaskLoader<Cursor> implements Constant {
    ForceLoadContentObserver observer = new ForceLoadContentObserver();
    private Context context;
    private Cursor mCursor;

    public ImageCursorLoader(Context context) {
        super(context);
        this.context = context.getApplicationContext();
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER
        );
        if (cursor != null) {
            cursor.registerContentObserver(observer);
            cursor.setNotificationUri(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        return cursor;
    }

    /* 在ui 线程里运作 */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

}
