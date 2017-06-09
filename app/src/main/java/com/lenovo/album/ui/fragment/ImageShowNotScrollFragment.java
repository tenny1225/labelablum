package com.lenovo.album.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.util.CommonUtil;
import com.lenovo.common.util.DateUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Field;

import static com.lenovo.album.R.id.vp;


/**
 * Created by noahkong on 17-6-8.
 */

public class ImageShowNotScrollFragment extends BaseFragment {
    private final static String ALBUM_ENTITY = "param";

    public static ImageShowNotScrollFragment newInstance(ImageEntity entity) {

        Bundle args = new Bundle();
        //args.putSerializable(ALBUM_ENTITY, entity);
        ImageShowNotScrollFragment fragment = new ImageShowNotScrollFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private PhotoView photoView;
    private ImageView ivBack;
    private TextView tvImageName;


    private ImageEntity imageEntity;


    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    @Override
    protected int getViewResource() {
        return R.layout.fragment_image_not_scroll_show;
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
       //imageEntity = (ImageEntity) getArguments().getSerializable(ALBUM_ENTITY);
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);


        photoView = (PhotoView) rootView.findViewById(R.id.photo_view);
        photoView.enable();


        tvImageName = (TextView) rootView.findViewById(R.id.tv_image_name);
        tvImageName.setText(DateUtil.timestamp2String(getString(R.string.image_date_fromat),imageEntity.updated));
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });





    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        ImageLoaderFactory.getLoader().displayQuality(photoView,imageEntity.path);
    }
}
