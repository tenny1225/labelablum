package com.lenovo.album.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-12.
 */

public class ImageEditPopWindow extends PopupWindow implements View.OnClickListener {
    public interface ImageActionPopWindowListener {
        void onShare();

        void onRecognition();

        void onDelete();

        void onEdit();
    }

    private ImageActionPopWindowListener imageActionPopWindowListener;

    public void setImageActionPopWindowListener(ImageActionPopWindowListener imageActionPopWindowListener) {
        this.imageActionPopWindowListener = imageActionPopWindowListener;
    }

    public ImageEditPopWindow(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_image_edit, null);
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
                    imageActionPopWindowListener.onRecognition();
                }
                break;
            case R.id.rl_share:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onShare();
                }
                break;
            case R.id.rl_delete:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onDelete();
                }
                break;
            case R.id.rl_edit:
                if (imageActionPopWindowListener != null) {
                    imageActionPopWindowListener.onEdit();
                }
                break;
        }
        dismiss();

    }
}
