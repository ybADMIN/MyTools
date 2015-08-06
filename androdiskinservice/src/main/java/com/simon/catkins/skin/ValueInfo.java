package com.simon.catkins.skin;

import android.util.TypedValue;

/**
 * Use to bind a typed value to an applier
 */
public class ValueInfo {
    public final String skin;
    public final TypedValue typedValue;
    public final Hook.Apply apply;

    public ValueInfo(String skin, TypedValue typedValue, Hook.Apply apply) {
        this.skin = skin;
        this.typedValue = typedValue;
        this.apply = apply;
    }
}
