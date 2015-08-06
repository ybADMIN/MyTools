package com.simon.catkins.skin;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.simon.catkins.skin.impl.DefaultSkin;

import java.util.List;
import java.util.Stack;

/**
 * @author yulu02
 */
public class SkinService {

    public static final String SP_FILE_SKIN_SERVICE = "skin_service";
    private static SkinInflatorFactory sSkinInflatorFactory;

    public synchronized static SkinInflatorFactory getInflatorFactory(Context context) {
        if (sSkinInflatorFactory == null) {
            sSkinInflatorFactory = new SkinInflatorFactory(context);
        }
        return sSkinInflatorFactory;
    }

    private static String mSkinName;

    public static String getSkin() {
        return mSkinName;
    }

    public static void addSkin(Skin skin) {
        SkinFactory.getFactory().register(skin);
    }

    public static void applySkin(Activity activity) {
        mSkinName = activity.getSharedPreferences(SP_FILE_SKIN_SERVICE, Context.MODE_PRIVATE)
                .getString("skin", DefaultSkin.NAME);
        Loot.logApply("Applying skin [" + mSkinName + "] to activity " + activity.getClass().getSimpleName());
        applyViews(activity.findViewById(android.R.id.content));
    }

    public static void applySkin(Activity activity, String skinName) {
        if (SkinFactory.getFactory().get(skinName) == null) return;
        mSkinName = skinName;
        activity.getSharedPreferences(SP_FILE_SKIN_SERVICE, Context.MODE_PRIVATE).edit()
                .putString("skinName", mSkinName).commit();
        Loot.logApply("Applying skin [" + mSkinName + "] to activity " + activity.getClass().getSimpleName());
        applyViews(activity.findViewById(android.R.id.content));
    }

    private static void applyViews(View root) {
        if (mSkinName == null) return;

        Loot.logApply("Loop the view tree: " + root);
        Stack<View> stack = new Stack<View>();
        stack.push(root);

        while (!stack.isEmpty()) {
            View v = stack.pop();

            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                final int count = vg.getChildCount();
                for (int i = 0; i < count; i++) {
                    stack.push(vg.getChildAt(i));
                }
            } else {
                @SuppressWarnings("unchecked")
                List<ValueInfo> list = (List<ValueInfo>) ViewTagger.getTag(v, R.id.skin_hooker);

                if (list == null) {
                    continue;
                }

                Loot.logApply("Apply skin [" + mSkinName + "] to view id: " + Integer.toHexString(v.getId()));
                for (ValueInfo info : list) {
                    if (mSkinName.equals(info.skin)) {
                        info.apply.to(v, info.typedValue);
                    }
                }
            }
        }
    }
}
