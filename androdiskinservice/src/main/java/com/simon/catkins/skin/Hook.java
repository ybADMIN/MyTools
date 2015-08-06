package com.simon.catkins.skin;

import android.util.TypedValue;
import android.view.View;

/**
 * @author yulu02
*/
public interface Hook {

    int hookType();

    String hookName();

    boolean shouldHook(View view, TypedValue value);

    Apply getApply();

    public interface Apply {
        void to(View view, TypedValue value);
    }
}
