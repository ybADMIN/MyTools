package com.simon.catkins.skin;

import android.content.res.Resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yulu02
 */
public abstract class Skin implements Map<String, Hook> {

    private final Map<String, Hook> mHooks = new HashMap<String, Hook>();

    public abstract String getPrefix();

    public abstract String getNamespace();

    public abstract Resources getResources();

    public abstract TypedValueParser getParser();

    @Override
    public void clear() {
        mHooks.clear();
    }

    @Override
    public boolean containsKey(Object o) {
        return mHooks.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return mHooks.containsValue(o);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Set<Entry<String, Hook>> entrySet() {
        return mHooks.entrySet();
    }

    @Override
    public Hook get(Object o) {
        return mHooks.get(o);
    }

    @Override
    public boolean isEmpty() {
        return mHooks.isEmpty();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Set<String> keySet() {
        return mHooks.keySet();
    }

    @Override
    public Hook put(String s, Hook hook) {
        return mHooks.put(s, hook);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Hook> map) {
        mHooks.putAll(map);
    }

    @Override
    public Hook remove(Object o) {
        return mHooks.remove(0);
    }

    @Override
    public int size() {
        return mHooks.size();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Collection<Hook> values() {
        return mHooks.values();
    }
}

