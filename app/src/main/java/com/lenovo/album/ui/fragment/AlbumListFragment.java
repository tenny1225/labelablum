package com.lenovo.album.ui.fragment;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseFragment;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.album.contract.FolderAlbumScannerContract;
import com.lenovo.album.contract.LabelAlbumScannerContract;
import com.lenovo.album.event.LabelDataChangeEvent;
import com.lenovo.album.presenter.FolderAlbumScannerPresenter;
import com.lenovo.album.presenter.LabelAlbumScannerPresenter;
import com.lenovo.album.ui.adapter.FolderAlbumShowAdapter;
import com.lenovo.album.ui.adapter.LabelAlbumShowAdapter;

import com.lenovo.album.ui.fragment.helper.LabelItemTouchHelperCallback;
import com.lenovo.album.ui.widget.AlbumSwitchPopWindow;
import com.lenovo.common.entity.AlbumEntity;
import com.lenovo.common.entity.BundleEntity;
import com.lenovo.common.entity.FolderAlbumEntity;
import com.lenovo.common.entity.ImageEntity;
import com.lenovo.common.entity.LabelAlbumEntity;
import com.lenovo.common.entity.LabelEntity;
import com.lenovo.common.util.CommonUtil;
import com.lenovo.common.util.SpUtil;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.TransitionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by noahkong on 17-6-7.
 */

public class AlbumListFragment extends BaseFragment implements FolderAlbumScannerContract.View,
        LabelAlbumScannerContract.View, AlbumSwitchPopWindow.SwitchAlbumListener {
    public final static int FOLDER_ALBUM = SpUtil.NORMAL_ALBUM;
    public final static int LABEL_ALBUM = SpUtil.LABEL_ALBUM;

    private final static int REQUEST_CAMERA_2 = 2000;

    public static AlbumListFragment newInstance() {

        AlbumListFragment fragment = new AlbumListFragment();
        return fragment;
    }

    private ImageView ivSearch;
    private ImageView ivSelect;
    private TextView tvAlbumTypeName;

    private RecyclerView recycler;

    private FolderAlbumShowAdapter folderAlbumShowAdapter;

    private LabelAlbumShowAdapter labelAlbumShowAdapter;

    private FolderAlbumScannerContract.Presenter folderPresenter;

    private LabelAlbumScannerContract.Presenter labelPresenter;

    private int currentShow;

    private AlbumSwitchPopWindow popWindow;

    private LabelItemTouchHelperCallback touchCallback;


    private String cameraFilePath;

    @Override
    protected int getViewResource() {
        return R.layout.fragment_album_list;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        EventBus.getDefault().register(this);

        folderPresenter = new FolderAlbumScannerPresenter(activity, this);
        folderPresenter.start();

        labelPresenter = new LabelAlbumScannerPresenter(activity, this);
        labelPresenter.start();

        currentShow = SpUtil.getCurrentAlbumType(activity);
    }

    @Override
    protected void initWidget(final View rootView) {
        super.initWidget(rootView);

        ivSearch = (ImageView) rootView.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(SearchFragment.newInstance());
            }
        });

        ivSelect = (ImageView) rootView.findViewById(R.id.iv_select);

        $(R.id.iv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraFilePath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png";
                File f = new File(cameraFilePath);

                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                Uri photoURI;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    photoURI = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", f);
                } else {
                    photoURI = Uri.fromFile(f); // 传递路径
                }


                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);// 更改系统默认存储路径
                startActivityForResult(intent, REQUEST_CAMERA_2);
            }
        });

        tvAlbumTypeName = (TextView) rootView.findViewById(R.id.tv_album_type_name);
        tvAlbumTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popWindow = new AlbumSwitchPopWindow(activity);
                popWindow.setSwitchAlbumListener(AlbumListFragment.this);
                popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rotation();
                    }
                });
                popWindow.showAtLocation(rootView, Gravity.TOP, 0, (int) (recycler.getTop() + CommonUtil.getStatusBarHeight(activity)));
                rotation();

            }
        });


        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recycler.setLayoutManager(new GridLayoutManager(activity, 2));

        folderAlbumShowAdapter = new FolderAlbumShowAdapter(folderPresenter.getFolderAlbumEntityList());

        labelAlbumShowAdapter = new LabelAlbumShowAdapter(labelPresenter.getLabelAlbumEntityList());
        labelAlbumShowAdapter.setEditLabelAlbumListener(new LabelAlbumShowAdapter.EditLabelAlbumListener() {
            @Override
            public void onEdit(LabelAlbumEntity obj) {
                start(LabelAlbumEditFragment.newInstance(obj.id, LabelAlbumEditFragment.EDIT));
            }
        });
        labelAlbumShowAdapter.setListItemHolderClickListener(new BaseRecyclerAdapter.ListItemHolderClickListener<LabelAlbumEntity>() {
            @Override
            public void onItemHolderClick(BaseRecyclerAdapter.VH holder, LabelAlbumEntity labelAlbumEntity) {

                if (labelAlbumEntity == null) {

                    start(LabelAlbumEditFragment.newInstance(0, LabelAlbumEditFragment.ADD));
                    return;
                }
                if (labelAlbumShowAdapter.isEditState()) {

                    start(LabelAlbumEditFragment.newInstance(labelAlbumEntity.id, LabelAlbumEditFragment.EDIT));
                    return;
                }
                AlbumEntity entity = new AlbumEntity();
                entity.imageList = labelAlbumEntity.imageList;

                AlbumShowFragment fragment = AlbumShowFragment.newInstance(new BundleEntity<AlbumEntity>(activity, entity), labelAlbumEntity.alias == null ? labelAlbumEntity.name : labelAlbumEntity.alias);
                fragment.setAlbumEntity(entity);
                start(fragment);
            }

            @Override
            public void onItemHolderLongClick(BaseRecyclerAdapter.VH holder, LabelAlbumEntity entity) {
                labelAlbumShowAdapter.setEditState(true);
            }
        });


        folderAlbumShowAdapter.setListItemClickListener(new BaseRecyclerAdapter.ListItemClickListener<FolderAlbumEntity>() {
            @Override
            public void onItemClick(FolderAlbumEntity folderAlbumEntity) {
                AlbumEntity entity = new AlbumEntity();
                entity.imageList = folderAlbumEntity.imageList;
               /* for (ImageEntity entity1 : entity.imageList) {
                    if (entity1.getLabelEntityList() != null) {
                        entity1.getLabelEntityList().clear();
                    }
                }
*/
                AlbumShowFragment fragment = AlbumShowFragment.newInstance(new BundleEntity<>(activity, entity), folderAlbumEntity.name);
                fragment.setAlbumEntity(entity);
                start(fragment);
            }

            @Override
            public void onItemLongClick(FolderAlbumEntity folderAlbumEntity) {

            }
        });


        touchCallback = new LabelItemTouchHelperCallback(new LabelItemTouchHelperCallback.SwitchItemListener() {
            @Override
            public void onItemMove(int position1, int position2) {
                List<LabelAlbumEntity> list = labelPresenter.getLabelAlbumEntityList();
                LabelAlbumEntity obj = list.get(position1);

                list.remove(obj);
                list.add(position2, obj);

                labelAlbumShowAdapter.notifyItemMoved(position1, position2);
                labelPresenter.updateLabelAlbum();
            }
        });

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(touchCallback);
        mItemTouchHelper.attachToRecyclerView(recycler);

        labelAlbumShowAdapter.setItemTouchHelper(mItemTouchHelper);
        initAlbum();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据
            if (requestCode == REQUEST_CAMERA_2) {


                try {
                    // MediaStore.Images.Media.insertImage(activity.getContentResolver(), cameraFilePath, "", "");
                    MediaScannerConnection.scanFile(activity, new String[]{cameraFilePath}, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + cameraFilePath)));
                start(RecognitionFragment.newInstance(cameraFilePath, RecognitionFragment.CAMERA));
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if (labelAlbumShowAdapter.isEditState()) {
            labelAlbumShowAdapter.setEditState(false);

            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        labelAlbumShowAdapter.setEditState(false);
    }

    private void initAlbum() {
        if (currentShow == FOLDER_ALBUM) {
            tvAlbumTypeName.setText(R.string.normal_album);
            recycler.setAdapter(folderAlbumShowAdapter);
            touchCallback.setEnable(false);
            folderPresenter.searchAllAlbum();


            ivSearch.setVisibility(View.GONE);

        } else {
            tvAlbumTypeName.setText(R.string.label_album);
            recycler.setAdapter(labelAlbumShowAdapter);
            touchCallback.setEnable(true);
            labelPresenter.searchAllAlbum();


            ivSearch.setVisibility(View.VISIBLE);


        }
    }

    @Override
    protected int getStatusBarColor() {

        return CommonUtil.getAttrColor(activity, R.attr.colorPrimary);
    }

    private boolean isRotated;

    private void rotation() {
        TransitionManager.beginDelayedTransition((ViewGroup) rootView, new Rotate());
        ivSelect.setRotation(isRotated ? 0 : 180);
        isRotated = !isRotated;
    }

    @Override
    public void onSwitch(int type) {
        currentShow = type;
        initAlbum();
    }


    @Override
    public void refreshListView() {
        if (currentShow == FOLDER_ALBUM) {
            folderAlbumShowAdapter.notifyDataSetChanged();
        } else {
            labelAlbumShowAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void labelDataChange(LabelDataChangeEvent event) {
        if (currentShow == LABEL_ALBUM) {

            labelPresenter.searchAllAlbum();
        }

    }


}
