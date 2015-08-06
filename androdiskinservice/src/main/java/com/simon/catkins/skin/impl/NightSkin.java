package com.simon.catkins.skin.impl;

import android.content.res.Resources;

/**
 * @author Simon Yu
 */
public class NightSkin extends BaseSkin {

    public static final String NAME = "night";

    @Override
    public String getPrefix() {
        return NAME;
    }

    @Override
    public String getNamespace() {
        return "http://schemas.android.com/android/skin/night";
    }

    @Override
    public Resources getResources() {
        return null;
    }
}
