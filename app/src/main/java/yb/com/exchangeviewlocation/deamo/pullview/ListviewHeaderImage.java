package yb.com.exchangeviewlocation.deamo.pullview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import yb.com.exchangeviewlocation.R;

/**
 * Created by Administrator on 2015/7/24.
 */
public class ListviewHeaderImage extends ListView {


    private static final String TAG = "ListviewHeaderImage";
    private Bitmap mHeadImage;//header
    private boolean isEable;
    private ImageView mHeaderImgView;
    /*头部View 的容器*/
    private FrameLayout mHeaderContainer;
    /*屏幕的高度*/
    private int mScreenHeight;
    /*屏幕的宽度*/
    private int mScreenWidth;
    private int mHeaderHeight;
    /*无效的点*/
    private static final int INVALID_POINTER = -1;
    /*滑动动画执行的时间*/
    private static final int MIN_SETTLE_DURATION = 200; // ms
    /*定义了一个时间插值器，根据ViewPage控件来定义的*/
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
    /*记录上一次手指触摸的点*/
    private float mLastMotionX;
    private float mLastMotionY;
    /*当前活动的点Id,有效的点的Id*/
    protected int mActivePointerId = INVALID_POINTER;
    /*开始滑动的标志距离*/
    private int mTouchSlop;
    /*放大的倍数*/
    private float mScale;
    /*上一次放大的倍数*/
    private float mLastScale;
    /*最大放大的倍数*/
    private final float mMaxScale = 2.0f;
    /*是否需要禁止ListView 的事件响应*/
    private boolean isNeedCancelParent;
    /*这个不解释*/
    private OnScrollListener mScrollListener ;
    /*下拉刷新的阈值*/
    private final float REFRESH_SCALE = 1.20F;
    /*下拉刷新监听*/
    private OnRefreshListener mRefreshListener ;


    public ListviewHeaderImage(Context context) {
      this(context, null, 0);
    }

    public ListviewHeaderImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListviewHeaderImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListviewHeaderImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context,AttributeSet attrs){
        if (attrs==null)
            return;
        TypedArray tvl=context.obtainStyledAttributes(attrs, R.styleable.ListviewHeaderImage);
        int n = tvl.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr= tvl.getIndex(i);
            switch (attr){

                case R.styleable.ListviewHeaderImage_enabledHead:
                    isEable=tvl.getBoolean(attr,true);
                    break;

                case R.styleable.ListviewHeaderImage_headerImage:
                    BitmapDrawable bd = (BitmapDrawable) tvl.getDrawable(attr);
                    mHeadImage=bd.getBitmap();
                    break;

            }
        }
        tvl.recycle();
        if (isEable){
        createHeadView(context);
        }

    }

    private void createHeadView(Context context){
          /*这里获取的是一个无用值，可忽略*/
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
       /*创建头部View 的容器*/
        mHeaderContainer = new FrameLayout(context);
       /*获取屏幕的像素值*/
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
        /*设置头部View 的初始大小*/
        mHeaderHeight = (int) ((9 * 1.0f / 16) * mScreenWidth);
        LayoutParams absLayoutParams = new LayoutParams(mScreenWidth, mHeaderHeight);
        mHeaderContainer.setLayoutParams(absLayoutParams);
       /*创建图片显示的View*/
        mHeaderImgView = new ImageView(context);
        FrameLayout.LayoutParams imgLayoutParams = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mHeaderImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mHeaderImgView.setLayoutParams(imgLayoutParams);
        if (mHeadImage!=null)
            mHeaderImgView.setImageBitmap(mHeadImage);
        mHeaderContainer.addView(mHeaderImgView);
      /*增加头部View*/
        addHeaderView(mHeaderContainer);
      /*设置监听事件*/
        super.setOnScrollListener(new InternalScrollerListener() );
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
         /*计算 x，y 的距离*/
                int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                if (mActivePointerId == INVALID_POINTER)
                    break;
                mLastMotionX = MotionEventCompat.getX(ev, index);
                mLastMotionY = MotionEventCompat.getY(ev, index);
                // 结束动画，目前没做处理，可忽略
                abortAnimation();
          /*计算算一次放缩的比例*/
                mLastScale = (this.mHeaderContainer.getBottom() / this.mHeaderHeight);
       /*当按下的时候把这个标志为设为有效*/
                isNeedCancelParent = true ;
                break;
            case MotionEvent.ACTION_MOVE:
                int indexMove = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, indexMove);
                if (mActivePointerId == INVALID_POINTER) {
            /*这里相当于松手*/
                    finishPull();
                    isNeedCancelParent = true ;
                } else {
           /*这是一个关键值，通过这个值来判断是否需要放大图片*/
                    if (mHeaderContainer.getBottom() >= mHeaderHeight) {
                        ViewGroup.LayoutParams params = this.mHeaderContainer.getLayoutParams();
                        final float y = MotionEventCompat.getY(ev, indexMove);
                        float dy = y - mLastMotionY;
                        float f = ((y - this.mLastMotionY + this.mHeaderContainer
                                .getBottom()) / this.mHeaderHeight - this.mLastScale)
                                / mMaxScale + this.mLastScale;

                        if ((this.mLastScale <= 1.0D) && (f <= this.mLastScale)) {
                            params.height = this.mHeaderHeight;
                            this.mHeaderContainer.setLayoutParams(params);
                            return super.onTouchEvent(ev);
                        }
            /*这里设置紧凑度*/
//                        Log.v(TAG,mHeaderHeight +"----"+params.height+"");
                        dy = dy *0.5f * (mHeaderHeight * 1.0f / params.height);
                        mLastScale = (dy + params.height) * 1.0f / mHeaderHeight;
                        mScale = clamp(mLastScale, 1.0f, mMaxScale);
                        params.height = (int) (mHeaderHeight * mScale);
                        mHeaderContainer.setLayoutParams(params);
                        mLastMotionY = y;
           /*这里，如果图片有放大，则屏蔽ListView 的其他事件响应*/
                        if(isNeedCancelParent ){
                            isNeedCancelParent = false;
                            MotionEvent motionEvent = MotionEvent.obtain(ev);
                            motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(motionEvent);
                        }
                        return true;
                    }
                    mLastMotionY = MotionEventCompat.getY(ev, indexMove);
                }
                break;
            case MotionEvent.ACTION_UP:
      /*结束事件响应，做相应的操作*/
                finishPull();
                break;
            case MotionEvent.ACTION_POINTER_UP:
    /*这里需要注意，多点处理，这里的处理方式是：如果有两个手指按下，抬起的是后按下的手指，则不做处理
      * 如果抬起的是最先按下的手指，则复原图片效果。
      * */
                int pointUpIndex = MotionEventCompat.getActionIndex(ev);
                int pointId = MotionEventCompat.getPointerId(ev, pointUpIndex);
                if (pointId == mActivePointerId) {
        /*松手执行结束拖拽操作*/
        /*结束事件响应，做相应的操作*/
                    finishPull();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private class InternalScrollerListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mScrollListener != null) {
                mScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            float diff = mHeaderHeight - mHeaderContainer.getBottom();
            if ((diff > 0.0F) && (diff < mHeaderHeight)) {
                int i = (int) (0.3D * diff);
                mHeaderImgView.scrollTo(0, -i);
            } else if (mHeaderImgView.getScrollY() != 0) {
                mHeaderImgView.scrollTo(0, 0);
            }
//            Log.v("zgy", "=========height===" + getScrolledY());
            if (mScrollListener != null){
                mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }
    public int getScrolledY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }
        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l ;
    }
    private void abortAnimation() {
   /*啥都没做，暂时没做而已*/
    }
    private void finishPull() {
        mActivePointerId = INVALID_POINTER;
   /*这是一个阈值，如果成立，则表示图片已经放大了，在手指抬起的时候需要复原图片*/
        if (mHeaderContainer.getBottom() > mHeaderHeight){
            // Log.v(zgy, ===super====onTouchEvent========);
            //这里是下拉刷新的阈值，当达到了，则表示需要刷新，可以添加刷新动画
            if (mScale > REFRESH_SCALE){
                if (mRefreshListener != null){
                    mRefreshListener.onRefresh();
                }
            }
            //图片复原动画
            pullBackAnimation();
        }
    }
    /**
     * 
     */
    private void pullBackAnimation(){
        ValueAnimator pullBack = ValueAnimator.ofFloat(mScale , 1.0f);
        pullBack.setInterpolator(sInterpolator);
        pullBack.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                ds=mScale/2;
                float value = (float) animation.getAnimatedValue();
                LayoutParams params = (LayoutParams) mHeaderContainer.getLayoutParams();
                params.height = (int) (mHeaderHeight * value);
                mHeaderContainer.setLayoutParams(params);
            }
        });
        pullBack.setDuration((long) (MIN_SETTLE_DURATION*mScale));
        pullBack.start();
    }

    /**
     *   通过事件和点的 id 来获取点的索引
     *
     * @param ev
     * @param id
     * @return
     */
    private int getPointerIndex(MotionEvent ev, int id) {
        int activePointerIndex = MotionEventCompat.findPointerIndex(ev, id);
        if (activePointerIndex == -1)
            mActivePointerId = INVALID_POINTER;
        return activePointerIndex;
    }
    public void setOnRefreshListener(OnRefreshListener l){
        mRefreshListener = l ;
    }
    public ImageView getHeaderImageView() {
        return this.mHeaderImgView;
    }
    private float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * 刷新监听
     */
    public interface OnRefreshListener {
        void onRefresh() ;
    }

    /**
     * 完成监听
     */
    public void computeRefresh(){
        if (mActivePointerId != INVALID_POINTER){
        }
    }
}
