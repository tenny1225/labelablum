package com.lenovo.album.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovo.album.R;
import com.lenovo.album.base.BaseRecyclerAdapter;
import com.lenovo.common.entity.LabelEntity;

import java.util.List;

/**
 * Created by noahkong on 17-6-15.
 */

public class RecognitionLabelAdapter extends BaseRecyclerAdapter<LabelEntity> {
    public RecognitionLabelAdapter(List<LabelEntity> itemList) {
        super(itemList);
    }

    @Override
    protected View getItemView(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_recognition_label, parent, false);
        return v;
    }

    @Override
    protected void onBind(VH holder, LabelEntity obj) {
        holder.<TextView>$(R.id.tv_label_name).setText(obj.name);
    }
}
