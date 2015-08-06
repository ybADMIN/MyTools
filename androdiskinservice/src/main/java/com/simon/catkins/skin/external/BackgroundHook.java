package com.simon.catkins.skin.external;

import android.os.Build;
import android.util.TypedValue;
import android.view.View;

import com.simon.catkins.skin.Hook;
import com.simon.catkins.skin.HookType;

/**
 * @author Simon Yu
 */
public class BackgroundHook implements Hook {
    private final static Apply APPLY = new Apply() {
        @Override
        public void to(View view, TypedValue value) {
            String[] tyep = value.string.toString().split("/");
           if (tyep[0].equals("color")){
               view.setBackgroundColor(ExtResources.getInstance()
                       .getResources()
                       .getColor(value.resourceId));
           }
            if (tyep[0].equals("mipmap") || tyep[0].equals("drawable")){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setBackground(ExtResources.getInstance().getResources().getDrawable(value.resourceId, null));
                }else if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
                    view.setBackgroundDrawable(ExtResources.getInstance().getResources().getDrawable(value.resourceId));
                }else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
                    view.setBackgroundDrawable(ExtResources.getInstance().getResources().getDrawable(value.resourceId));
                }
            }
        }
    };

    @Override
    public int hookType() {
        return HookType.REFERENCE_ID;
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
