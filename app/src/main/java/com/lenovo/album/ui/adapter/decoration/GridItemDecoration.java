package com.lenovo.album.ui.adapter.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by noahkong on 17-6-8.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Paint paint;

    public GridItemDecoration() {
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = parent.getChildCount();
        for (int i = 0; i < itemCount; i++) {

            View v = parent.getChildAt(i);

            {
                float left = v.getLeft();
                float top = v.getTop();
                float right = v.getRight();
                float bottom = 0;
                if (i - 3 < 0) {
                    bottom = top + AutoUtils.getPercentHeight1px() * 10;
                } else {
                    bottom = top + AutoUtils.getPercentHeight1px() * 5;
                }


                c.drawRect(left, top, right, bottom, paint);
            }
            {
                float left = v.getLeft();
                float bottom = v.getBottom();
                float top = 0;
                if (i + 3 >= itemCount) {
                    top = bottom - AutoUtils.getPercentHeight1px() * 10;
                } else {
                    top = bottom - AutoUtils.getPercentHeight1px() * 5;
                }

                float right = v.getRight();


                c.drawRect(left, top, right, bottom, paint);
            }


            {
                float left = v.getLeft();
                float top = v.getTop();
                float right = 0;
                if (i % 3 == 0) {
                    right = left + AutoUtils.getPercentHeight1px() * 10;
                } else {
                    right = left + AutoUtils.getPercentHeight1px() * 5;
                }

                float bottom = v.getBottom();


                c.drawRect(left, top, right, bottom, paint);
            }
            {

                float top = v.getTop();
                float right = v.getRight();
                float left;
                if (i % 3 == 2) {
                    left = right - AutoUtils.getPercentHeight1px() * 10;
                } else {
                    left = right - AutoUtils.getPercentHeight1px() * 5;
                }

                float bottom = v.getBottom();

                c.drawRect(left, top, right, bottom, paint);
            }

        }
    }
}
