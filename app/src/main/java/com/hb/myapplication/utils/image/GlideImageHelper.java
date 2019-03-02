package com.hb.myapplication.utils.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hb.myapplication.R;
import com.hb.myapplication.utils.image.gt.GrayscaleTransformation;


public class GlideImageHelper implements ImageHelper {

    private RequestOptions centerCrop;
    private RequestOptions fitCenter;
//    private RequestOptions avatar;


    private Context mContext;

    public GlideImageHelper(Context context) {
        mContext = context;

        fitCenter = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .priority(Priority.HIGH);

        centerCrop = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .priority(Priority.HIGH);
//
//
//        avatar = new RequestOptions()
//                .fitCenter()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .placeholder(R.drawable.ic_avatar_customer)
//                .error(R.drawable.ic_avatar_customer)
//                .priority(Priority.HIGH);

    }

    private Drawable getProgressBarIndeterminate() {
        final int[] attrs = {android.R.attr.indeterminateDrawable};
        final int attrs_indeterminateDrawable_index = 0;
        TypedArray a = mContext.obtainStyledAttributes(android.R.style.Widget_ProgressBar, attrs);
        try {
            Drawable drawable = a.getDrawable(attrs_indeterminateDrawable_index);
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).stop();
                ((Animatable) drawable).start();
            }
            return drawable;
        } finally {
            a.recycle();
        }
    }

    @Override
    public void loadImageGrayScaleFromUrl(ImageView view, Object url) {
        Glide.with(view)
                .load(url)
                .apply(fitCenter)
                .apply(new RequestOptions().transform(new GrayscaleTransformation()))
                .into(view);
    }

//    @Override
//    public void loadAvatar(ImageView view, Object url) {
//        Glide.with(view)
//                .load(url)
//                .apply(avatar)
//                .into(view);
//    }

    @SuppressLint("CheckResult")
    @Override
    public void loadThumbnail(ImageView view, Object path, int resId) {
        if (resId != -1) {
            fitCenter.error(resId)
                    .placeholder(resId);
        }
        loadImageDetail(view, path, true, 0.5f, null, null);
    }

    @Override
    public void loadImageFromUrl(ImageView view, Object path) {
        loadImageDetail(view, path, true, 1.0f, null, null);
    }

    @Override
    public void loadImageFromUrl(ImageView view, Object url, boolean isFitCenter) {
        loadImageDetail(view, url, isFitCenter, 1.0f, null, null);
    }

    @Override
    public void loadBanner(ImageView view, Object url) {
        loadImageDetail(view, url, false, 1.0f, null, null);
    }

    @Override
    public void loadImageDetail(ImageView view, Object url, boolean isFitCenter, float sizeMultiplier,
                                OnLoadSuccessListener successListener, OnLoadFailedListener failedListener) {
        Context context = view.getContext();
        Glide.with(context)
                .load(url)
                .apply(isFitCenter ? fitCenter : centerCrop)
                .thumbnail(sizeMultiplier)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (failedListener != null) {
                            failedListener.onFailed(e);
                        }
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        fitCenter.placeholder(R.drawable.no_image)
                                .error(R.drawable.no_image);

                        if (successListener != null) {
                            successListener.onSuccess(resource);
                        }
                        return false;
                    }
                })
                .into(view);
    }


}
