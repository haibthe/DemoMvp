package com.hb.myapplication.utils.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;


public interface ImageHelper {

    void loadImageFromUrl(ImageView view, Object path);

    void loadImageFromUrl(ImageView view, Object url, boolean isFitCenter);

    void loadImageGrayScaleFromUrl(ImageView view, Object url);

    void loadThumbnail(ImageView view, Object path, int resId);

//    void loadAvatar(ImageView view, Object url);

    void loadBanner(ImageView view, Object url);

    void loadImageDetail(ImageView view, Object url,
                         boolean isFitCenter,
                         float sizeMultiplier,
                         OnLoadSuccessListener successListener,
                         OnLoadFailedListener failedListener);


    interface OnLoadFailedListener {
        void onFailed(Exception e);
    }

    interface OnLoadSuccessListener {
        void onSuccess(Drawable drawable);
    }
}
