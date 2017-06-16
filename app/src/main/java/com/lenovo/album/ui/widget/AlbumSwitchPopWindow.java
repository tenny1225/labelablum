package com.lenovo.album.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.lenovo.album.R;
import com.lenovo.common.util.SpUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by noahkong on 17-6-13.
 */

public class AlbumSwitchPopWindow extends PopupWindow implements View.OnClickListener {
    public interface SwitchAlbumListener{
        void onSwitch(int type);
    }
    private AutoRelativeLayout llNormalAlbum;
    private AutoRelativeLayout llLabelAlbum;

    private ImageView ivNormalAlbum;
    private ImageView ivLabelAlbum;

    private SwitchAlbumListener switchAlbumListener;

    public void setSwitchAlbumListener(SwitchAlbumListener switchAlbumListener) {
        this.switchAlbumListener = switchAlbumListener;
    }

    public AlbumSwitchPopWindow(Context context) {
        super( null, ViewGroup.LayoutParams.MATCH_PARENT, -2, false);

        View view = LayoutInflater.from(context).inflate(R.layout.pop_switch_album, null);

        setContentView(view);
        setTouchable(true);
        setFocusable(true);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        llNormalAlbum = (AutoRelativeLayout) view.findViewById(R.id.ll_normal_album);
        llNormalAlbum.setOnClickListener(this);
        llLabelAlbum = (AutoRelativeLayout) view.findViewById(R.id.ll_label_album);
        llLabelAlbum.setOnClickListener(this);

        ivNormalAlbum = (ImageView) view.findViewById(R.id.iv_normal_album);
        ivLabelAlbum = (ImageView) view.findViewById(R.id.iv_label_album);

        int type = SpUtil.getCurrentAlbumType(context);
        if(SpUtil.LABEL_ALBUM==type){
            ivNormalAlbum.setVisibility(View.INVISIBLE);
            ivLabelAlbum.setVisibility(View.VISIBLE);
        }else{
            ivNormalAlbum.setVisibility(View.VISIBLE);
            ivLabelAlbum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.ll_normal_album:
                SpUtil.setCurrentAlbumType(v.getContext(),SpUtil.NORMAL_ALBUM);
                if(switchAlbumListener!=null){
                    switchAlbumListener.onSwitch(SpUtil.NORMAL_ALBUM);
                }
                break;
            case R.id.ll_label_album:
                SpUtil.setCurrentAlbumType(v.getContext(),SpUtil.LABEL_ALBUM);
                if(switchAlbumListener!=null){
                    switchAlbumListener.onSwitch(SpUtil.LABEL_ALBUM);
                }
                break;
        }
        dismiss();
    }
}
