package com.simon.catkins.skin.external;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This resources class can load external zipped or not zipped(directory) resources.
 *
 * Call {@link #setExternalResources} before you try to load any resources,
 * otherwise you will get nothing.
 *
 * @author Simon Yu
 *
 * @see AssetManager
 * @see android.content.pm.PackageManager
 * @see Resources
 */
class ExtResources {
    private static final String TAG = "ExtResources";

    private static ExtResources mExtResources;

    private AssetManager mAssetManager;
    private Resources mRes;

    private ExtResources() {
    }

    synchronized static ExtResources getInstance() {
        if (mExtResources == null)  {
            mExtResources = new ExtResources();
        }
        return mExtResources;
    }


    /**
     * Use reflection to create a default AssetManager
     * @return the system asset manager
     */
    private static AssetManager getSystemAssetManager() {
        Class<AssetManager> clazz = AssetManager.class;
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Use reflection to add resources path to asset manager.
     *
     * @param file the zip file or directory
     */
    private int addAssetPath(String file) {
        Class<AssetManager> clazz = AssetManager.class;
        try {
            Method m = clazz.getDeclaredMethod("addAssetPath", String.class);
            return (Integer) m.invoke(mAssetManager, file);
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "NoSuchMethodException");
        } catch (InvocationTargetException e) {
            Log.d(TAG, "InvocationTargetException");
        } catch (IllegalAccessException e) {
            Log.d(TAG, "IllegalAccessException");
        }
        return 0;
    }

    /**
     * the resource class will contain all resources in the target zipped file.
     *
     * @return the resources
     */
    Resources getResources() {
        return mRes;
    }

    void setExternalResources(String file, DisplayMetrics dm, Configuration config) {
        mAssetManager = getSystemAssetManager();
        int cookie = addAssetPath(file);
        if (cookie == 0) {
            Log.w(TAG, "external resources not found");
        }
        mRes = new Resources(mAssetManager, dm, config);
    }

    void updateConfiguration(DisplayMetrics dm, Configuration config) {
        mRes.updateConfiguration(config, dm);
    }
}
