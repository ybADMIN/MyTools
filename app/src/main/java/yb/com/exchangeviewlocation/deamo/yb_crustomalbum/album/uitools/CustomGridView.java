package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CustomGridView extends GridView{

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	float oY=0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			oY=ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs(ev.getY()-oY)>8) {
				return true;
			}
			break;
		}
		
		return super.onInterceptTouchEvent(ev);
	}

}
