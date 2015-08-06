package com.simon.catkins.skin.impl;


import android.content.res.Resources;

/**
 * @author Simon Yu
 */
public class DefaultSkin extends BaseSkin {

    public static final String NAME = "default";

    @Override
    public String getPrefix() {
        return NAME;
    }

    @Override
    public String getNamespace() {
        return "http://schemas.android.com/android/skin/default";
    }

    @Override
    public Resources getResources() {
        return null;
    }
}
