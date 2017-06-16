package com.lenovo.album.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.contract.AlbumLabelEditContract;
import com.lenovo.album.event.LabelDataChangeEvent;
import com.lenovo.album.presenter.LabelAlbumEditPresenter;
import com.lenovo.album.ui.adapter.LabelItemProvider;
import com.lenovo.album.ui.adapter.LabelTitleProvider;
import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by noahkong on 17-6-12.
 */

public class LabelAlbumEditFragment extends BaseFragment implements AlbumLabelEditContract.View, View.OnClickListener {
    private final static String ID = "id";
    private final static String TYPE = "type";

    public final static int EDIT = 0;
    public final static int ADD = 1;

    public static LabelAlbumEditFragment newInstance(long id, int type) {

        Bundle args = new Bundle();
        args.putLong(ID, id);
        args.putInt(TYPE, type);
        LabelAlbumEditFragment fragment = new LabelAlbumEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView ivBack;
    private ImageView ivPlus;
    private ImageView ivDelete;
    private EditText etAlbumName;
    private TextView tvTitle;
    private RecyclerView recycler;
    private MultiTypeAdapter adapter;

    private AlbumLabelEditContract.Presenter presenter;

    private long labelAlbumId;
    private int type;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_album_label_edit;
    }

    @Override
    protected int getStatusBarColor() {
        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        presenter = new LabelAlbumEditPresenter(activity, this);
        labelAlbumId = getArguments().getLong(ID);
        type = getArguments().getInt(TYPE);
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        ivBack = $(R.id.iv_back);
        ivBack.setOnClickListener(this);

        ivPlus = $(R.id.iv_plus);
        ivPlus.setOnClickListener(this);

        ivDelete = $(R.id.iv_delete);
        ivDelete.setOnClickListener(this);
        ivDelete.setVisibility(type == EDIT ? View.VISIBLE : View.GONE);

        tvTitle = $(R.id.tv_title);
        tvTitle.setText(type == EDIT ? activity.getString(R.string.edit_album) : activity.getString(R.string.add_album));

        etAlbumName = $(R.id.et_ablum_name);

        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 10);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                Object obj = presenter.getData().get(position);
                int i = (obj instanceof String) ? 10 : 1;
                if (i == 1) {
                    LabelEntity entity = (LabelEntity) obj;
                    int w = FilletView.getViewWidth(activity, entity.alias == null ? entity.name : entity.alias);
                    return (w / (recycler.getWidth() / 10) + 1);
                }
                return i;
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

    }


    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        presenter.setLabelAlbum(labelAlbumId);
    }

    @Override
    public void editAlbumSuccess() {
        EventBus.getDefault().post(new LabelDataChangeEvent());
        hideSoftInput();
        pop();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
        etAlbumName.setText(presenter.getLabelAlbum().name == null ? "" : presenter.getLabelAlbum().name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;
            case R.id.iv_plus:
                presenter.getLabelAlbum().name = etAlbumName.getText().toString();
                presenter.saveAlbum();
                break;
            case R.id.iv_delete:
                presenter.deleteAlbum();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        presenter.destroy();
        super.onDestroyView();
    }
}
