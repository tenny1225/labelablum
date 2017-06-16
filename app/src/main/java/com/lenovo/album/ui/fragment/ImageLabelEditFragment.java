package com.lenovo.album.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.contract.ImageLabelEditContract;
import com.lenovo.album.presenter.ImageLabelEditPresenter;
import com.lenovo.album.ui.adapter.LabelItemProvider;
import com.lenovo.album.ui.adapter.LabelTitleProvider;
import com.lenovo.album.ui.widget.FilletView;
import com.lenovo.album.ui.widget.MagicDialog;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by noahkong on 17-6-14.
 */

public class ImageLabelEditFragment extends BaseFragment implements View.OnClickListener, ImageLabelEditContract.View {
    private final static String IMAGE_ID = "image-id";

    public static ImageLabelEditFragment newInstance(String uniqueString) {

        Bundle args = new Bundle();
        args.putString(IMAGE_ID, uniqueString);
        ImageLabelEditFragment fragment = new ImageLabelEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String imageId;

    private ImageView ivBack;
    private ImageView ivAdd;
    private ImageView ivSave;

    private RecyclerView recycler;
    private MultiTypeAdapter adapter;

    private ImageLabelEditContract.Presenter presenter;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_show_image_label_edit;
    }

    @Override
    protected int getStatusBarColor() {
        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        imageId = getArguments().getString(IMAGE_ID);
        presenter = new ImageLabelEditPresenter(activity, this);
    }

    @Override
    protected void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        presenter.setImageId(imageId);
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        ivBack = $(R.id.iv_back);
        ivBack.setOnClickListener(this);

        ivAdd = $(R.id.iv_add);
        ivAdd.setOnClickListener(this);

        ivSave = $(R.id.iv_save);
        ivSave.setOnClickListener(this);

        recycler = $(R.id.recycler);

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

        adapter = new MultiTypeAdapter(presenter.getData());

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
                showSelectDialog(item);
            }
        });
        adapter.register(LabelEntity.class, labelItemProvider);


        recycler.setAdapter(adapter);
    }

    private void showSelectDialog(final LabelEntity entity) {
        View view = View.inflate(activity, R.layout.dialog_label_select, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        final MagicDialog dialog = new MagicDialog.Builder(activity).setContent(view).build().show();

        view.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteLabel(entity);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showAddLabelDialog(entity);
            }
        });
    }

    private void showAddLabelDialog(final LabelEntity entity) {
        View view = View.inflate(activity, R.layout.dialog_add_label, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        final MagicDialog dialog = new MagicDialog.Builder(activity).setContent(view).build().show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideSoftInput();
            }
        });
        final EditText etLabelName = (EditText) view.findViewById(R.id.et_label_name);
        if (entity.name != null) {
            etLabelName.setText(entity.alias == null ? entity.name : entity.alias);
        }
        showSoftInput(etLabelName);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etLabelName.getText().toString().trim();
                if (name.equals("")) {
                    return;
                }
                hideSoftInput();

                if (entity.id != null && entity.id.longValue() != 0) {
                    entity.alias = name;
                } else {
                    entity.name = name;
                }

                presenter.addLabel(entity);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;
            case R.id.iv_add:
                showAddLabelDialog(new LabelEntity());
                break;
            case R.id.iv_save:
                presenter.saveLabels();
                break;
        }
    }


    @Override
    public void editLabelsSuccess() {
        pop();
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
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
}
