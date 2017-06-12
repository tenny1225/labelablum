package com.lenovo.album.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.jaeger.library.StatusBarUtil;
import com.lenovo.album.R;
import com.lenovo.album.base.BaseActivity;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.contract.ImageLabelScannerContract;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.album.presenter.ImageLabelScannerPresenter;
import com.lenovo.album.ui.adapter.ImagePagerAdapter;

import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.album.ui.widget.ImageEditPopWindow;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;
import com.lenovo.common.util.DateUtil;
import com.transitionseverywhere.Explode;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by noahkong on 17-6-8.
 */

public class ImageShowFragment extends BaseFragment implements ImageLabelScannerContract.View, ImageEditPopWindow.ImageActionPopWindowListener {
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
    private ImageView ivMore;
    private TextView tvImageName;

    private MyPagerAdapter adapter;

    private AutoRelativeLayout actionBar;
    private HorizontalScrollView scrollLabelContainer;
    private AutoLinearLayout labelContainer;


    private AlbumEntity albumEntity;

    private ImageLabelScannerContract.Presenter presenter;

    private ImageEditPopWindow popWindow;


    public void setAlbumEntity(AlbumEntity albumEntity) {
        this.albumEntity = albumEntity;
    }

    @Override
    protected int getViewResource() {
        return R.layout.fragment_image_show;
    }

    @Override
    protected int getStatusBarColor() {

        return -1;
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
        presenter = new ImageLabelScannerPresenter(activity, this);
    }

    @Override
    protected void initWidget(final View rootView) {
        super.initWidget(rootView);


        actionBar = (AutoRelativeLayout) rootView.findViewById(R.id.action_bar);
        actionBar.setPadding(0, (int) CommonUtil.getStatusBarHeight(activity), 0, 0);

        labelContainer = (AutoLinearLayout) rootView.findViewById(R.id.ll_label_container);
        scrollLabelContainer = (HorizontalScrollView) rootView.findViewById(R.id.scroll_label_container);

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
        ivMore = (ImageView) rootView.findViewById(R.id.iv_more);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow = new ImageEditPopWindow(activity);
                popWindow.setImageActionPopWindowListener(ImageShowFragment.this);
                popWindow.showAtLocation(rootView, Gravity.TOP | Gravity.RIGHT, 0, actionBar.getHeight());
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

        adapter = new MyPagerAdapter();
        vp.setAdapter(adapter);

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

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return albumEntity.imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vp_item_image, container, false);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setFullScreenOrCancelFullScreen();
                }
            });
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ImageLoaderFactory.getLoader().displayQuality(photoView, albumEntity.imageList.get(position).path);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void setCurrentImage(int position) {
        albumEntity.currentIndex = position;
        long timestamp = albumEntity.imageList.get(albumEntity.currentIndex).updated;
        tvImageName.setText(DateUtil.timestamp2String(getResources().getString(R.string.image_date_fromat), timestamp));
        clearLabels();
        presenter.queryLabels(albumEntity.imageList.get(position).uniqueString);
    }


    private void setFullScreenOrCancelFullScreen() {
        if (activity == null) {
            return;
        }
        TransitionSet transitionSet = new TransitionSet();

        TransitionSet topTransition = new TransitionSet();
        topTransition.addTransition(new Slide(Gravity.TOP));
        topTransition.addTarget(actionBar);

        transitionSet.addTransition(topTransition);

        TransitionSet bottomTransition = new TransitionSet();
        bottomTransition.addTransition(new Slide(Gravity.BOTTOM));
        bottomTransition.addTarget(labelContainer);

        transitionSet.addTransition(bottomTransition);
        TransitionManager.endTransitions((ViewGroup) rootView);
        TransitionManager.beginDelayedTransition((ViewGroup) rootView, transitionSet);

        if (CommonUtil.isFullScreen(activity)) {

            ((BaseActivity) activity).cancelFullScreen();
            actionBar.setVisibility(View.VISIBLE);
            labelContainer.setVisibility(View.VISIBLE);
        } else {

            ((BaseActivity) activity).setFullScreen();
            actionBar.setVisibility(View.GONE);
            labelContainer.setVisibility(View.GONE);

        }
    }

    private void clearLabels() {
        labelContainer.removeAllViews();
    }

    @Override
    public void onShare() {
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(albumEntity.imageList.get(albumEntity.currentIndex).path));
        startActivity(Intent.createChooser(imageIntent, getResources().getString(R.string.app_name)));
    }

    @Override
    public void onRecognition() {
        String path = albumEntity.imageList.get(albumEntity.currentIndex).path;
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onEdit() {
        String path = albumEntity.imageList.get(albumEntity.currentIndex).path;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (popWindow != null && popWindow.isShowing()) {
            popWindow.dismiss();
            popWindow = null;
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void showLabels(List<LabelEntity> labelEntityList) {
        TransitionSet transitionSet = new TransitionSet();
        int n = 0;
        for (LabelEntity entity : labelEntityList) {
            FilletView filletView = new FilletView(activity);
            filletView.setBgColor(Color.RED);
            filletView.setTextColor(Color.WHITE);
            filletView.setIcon(null);
            filletView.setTextSize(AutoUtils.getPercentWidthSize(25));
            filletView.setTextMargin(AutoUtils.getPercentWidthSize(20));
            filletView.setPadding(AutoUtils.getPercentWidthSize(18), 0, AutoUtils.getPercentWidthSize(9), 0);
            filletView.setText(entity.name );
            filletView.setLayoutParams(new AutoRelativeLayout.LayoutParams(-2, AutoUtils.getPercentHeightSize(55)));
            filletView.setVisibility(View.INVISIBLE);
            labelContainer.addView(filletView);
            TransitionSet set = new TransitionSet();
            set.addTarget(filletView).setStartDelay(n).addTransition(new Slide(Gravity.BOTTOM));
            transitionSet.addTransition(set);
            n += 120;

        }
        TransitionManager.endTransitions((ViewGroup) rootView);
        TransitionManager.beginDelayedTransition((ViewGroup) rootView, transitionSet);

        for (int i = 0; i < labelContainer.getChildCount(); i++) {
            View v = labelContainer.getChildAt(i);
            v.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroyView() {
        presenter.destroy();
        ((BaseActivity) activity).cancelFullScreen();
        super.onDestroyView();
    }
}
