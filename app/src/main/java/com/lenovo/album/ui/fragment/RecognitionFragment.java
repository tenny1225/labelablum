package com.lenovo.album.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.contract.RecogniationContract;
import com.lenovo.album.imageloader.ImageLoaderFactory;
import com.lenovo.album.presenter.RecognitionPresenter;
import com.lenovo.album.ui.adapter.RecognitionLabelAdapter;
import com.lenovo.album.ui.widget.LoadingDialogUtil;
import com.lenovo.common.util.CommonUtil;

/**
 * Created by noahkong on 17-6-15.
 */

public class RecognitionFragment extends BaseFragment implements RecogniationContract.View {
    private static final String PATH = "path";
    private static final String TYPE = "type";
    public static final int REMOTE = 1;
    public static final int CAMERA = 0;

    public static RecognitionFragment newInstance(String path, int type) {

        Bundle args = new Bundle();
        args.putString(PATH, path);
        args.putInt(TYPE, type);
        RecognitionFragment fragment = new RecognitionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private PhotoView photoView;
    private RecyclerView recycler;

    private RecognitionLabelAdapter adapter;

    private int type;
    private String imagePath;

    private RecogniationContract.Presenter presenter;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_recognition;
    }

    @Override
    protected int getStatusBarColor() {
        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        imagePath = getArguments().getString(PATH);
        type = getArguments().getInt(TYPE);
        presenter = new RecognitionPresenter(activity, this);

    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        photoView = $(R.id.photo_view);
        recycler = $(R.id.recycler);
        $(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        recycler.setLayoutManager(new GridLayoutManager(activity, 2));
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecognitionLabelAdapter(presenter.getLabels());

        recycler.setAdapter(adapter);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        ImageLoaderFactory.getLoader().displayQuality(photoView, imagePath);
        presenter.recognition(imagePath, type == REMOTE);
        LoadingDialogUtil.showDialog(activity);
    }

    @Override
    public void showLabels() {
        adapter.notifyDataSetChanged();
        LoadingDialogUtil.closeDialog();
    }
}
