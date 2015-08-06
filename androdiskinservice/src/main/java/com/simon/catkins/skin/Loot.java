package com.simon.catkins.skin;

import android.util.Log;

/**
 * An internal logger
 * <p/>
 *
 * @author Simon Yu
 */
class Loot {
    static final boolean DEBUG = true;

    static void logApply(String msg) {
        Log.d("skin.apply", msg);
    }

    static void logInflate(String msg) {
        Log.d("skin.inflate", msg);

    }

    static void logParse(String msg) {
        Log.d("skin.parse", msg);
    }
}
