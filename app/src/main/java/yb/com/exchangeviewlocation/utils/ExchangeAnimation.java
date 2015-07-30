package yb.com.exchangeviewlocation.utils;

import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Administrator on 2015/7/22.
 */
public class ExchangeAnimation {
    private final View mTarget;
    private final View mSource;
    private final String TAG="com.app.utils.ExchangeAnimation";
    private ViewGroup.LayoutParams mSparams;
    private ViewGroup.LayoutParams mTparams;
    private int[] mSlocation;//点击控件的坐标
    private int[] mTlocation;//目标控件坐标

    public ExchangeAnimation(View source, View target) {
        mSource =source;
        mTarget=target;
        if (mSource==null)
            Logger.e(TAG, "source is null");
        if (target==null)
            Logger.e(TAG, "target is null");

            init();
    }

    public void init(){
        mSparams=mSource.getLayoutParams();
        mTparams=mTarget.getLayoutParams();
        mSlocation=getLocationOnScreen(mSource);
        mTlocation=getLocationOnScreen(mTarget);
    }

    /**
     * 控件相对于屏幕的坐标
     * @param view
     * @return
     */
    public int[] getLocationOnScreen(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public void exchangeLocation(){

    }

}
