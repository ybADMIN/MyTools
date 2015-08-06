package com.simon.catkins.skin;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InflatorFactory to bind hooks to views.
 *
 * @author yulu02
 */
public class SkinInflatorFactory implements LayoutInflater.Factory {
    private static final String TAG = "SkinInflatorFactory";

    private static final String LOAD_PREFIX = "android.widget.";

    private final Map<String, Constructor<? extends View>> mConstructors;

    private final DisplayMetrics mDisplayMetrics;
    private final Resources mRes;
    private final String mPackageName;

    private final TypedValueParser mParser = new TypedValueParserImpl();

    SkinInflatorFactory(Context context) {
        mConstructors = new HashMap<String, Constructor<? extends View>>();
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        mRes = context.getResources();
        mPackageName = context.getPackageName();
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        long now;
        if (Loot.DEBUG) {
            now = SystemClock.uptimeMillis();
        }
        View view;
        try {
            Constructor<? extends View> constructor;
            Class<? extends View> c;
            constructor = mConstructors.get(name);
            if (constructor == null) {
                ClassLoader classLoader = context.getClassLoader();
                if (classLoader != null) {
                    c = classLoader.loadClass(name.contains(".") ? name : LOAD_PREFIX + name).asSubclass(View.class);
                    constructor = c.getConstructor(Context.class, AttributeSet.class);
                    mConstructors.put(name, constructor);
                    view = constructor.newInstance(context, attrs);
                } else {
                    return null;
                }
            } else {
                view = constructor.newInstance(context, attrs);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        if (Loot.DEBUG) {
            Log.d(TAG, "view name = " + name);
            final int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                Log.v(TAG, "attribute " + i + " : " + attrs.getAttributeName(i) + ":" + attrs.getAttributeValue(i));
            }
        }

        // if skipped just return the view
        if (attrs.getAttributeBooleanValue(Attrs.NAMESPACE, Attrs.ATTR_SKIP, false)) return view;

        // if no skin register just return the view
        if (SkinFactory.getFactory().all().size() == 0) return view;

        final List<ValueInfo> infos = new ArrayList<ValueInfo>();

        // if force to a skin type, we only bind hooks of this skin and apply skin immediately to the view.
        final String forceSkin = attrs.getAttributeValue(Attrs.NAMESPACE, Attrs.ATTR_FORCE_SKIN);
        final boolean isForceSkin = forceSkin != null;

        for (Skin skin : SkinFactory.getFactory().all()) {
            if (isForceSkin && !skin.getPrefix().equals(forceSkin)) continue;

            for (Hook hook : skin.values()) {
                Log.d("Skin>Name",skin.getPrefix()+"--"+view.getClass().getSimpleName());
                doHook(attrs, view, infos, isForceSkin, skin, hook);
        }
        }

        if (infos.size() > 0) {
            ViewTagger.setTag(view, R.id.skin_hooker, infos);
        }

        if (Loot.DEBUG) {
            now = SystemClock.uptimeMillis() - now;
            Log.d(TAG, "inflate view time = " + now);

        }

        if (Loot.DEBUG) {
            Loot.logInflate("Inflated a view: " + name + " using SkinInflatorFactory");
        }
        return view;
    }

    private void doHook(AttributeSet attrs,
                        View view,
                        List<ValueInfo> infos,
                        boolean forceSkin,
                        Skin skin,
                        Hook hook) {
        final String value = attrs.getAttributeValue(skin.getNamespace(), hook.hookName());
        if (value == null) {
            return;
        }
        final TypedValueParser parser = skin.getParser() == null ? mParser : skin.getParser();
        TypedValue tv = null;
        final int hookType = hook.hookType();
        if ((hookType & HookType.REFERENCE_ID) == HookType.REFERENCE_ID) {
            if (skin.getResources() == null) {
                tv = parser.parseReference(value, mRes, mPackageName);
            } else {
                tv = parser.parseReference(value, skin.getResources(), "com.simon.catkins.skin.sample.externalskin");
            }
        }
        if (tv == null) {
            if ((hookType & HookType.COLOR) == HookType.COLOR) {
                tv = parser.parseColor(value);
            } else if ((hookType & HookType.STRING) == HookType.STRING) {
                tv = parser.parseString(value);
            } else if ((hookType & HookType.FLOAT) == HookType.FLOAT) {
                tv = parser.parseFloat(value);
            } else if ((hookType & HookType.INTEGER) == HookType.INTEGER) {
                tv = parser.parseInt(value);
            } else if ((hookType & HookType.BOOLEAN) == HookType.BOOLEAN) {
                tv = parser.parseInt(value);
            } else if ((hookType & HookType.DIMENSION) == HookType.DIMENSION) {
                tv = parser.parseDimension(value, mDisplayMetrics);
            }
        }
        if (tv == null) return;

        if (hook.shouldHook(view, tv)) {
            infos.add(new ValueInfo(skin.getPrefix(), tv, hook.getApply()));
            if (forceSkin) hook.getApply().to(view, tv);
        }
    }

}
