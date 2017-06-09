package com.lenovo.album.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseActivity;
import com.lenovo.album.ui.fragment.AlbumListFragment;

/**
 * Created by noahkong on 17-6-7.
 */

public class MainActivity extends BaseActivity {
    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected int getRootResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        loadRootFragment(R.id.frame, AlbumListFragment.newInstance());
    }

    /**
     * 跳转到MainActivity
     *
     * @param activity
     */
    public static void startSelf(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
