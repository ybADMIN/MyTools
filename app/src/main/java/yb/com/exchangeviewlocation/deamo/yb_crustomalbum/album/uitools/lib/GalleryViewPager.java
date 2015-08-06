package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class GalleryViewPager extends ViewPager {


	private boolean isCanScroll=true;//能否滚动

	public GalleryViewPager(Context context) {
		this(context, null);
	}

	public GalleryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof TouchImageView) {
			return ((TouchImageView) v).canScrollHorizontally(dx);
		} else {
			return super.canScroll(v, checkV, dx, x, y);
		}
	}

	public void setScanScroll(boolean isCanScroll){

		this.isCanScroll = isCanScroll;

	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(!isCanScroll)
		{
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(!isCanScroll)
		{
			return false;
		}
		return super.onTouchEvent(ev);
	}


}
