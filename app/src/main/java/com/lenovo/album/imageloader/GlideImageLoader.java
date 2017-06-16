package com.lenovo.album.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lenovo.album.R;

/**
 * Created by noahkong on 17-6-8.
 */

public class GlideImageLoader implements ImageLoaderContract {

    @Override
    public void display(ImageView iv, String url) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_default);
        options.diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(options)
                .thumbnail(0.5f)

                .into(iv);


    }

    @Override
    public Bitmap getBitmap(Context context, String url) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(url, options);

        float s = 1;
        if (options.outWidth > options.outHeight) {
            s = options.outHeight * 1f / 512;
        } else {
            s = options.outWidth * 1f / 512;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) s;

        Bitmap bm = BitmapFactory.decodeFile(url, options);

        return Bitmap.createScaledBitmap(bm, 224, 224, false);
        // return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(url),224,224,false);

    }


    @Override
    public void clear(View v) {
        Glide.with(v.getContext()).clear(v);
    }

    @Override
    public void displayCircle(ImageView iv, String url) {
        RequestOptions options = new RequestOptions();
        options
                //.placeholder(R.mipmap.ic_default)
                .transform(new CircleCrop());
        options.diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(iv.getContext().getApplicationContext())
                .load(url)

                .transition(new DrawableTransitionOptions().crossFade())

                .apply(options)
                .thumbnail(0.5f)
                .into(iv);
    }

    @Override
    public void displayCircle(ImageView iv, int id) {
        RequestOptions options = new RequestOptions();
        options
                //.placeholder(R.mipmap.ic_default)
                .transform(new CircleCrop());
        options.diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(iv.getContext().getApplicationContext())
                .load(id)

                .transition(new DrawableTransitionOptions().crossFade())

                .apply(options)
                .thumbnail(0.5f)
                .into(iv);
    }

    @Override
    public void displayQuality(ImageView iv, String url) {
        RequestOptions options = new RequestOptions();
        options.transform(new FitCenter());
        // options.placeholder(R.mipmap.ic_default);//transform(new CenterCrop());
        options.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(iv.getContext().getApplicationContext())
                .load(url)
                .apply(options)
                //  .thumbnail(0.5f)
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

    @Override
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }
}
