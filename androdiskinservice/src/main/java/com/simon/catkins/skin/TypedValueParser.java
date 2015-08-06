package com.simon.catkins.skin;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author Simon Yu
 */
public interface TypedValueParser {
    TypedValue parseColor(String value);

    TypedValue parseBoolean(String value);

    TypedValue parseString(String value);

    TypedValue parseReference(String value, Resources res, String pkg);

    TypedValue parseDimension(String value, DisplayMetrics dm);

    TypedValue parseFloat(String value);

    TypedValue parseInt(String value);
}
