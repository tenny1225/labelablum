package com.lenovo.album.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lenovo.common.util.CommonUtil;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by noahkong on 17-6-5.
 */

public abstract class BaseFragment extends SupportFragment {

    protected Activity activity;
    protected View rootView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getViewResource(), container, false);
        int statusBarColor = getStatusBarColor();
        if (statusBarColor != -1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (statusBarColor == 0) {
                rootView = view;
                rootView.setPadding(0, (int) CommonUtil.getStatusBarHeight(activity), 0, 0);
            } else {
                LinearLayout root = new LinearLayout(container.getContext());
                root.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                root.setOrientation(LinearLayout.VERTICAL);

                View statusBarView = new View(root.getContext());
                statusBarView.setBackgroundColor(statusBarColor);

                root.addView(statusBarView, new LinearLayout.LayoutParams(-1, (int) CommonUtil.getStatusBarHeight(activity)));

                root.addView(view, new LinearLayout.LayoutParams(-1, -1));
                rootView = root;
            }
        } else {
            rootView = view;
        }
        initData(savedInstanceState);
        initWidget(rootView);
        initSync();
        return rootView;
    }
    protected <T> T $(int id){
        return (T) rootView.findViewById(id);
    }

    /**
     * 设置状态栏颜色
     *
     * @return
     */
    protected int getStatusBarColor() {
        return -1;
    }

    /**
     * 返回activity根布局
     *
     * @return 布局id
     */
    protected abstract int getViewResource();

    /**
     * 初始化数据方法
     */
    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 初始化view方法
     */
    protected void initWidget(View rootView) {

    }

    /**
     * 初始同步方法
     */
    protected void initSync() {

    }
}
