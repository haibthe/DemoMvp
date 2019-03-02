package com.hb.myapplication.utils.image.gt;

import android.content.Context;
import android.graphics.*;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

/**
 * Created by buihai on 10/12/17.
 */

public class GrayscaleTransformation extends BitmapTransformation {

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap.Config config =
                toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = pool.get(width, height, config);

        Canvas canvas = new Canvas(bitmap);
        ColorMatrix saturation = new ColorMatrix();
        saturation.setSaturation(0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(saturation));
        canvas.drawBitmap(toTransform, 0, 0, paint);

        return bitmap;
    }

    @Override
    public String key() {
        return "GrayscaleTransformation()";
    }
}