package com.lenovo.album.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.album.presenter.SearchLabelPresenter;
import com.lenovo.album.ui.adapter.SearchLabelAdapter;
import com.lenovo.album.ui.widget.LoadingDialogUtil;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noahkong on 17-6-12.
 */

public class SearchFragment extends BaseFragment implements SearchLabelContract.View, BaseRecyclerAdapter.ListItemClickListener<LabelEntity> {
    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText etContent;
    private ImageView ivSearch;
    private RecyclerView recycler;
    private SearchLabelAdapter adapter;

    private SearchLabelContract.Presenter presenter;

    private boolean startQuery;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_search;
    }

    @Override
    protected int getStatusBarColor() {
        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        presenter = new SearchLabelPresenter(activity, this);
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        etContent = $(R.id.et_content);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter.queryCandidate(s.toString());
            }
        });
        ivSearch = $(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                presenter.queryImages(etContent.getText().toString());
                LoadingDialogUtil.showDialog(activity);
            }
        });
        recycler = $(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new SearchLabelAdapter(presenter.getLabelEntities());
        adapter.setListItemClickListener(this);

        recycler.setAdapter(adapter);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        etContent.requestFocus();
        showSoftInput(etContent);
        presenter.queryCandidate("");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

    }

    @Override
    public void refresh() {

        adapter.notifyDataSetChanged();
    }

    @Override
    public void querySuccess() {
        LoadingDialogUtil.closeDialog();

        AlbumEntity entity = new AlbumEntity();

        entity.imageList = presenter.getImageEntities();



        BundleEntity<AlbumEntity> albumEntityBundleEntity = new BundleEntity<>(activity, entity);

        AlbumShowFragment fragment = AlbumShowFragment.newInstance(albumEntityBundleEntity, etContent.getText().toString());
        fragment.setAlbumEntity(entity);
        start(fragment);
    }

    @Override
    public void onItemClick(LabelEntity labelEntity) {
        hideSoftInput();
        String name = labelEntity.alias == null ? labelEntity.name : labelEntity.alias;
        String text = etContent.getText().toString();
        if (!text.endsWith(" ")) {
            String[] array = text.split(" ");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length - 1; i++) {
                String txt = array[i];
                if (!txt.trim().equals("")) {
                    builder.append(txt.trim());
                    builder.append(" ");
                }

            }
            String string = builder.toString();
            etContent.setText(string);
            etContent.setSelection(string.length());

        }
        etContent.getText().append(name + " ");

    }

    @Override
    public void onItemLongClick(LabelEntity labelEntity) {

    }
}
