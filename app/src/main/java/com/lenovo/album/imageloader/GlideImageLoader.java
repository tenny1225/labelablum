package com.lenovo.album.imageloader;

import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-8.
 */

public class GlideImageLoader implements ImageLoaderContract {

    @Override
    public void display(ImageView iv, String url) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_default);

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(options)
                .thumbnail(0.5f)
                .into(iv);
    }

    @Override
    public void displayQuality(ImageView iv, String url) {

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .into(iv);
    }

    @Override
    public void display(ImageView iv, int id) {
        Glide.with(iv.getContext().getApplicationContext())
                .load(id)
                .transition(new DrawableTransitionOptions().crossFade())

                .into(iv);
    }

    @Override
    public void display(ImageView iv, int defaultId, String url) {

        RequestOptions options = new RequestOptions();
        options.placeholder(defaultId);

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(options)
                .into(iv);
    }

    @Override
    public void display(ImageView iv, int defaultId, int errorId, String url) {
        RequestOptions options = new RequestOptions();
        options.placeholder(defaultId)
                .error(errorId)
                .centerCrop();

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(iv);
    }
}
