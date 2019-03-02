package com.hb.lib;

import android.content.Context;
import android.util.TypedValue;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class UtilsColor {

    @ColorInt
    public static int getThemeColor(
            @NonNull final Context context,
            @AttrRes final int attributeColor
    ) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, value, true);
        return value.data;
    }
}
