package com.simon.catkins.skin.hooks;

import android.util.TypedValue;
import android.view.View;

import com.simon.catkins.skin.Hook;
import com.simon.catkins.skin.HookType;

/**
 * @author Simon Yu
 */
public class VisibilityHook implements Hook {

    private static final Apply APPLY = new Apply() {
        @Override
        public void to(View view, TypedValue value) {
            if ("visible".equals(value.string)) {
                view.setVisibility(View.VISIBLE);
            } else if ("invisible".equals(value.string)) {
                view.setVisibility(View.INVISIBLE);
            } else if ("gone".equals(value.string)) {
                view.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public int hookType() {
        return HookType.STRING;
    }

    @Override
    public String hookName() {
        return "visibility";
    }

    @Override
    public boolean shouldHook(View view, TypedValue value) {
        return true;
    }

    @Override
    public Apply getApply() {
        return APPLY;
    }
}
