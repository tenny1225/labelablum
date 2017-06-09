package com.lenovo.common.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.lenovo.common.R;

/**
 * Created by noahkong on 17-6-9.
 */

public class CommonUtil {
    public static float getStatusBarHeight(Context context) {
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public static int getAttrColor(Context context, int id) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{id});
        int color = typedArray.getColor(0, Color.WHITE);
        typedArray.recycle();
        return color;
    }
}
