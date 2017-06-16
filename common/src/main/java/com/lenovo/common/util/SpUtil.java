package com.lenovo.common.util;

import android.content.Context;

/**
 * Created by noahkong on 17-6-13.
 */

public class SpUtil {
    public static final int LABEL_ALBUM = 0;
    public static final int NORMAL_ALBUM = 1;

    private static final String ALBUM= "album";
    private static final String ALBUM_TYPE = "album-type";
    private static final String FIRST_START = "first-start";

    public static void setCurrentAlbumType(Context context,int type){
        context.getSharedPreferences(ALBUM,Context.MODE_APPEND).edit().putInt(ALBUM_TYPE,type).commit();
    }
    public static int getCurrentAlbumType(Context context){
        return context.getSharedPreferences(ALBUM,Context.MODE_APPEND).getInt(ALBUM_TYPE,NORMAL_ALBUM);
    }

    public static boolean isFirstStart(Context context){
        boolean result =  context.getSharedPreferences(ALBUM,Context.MODE_APPEND).getBoolean(FIRST_START,true);
        if(result){
            context.getSharedPreferences(ALBUM,Context.MODE_APPEND).edit().putBoolean(FIRST_START,false).commit();
        }
        return result;
    }
}
