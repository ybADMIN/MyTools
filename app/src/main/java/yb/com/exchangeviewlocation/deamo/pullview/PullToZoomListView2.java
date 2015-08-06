package yb.com.exchangeviewlocation.deamo.pullview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import yb.com.exchangeviewlocation.utils.Logger;

public class PullToZoomListView2 extends ListView implements
		AbsListView.OnScrollListener {

	private int mScreenHeight;
	private FrameLayout mHeaderContainer;
	private ImageView mHeaderImage;
	private int mHeaderMaxHeight;
	private String TAG="PullToZoomListView2";
	private float mLastMotionY=-1.0f;
	private int mActivePointerId;
	private int mLastHeight;

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
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		((Activity) paramContext).getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		this.mScreenHeight = localDisplayMetrics.heightPixels;
		this.mHeaderContainer = new FrameLayout(paramContext);

		this.mHeaderImage = new ImageView(paramContext);
		int i = localDisplayMetrics.widthPixels;
		setHeaderViewSize(i,0);
		this.mHeaderContainer.addView(this.mHeaderImage);
		addHeaderView(this.mHeaderContainer);
		super.setOnScrollListener(this);
	}

	public void setHeaderViewSize(int paramInt1, int paramInt2) {
		ViewGroup.LayoutParams localObject = new AbsListView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,paramInt2);
		this.mHeaderContainer.setLayoutParams(localObject);

		mHeaderMaxHeight= (int) (9.0F * (paramInt1 / 16.0F));
		ViewGroup.LayoutParams imglp = mHeaderImage.getLayoutParams();
		if (imglp==null)
			imglp= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
		this.mHeaderImage.setLayoutParams(imglp);

	}
//public void setHeaderViewSize(int paramInt1, int paramInt2) {
//	Object localObject = this.mHeaderContainer.getLayoutParams();
//	if (localObject == null)
//		localObject = new AbsListView.LayoutParams(paramInt1, paramInt2);
//	((ViewGroup.LayoutParams) localObject).width = paramInt1;
//	((ViewGroup.LayoutParams) localObject).height = paramInt2;
//	mHeaderContainer.setTop(-paramInt2);
//	this.mHeaderContainer
//			.setLayoutParams((ViewGroup.LayoutParams) localObject);
//	this.mHeaderMaxHeight = paramInt2;
//}

	public ImageView getHeaderView() {
		return this.mHeaderImage;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}


	@Override
	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		int action =0xFF & paramMotionEvent.getAction();
		switch (action){
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_DOWN:
				this.mLastMotionY = paramMotionEvent.getY();
				this.mActivePointerId = paramMotionEvent.getPointerId(0);
				this.mLastHeight = this.mHeaderContainer.getBottom();
				Logger.v(TAG,"ACTION_DOWN");
				break;
			case MotionEvent.ACTION_UP:
				reset();

				Logger.v(TAG,"ACTION_UP");
				break;
			case MotionEvent.ACTION_MOVE:
				int firstItme = getFirstVisiblePosition();
				if (firstItme!=0)
				{
					break;
				}
				Logger.d("mmm", "mActivePointerId" + mActivePointerId);
				int j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
				if (j == -1) {
					Logger.e("PullToZoomListView2", "Invalid pointerId="
							+ this.mActivePointerId + " in onTouchEvent");
				}else {
					if (mLastMotionY==-1.0f)
						this.mLastMotionY = paramMotionEvent.getY(j);

					if (this.mHeaderContainer.getBottom() >=0) {
						ViewGroup.LayoutParams localLayoutParams = this.mHeaderImage
								.getLayoutParams();
						float f = (paramMotionEvent.getY(j)-mLastMotionY);
						Logger.v(TAG+"::paramMotionEvent:",paramMotionEvent.getY(j)+"--mLastMotionY:"+mLastMotionY);
						localLayoutParams.height+=(int)f;
						if (localLayoutParams.height<=0){
							localLayoutParams.height=0;
							return  super.onTouchEvent(paramMotionEvent);
						}
						if (localLayoutParams.height>=mHeaderMaxHeight) {
							localLayoutParams.height = mHeaderMaxHeight;
							return super.onTouchEvent(paramMotionEvent);
						}
						this.mHeaderImage.setLayoutParams(localLayoutParams);
					}
					this.mLastMotionY = paramMotionEvent.getY(j);
					return true;
				}
				Logger.v(TAG,"ACTION_MOVE");
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				try {
					this.mLastMotionY = paramMotionEvent.getY(paramMotionEvent
							.findPointerIndex(this.mActivePointerId));
					Logger.v(TAG,"ACTION_POINTER_DOWN");
				}catch (IllegalArgumentException e){
					Logger.e(TAG,e.getMessage());
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				int i = paramMotionEvent.getActionIndex();
				this.mLastMotionY = paramMotionEvent.getY(i);
				this.mActivePointerId = paramMotionEvent.getPointerId(i);
				break;
		}

		return super.onTouchEvent(paramMotionEvent);
	}


	private void reset() {
		this.mActivePointerId = -1;
		this.mLastMotionY = -1.0f;
		ViewGroup.LayoutParams localLayoutParams = this.mHeaderImage
				.getLayoutParams();
		localLayoutParams.height=0;
		this.mHeaderImage.setLayoutParams(localLayoutParams);
	}
}