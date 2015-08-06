package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 单位转换类
 * @author Administrator
 *
 */
public class DensityUtils {
	
	public static float getDensity(Context context) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return scale;  
    }  
	
    /** 
     * 根据手机的分辨率从 dp的单位 转成为 px
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /** 
     * sp转px 
     *  
     * @param context 
     * @param val 
     * @return 
     */  
    public static int sp2px(Context context, float spVal)  
    {  
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  
                spVal, context.getResources().getDisplayMetrics());  
    }  
    
    /** 
     * px转sp 
     *  
     * @param fontScale 
     * @param pxVal 
     * @return 
     */  
    public static float px2sp(Context context, float pxVal)  
    {  
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);  
    }  
}
