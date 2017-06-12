package com.lenovo.album.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.contract.LabelEditContract;
import com.lenovo.album.presenter.LabelEditPresenter;
import com.lenovo.album.ui.adapter.LabelItemProvider;
import com.lenovo.album.ui.fragment.helper.LabelItemTouchHelperCallback;
import com.lenovo.album.ui.adapter.LabelTitleProvider;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelEditFragment extends BaseFragment implements LabelEditContract.View {
    public static LabelEditFragment newInstance() {

        Bundle args = new Bundle();

        LabelEditFragment fragment = new LabelEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView ivBack;
    private RecyclerView recycler;
    private MultiTypeAdapter adapter;

    private LabelEditContract.Presenter presenter;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_label_edit;
    }

    @Override
    protected int getStatusBarColor() {
        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        presenter = new LabelEditPresenter(activity, this);
        presenter.start();
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        ivBack = (ImageView) rootView.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });

        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (presenter.getData().get(position) instanceof String) ? 4 : 1;
            }
        });
        recycler.setLayoutManager(layoutManager);

        adapter = new MultiTypeAdapter();

        adapter.register(String.class, new LabelTitleProvider());
        LabelItemProvider labelItemProvider = new LabelItemProvider();
        labelItemProvider.setItemActionListener(new LabelItemProvider.ItemActionListener() {
            @Override
            public void onItemClick(LabelEntity item, View v) {
                if (item.selected) {
                    swap(presenter.getData().indexOf(item), presenter.getData().size() - 1);
                } else {
                    int n = 0;
                    List<Object> list = presenter.getData();
                    for (int i = 0; i < list.size(); i++) {
                        Object o = list.get(i);
                        if (o instanceof String) {
                            n++;
                            if (n == 2) {
                                int m = list.indexOf(item);
                                swap(m, i);
                                return;
                            }
                        }
                    }

                }
            }

            @Override
            public void onLongClick(LabelEntity item, View v) {

            }
        });
        adapter.register(LabelEntity.class, labelItemProvider);

        adapter.setItems(presenter.getData());

        ItemTouchHelper.Callback callback = new LabelItemTouchHelperCallback(new LabelItemTouchHelperCallback.SwitchItemListener() {
            @Override
            public void onItemMove(int position1, int position2) {
                swap(position1, position2);
            }
        });
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recycler);
        recycler.setAdapter(adapter);

    }

    private void swap(int position1, int position2) {


        List<Object> list = presenter.getData();
        Object obj = list.get(position1);
        if (obj instanceof String) {
            return;
        }
        list.remove(obj);
        list.add(position2, obj);
        int n = 0;
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (o instanceof String) {
                n++;
                if (n == 2) {
                    if (position2 > i) {
                        ((LabelEntity) obj).selected = false;
                    } else {
                        ((LabelEntity) obj).selected = true;
                    }
                    break;
                }
            }
        }

        adapter.notifyItemMoved(position1, position2);
        presenter.updateLabels();
    }


    @Override
    protected void initSync() {
        super.initSync();
        presenter.getAllLabels();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
    }
}
