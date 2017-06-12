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
import com.lenovo.album.contract.SearchLabelContract;
import com.lenovo.album.presenter.SearchLabelPresenter;
import com.lenovo.album.ui.adapter.SearchLabelAdapter;
import com.lenovo.common.util.CommonUtil;

/**
 * Created by noahkong on 17-6-12.
 */

public class SearchFragment extends BaseFragment implements SearchLabelContract.View {
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
                presenter.query(s.toString());
            }
        });
        ivSearch = $(R.id.iv_search);
        recycler = $(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new SearchLabelAdapter(presenter.getLabelEntities());

        recycler.setAdapter(adapter);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        etContent.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }
}
