package yb.com.exchangeviewlocation.deamo.pullview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.utils.Logger;

public class PullToZoomListView2 extends ListView implements
		AbsListView.OnScrollListener{
	private Rect normal = new Rect();

	private static final String TAG = "PullToZoomListView2";
	private View headview;
	private int headerHight;//布局文件高度
	private float startY=-1;
	//下拉状态
	private static final int size = 4;
	private float oY;

	public PullToZoomListView2(Context paramContext) {
		super(paramContext);
		init(paramContext);
	}

	public PullToZoomListView2(Context paramContext,
							   AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}

	public PullToZoomListView2(Context paramContext,
							   AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}


	private void init(Context paramContext) {
//		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
//		((Activity) paramContext).getWindowManager().getDefaultDisplay()
//				.getMetrics(localDisplayMetrics);
		this.setOnScrollListener(this);
		headview = LayoutInflater.from(paramContext).inflate(R.layout.pull_listviewhead,null);
		MeasuredView(headview);
		headerHight =headview.getMeasuredHeight();
		setTopHeader(-headerHight,headview.getBottom());
		addHeaderView(headview);
	}
	private void MeasuredView(View view){
		ViewGroup.LayoutParams pl = view.getLayoutParams();
		if (pl==null){
			pl=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width= ViewGroup.getChildMeasureSpec(0,0,pl.width);
		int hight ;
		int tempHight=pl.height;
		if (tempHight>0){
			hight=MeasureSpec.makeMeasureSpec(tempHight,MeasureSpec.EXACTLY);
		}
		else
		{
			hight=MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, hight);
	}
	/**
	 * 得到top
	 * @return
	 */
	private void setTopHeader(int topPadding,int deltaY){
		headview.setPadding(headview.getLeft(), topPadding, headview.getRight(), deltaY);
//		headview.layout(headview.getLeft(), topPadding, headview.getRight(),deltaY);
		headview.invalidate();
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Logger.v(TAG, "scrollState=" + scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Logger.v(TAG,"firstVisibleItem="+firstVisibleItem);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				oY=ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (Math.abs(ev.getY()-oY)>8) {
					oY=ev.getY();
					return true;
				}
				break;
		}

		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (headview == null) {
			return super.onTouchEvent(ev);
		} else {
			commOnTouchEvent(ev);
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 处理移动过程中的操作
	 * @param ev
	 */
	private void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				startY = ev.getY();
				break;
			case MotionEvent.ACTION_UP:
				if (isNeedAnimation()) {
					// Log.v("mlguitar", "will up and animation");
					animation();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				final float preY = startY;
				float nowY = ev.getY();
				/**
				 * size=4 表示 拖动的距离为屏幕的高度的1/4
				 */
				int deltaY = (int) (preY - nowY) / size;
				// 滚动
				// scrollBy(0, deltaY);

				startY = nowY;
				if (isNeedMove()) {
					Logger.v(TAG,"isEmpty="+normal.isEmpty());
					if (normal.isEmpty()) {
						normal.set(headview.getLeft(), headview.getTop(),
								headview.getRight(), headview.getBottom());
						Logger.v(TAG, "isEmpty=" + headview.getRight());
						return;
					}
					int yy = headview.getTop() - deltaY;
					Logger.v(TAG, "isEmpty=" +deltaY);
					// 移动布局
//					headview.layout(headview.getLeft(), yy, headview.getRight(),
//							headview.getBottom() - deltaY);
					setTopHeader(yy,headview.getBottom() - deltaY);
				}
				break;
			default:
				break;
		}
	}
	public boolean isNeedMove() {
		int offset = headview.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	public void animation() {
		TranslateAnimation ta = new TranslateAnimation(0, 0, headview.getTop(),
				normal.top);
		ta.setDuration(200);
		headview.startAnimation(ta);
//		headview.layout(normal.left, normal.top, normal.right, normal.bottom);
		setTopHeader(normal.top, normal.bottom);
		normal.setEmpty();
	}
}