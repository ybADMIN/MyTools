package com.simon.catkins.skin.hooks;

import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import com.simon.catkins.skin.Hook;


/**
* @author Simon Yu
*/
public class BackgroundHook implements Hook {

    private static final Apply APPLY = new Apply() {
        @Override
        public void to(View view, TypedValue value) {
            switch (value.type) {
                case TypedValue.TYPE_REFERENCE:
                    Resources res = view.getResources();
                    if (res != null) {
                        String[] tyep = value.string.toString().split("/");
                        if (tyep[0].equals("color")){
                            view.setBackgroundColor(res.getColor(value.resourceId));
                        }
                        if (tyep[0].equals("mipmap") || tyep[0].equals("drawable")){
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                view.setBackground(res.getDrawable(value.resourceId, null));
                            }else if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
                                view.setBackground(res.getDrawable(value.resourceId));
                            }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
                                view.setBackgroundDrawable(res.getDrawable(value.resourceId));
                            }
                        }
                    }
                    break;
                case TypedValue.TYPE_INT_COLOR_ARGB4:
                case TypedValue.TYPE_INT_COLOR_RGB4:
                case TypedValue.TYPE_INT_COLOR_ARGB8:
                case TypedValue.TYPE_INT_COLOR_RGB8:
                    view.setBackgroundColor(value.data);
                    break;
            }
        }
    };

    @Override
    public int hookType() {
        // computed from HookType.REFERENCE_ID | HookType.COLOR
        return 6;
    }

    @Override
    public String hookName() {
        return "background";
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
