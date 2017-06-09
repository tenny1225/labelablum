package com.lenovo.album.ui.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.jaeger.library.StatusBarUtil;
import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.album.ui.adapter.ImagePagerAdapter;

import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.util.CommonUtil;
import com.lenovo.common.util.DateUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by noahkong on 17-6-8.
 */

public class ImageShowFragment extends BaseFragment {
    private final static String ALBUM_ENTITY = "param";

    public static ImageShowFragment newInstance(BundleEntity<AlbumEntity> albumEntity) {

        Bundle args = new Bundle();
        args.putSerializable(ALBUM_ENTITY, albumEntity);
        ImageShowFragment fragment = new ImageShowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ViewPager vp;
    private ImageView ivBack;
    private TextView tvImageName;


    private AlbumEntity albumEntity;


    public void setAlbumEntity(AlbumEntity albumEntity) {
        this.albumEntity = albumEntity;
    }

    @Override
    protected int getViewResource() {
        return R.layout.fragment_image_show;
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        ((BundleEntity<AlbumEntity>) getArguments().getSerializable(ALBUM_ENTITY)).getData(activity, new BundleEntity.DataResponse<AlbumEntity>() {
            @Override
            public void onSuccess(AlbumEntity entity) {
                if (albumEntity == null) {
                    albumEntity = entity;
                    initWidgetData();
                }

            }
        });
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);


        vp = (ViewPager) rootView.findViewById(R.id.vp);
        vp.setPageMargin(AutoUtils.getPercentWidthSize(40));

        tvImageName = (TextView) rootView.findViewById(R.id.tv_image_name);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        if (albumEntity != null) {
            initWidgetData();
        }

    }

    private void initWidgetData() {
        try {
            Field field = vp.getClass().getDeclaredField("mCurItem");
            field.setAccessible(true);
            field.setInt(vp, albumEntity.currentIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setCurrentImage(albumEntity.currentIndex);

        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return albumEntity.imageList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(activity);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    view.setTransitionName(getResources().getString(R.string.album_share_element_name));
                }

                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageLoaderFactory.getLoader().displayQuality(view, albumEntity.imageList.get(position).path);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setCurrentImage(int position) {
        albumEntity.currentIndex = position;
        long timestamp = albumEntity.imageList.get(albumEntity.currentIndex).updated;
        tvImageName.setText(DateUtil.timestamp2String(getResources().getString(R.string.image_date_fromat), timestamp));
    }
}
