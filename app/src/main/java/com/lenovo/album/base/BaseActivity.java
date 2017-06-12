package com.lenovo.album.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.jaeger.library.StatusBarUtil;
import com.lenovo.common.util.CommonUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by noahkong on 17-6-5.
 */

public abstract class BaseActivity extends SupportActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fullScreen();
        int resource = getRootResource();
        if (resource != 0) {
            setContentView(resource);
        }

        initData(savedInstanceState);
        initWidget();
        initSync();
    }

    protected void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void setStatusBarColor(int color) {
        //View rootView = getWindow().getDecorView();
        //rootView.setPadding(0,0,0,0);
    }


    /**
     * 布局未加载前的初始化工作
     */
    protected void init() {

    }

    /**
     * 返回activity根布局
     *
     * @return 布局id
     */
    protected abstract int getRootResource();

    /**
     * 初始化数据方法
     */
    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 初始化view方法
     */
    protected void initWidget() {

    }

    /**
     * 初始同步方法
     */
    protected void initSync() {

    }

    /**
     * 设置全屏
     */
    public void setFullScreen() {

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;

        getWindow().setAttributes(params);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    /**
     * 取消全屏
     */
    public void cancelFullScreen() {

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setAttributes(params);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
