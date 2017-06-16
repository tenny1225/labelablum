package com.lenovo.album.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-12.
 */

public class ImageEditPopWindow extends PopupWindow implements View.OnClickListener {
    public final static int EDIT = 0;
    public final static int SHARE = 1;
    public final static int DELETE = 2;
    public final static int RECOGNITION = 3;

    public interface ImageActionPopWindowListener {

        void onPopAction(int action);


    }

    private ImageActionPopWindowListener imageActionPopWindowListener;

    public void setImageActionPopWindowListener(ImageActionPopWindowListener imageActionPopWindowListener) {
        this.imageActionPopWindowListener = imageActionPopWindowListener;
    }

    public ImageEditPopWindow(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_image_edit, null);
        setWidth(-2);
        setHeight(-2);
        setContentView(view);
        setTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.findViewById(R.id.rl_recognition).setOnClickListener(this);
        view.findViewById(R.id.rl_share).setOnClickListener(this);
        view.findViewById(R.id.rl_delete).setOnClickListener(this);
        view.findViewById(R.id.rl_edit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_recognition:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onPopAction(RECOGNITION);
                }
                break;
            case R.id.rl_share:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onPopAction(SHARE);
                }
                break;
            case R.id.rl_delete:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onPopAction(DELETE);
                }
                break;
            case R.id.rl_edit:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onPopAction(EDIT);
                }
                break;
        }
        dismiss();

    }
}
