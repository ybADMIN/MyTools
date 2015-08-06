package com.simon.catkins.skin.external;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.simon.catkins.skin.Skin;
import com.simon.catkins.skin.TypedValueParser;
import com.simon.catkins.skin.TypedValueParserImpl;

/**
 * @author Simon Yu
 */
public final class ExternalSkin extends Skin {
    public static final String NAME = "external";

    private TypedValueParser mParser = new Parser();

    @Override
    public String getPrefix() {
        return NAME;
    }

    @Override
    public String getNamespace() {
        return "http://schemas.android.com/android/skin/external";
    }

    @Override
    public Resources getResources() {
        return ExtResources.getInstance().getResources();
    }

    @Override
    public TypedValueParser getParser() {
        return mParser;
    }

    public ExternalSkin(String path, DisplayMetrics dm, Configuration config) {
        setExternalPath(path, dm, config);
        put("background", new BackgroundHook());
    }

    public void setExternalPath(String path, DisplayMetrics dm, Configuration config) {
        ExtResources.getInstance().setExternalResources(path, dm, config);
    }

    public void updateConfiguration(DisplayMetrics dm, Configuration config) {
        ExtResources.getInstance().updateConfiguration(dm, config);
    }

    private class Parser extends TypedValueParserImpl {
        @Override
        public TypedValue parseReference(String ref, Resources res, String pkg) {
            return super.parseReference(ref, res, pkg);
        }
    }
}
