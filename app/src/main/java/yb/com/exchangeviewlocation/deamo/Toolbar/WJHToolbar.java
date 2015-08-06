package yb.com.exchangeviewlocation.deamo.Toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import yb.com.exchangeviewlocation.R;

/**
 * Created by Administrator on 2015/6/30.
 */
public class WJHToolbar extends View{
    private static final String TAG = "WJHToolbar";
    private int mChoose_Color =0xffffff;
    private String mText="";
    private int mCircle_Bg =0x88ffffff;
    private Bitmap mIconBitmap;
    private int mTextSize= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics());
    private int mModo =3;//模式1表示只有颜色变化2标志只有圆形背景3表示都有

    private float mAlpah;
    private Canvas mCanvas;
    private  Bitmap mBitmap;
    private Paint mPaint;
    private Paint mBgPaint;
    private Canvas mBgCanvas;
    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;
    private int DefultTextColor=0xffffff;
    private static final String SAVEINSTANCESTATE= "saveinstancestate";
    private static final String MYSAVEINSTANCESTATE= "mysaveinstancestate";
    private static final  String MYBGOffSET="MYBGOffSET";
    private static final int ONALY_CIRCLE_BG=2;
    private static final int ONALY_COLOR=1;
    private static final int COLOR_AND_CIRCLE_BG=3;

    private Bitmap mBgBitmap;
    private int mViewWhidth;//控件宽度
    private int mViewHeight;//控件高度
    private float mBgOffset =0;//图片偏移量
    private int mOffsetWhidth;//偏移宽度



    public WJHToolbar(Context context) {
        this(context, null);
    }

    public WJHToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WJHToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tvl=context.obtainStyledAttributes(attrs, R.styleable.WJHToolbar);
        int n = tvl.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr= tvl.getIndex(i);
            switch (attr){

                case R.styleable.WJHToolbar_tb_icon:
                    BitmapDrawable bd = (BitmapDrawable) tvl.getDrawable(attr);
                    mIconBitmap=bd.getBitmap();
                    break;

                case R.styleable.WJHToolbar_tb_choose_color:
                    mChoose_Color =tvl.getColor(attr, DefultTextColor);
                    break;

                case R.styleable.WJHToolbar_tb_text:
                    mText= tvl.getString(attr);
                    break;

                case R.styleable.WJHToolbar_tb_textsize:
                    mTextSize = tvl.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;

                case R.styleable.WJHToolbar_tb_circle_bg:
                    mCircle_Bg =tvl.getColor(attr, mCircle_Bg);
                    break;
                case R.styleable.WJHToolbar_tb_modo:
                        mModo =tvl.getInt(attr, mModo);
                    break;

            }
        }
        tvl.recycle();
        mTextBound = new Rect();
        mTextPaint= new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(DefultTextColor);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight=getMeasuredHeight()+getPaddingBottom()+getPaddingTop();
        mViewWhidth=getMeasuredWidth()+getPaddingLeft()+getPaddingRight();
        int iconWitdh=Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height());
        int lift=getMeasuredWidth()/2-iconWitdh/2;
        int top = (getMeasuredHeight()-mTextBound.height())/2-iconWitdh/2;
        mIconRect = new Rect(lift,top,lift+iconWitdh,top+iconWitdh);

        mOffsetWhidth=(mViewWhidth+getPaddingLeft())-((mViewWhidth/2)-(mViewHeight/2));//视图的宽度-（视图的一半-半径）==移动距离
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        int alpha = (int) Math.ceil(255*mAlpah);
        if (mModo ==COLOR_AND_CIRCLE_BG || mModo ==ONALY_CIRCLE_BG) {//判断是否绘制圆形背景或者两个modo都绘制
            setUpTargetBg(alpha);//圆形背景```
            canvas.drawBitmap(mBgBitmap, 0, 0, null);
            if (mModo ==ONALY_CIRCLE_BG)//只有背景时需要将Alpha设置成0为了让字体显示出来
                alpha=0;
        }
        if (mModo ==COLOR_AND_CIRCLE_BG || mModo ==ONALY_COLOR){//如果只选择绘制颜色变化或者同时绘制两个modo
            setUpTargetBitmap(alpha);//选中颜色
            drawSourceText(canvas, alpha);//绘出字体
            drawTargetText(canvas, alpha);//字体选中时候绘出颜色
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        if (mModo==ONALY_CIRCLE_BG)
        drawSourceText(canvas, alpha);//绘出字体

        super.onDraw(canvas);
    }


    /**
     * 绘制变色文本Alpha
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mChoose_Color);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth()/2-mTextBound.width()/2;
        int y =mIconRect.bottom+ mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 绘制文本
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(DefultTextColor);
        mTextPaint.setAlpha(255-alpha);
        int x = getMeasuredWidth()/2-mTextBound.width()/2;
        int y =mIconRect.bottom+ mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 绘制背景
     */
    private void setUpTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(mViewWhidth,mViewHeight,
                Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mChoose_Color);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);

        mCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap
                , null, mIconRect, mPaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVEINSTANCESTATE, super.onSaveInstanceState());
        bundle.putFloat(MYSAVEINSTANCESTATE, mAlpah);
        bundle.putFloat(MYBGOffSET, mBgOffset);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            super.onRestoreInstanceState(((Bundle) state).getParcelable(SAVEINSTANCESTATE));
            mAlpah=((Bundle) state).getFloat(MYSAVEINSTANCESTATE,0);
            mBgOffset=((Bundle) state).getFloat(MYBGOffSET,0);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    private void setUpTargetBg(int alpha) {
         mBgBitmap = Bitmap.createBitmap(mViewWhidth,mViewHeight,
                Bitmap.Config.ARGB_8888);
        mBgCanvas = new Canvas(mBgBitmap);
        mBgPaint = new Paint();
        mBgPaint.setColor(mCircle_Bg);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setAlpha((int) (alpha * 0.3));
        mBgCanvas.drawCircle((mViewWhidth / 2 - getPaddingLeft())+ mBgOffset, (mViewHeight / 2) - getPaddingBottom() - getPaddingTop(), mViewHeight / 2, mBgPaint);
    }

    /**
     * 设置渐变
     * @param alpha
     */
    public void setIconAlpha(float alpha){
        this.mAlpah=alpha;
        invalidateView();
    }


    public float getIconAlpha(){
        return this.mAlpah;
    }
    /**
     * 设置偏移量
     * @param offset
     */
    public void setCircleTranslation(float offset){
        this.mBgOffset =(mOffsetWhidth)*offset;
        invalidateView();
    }

    /**
     * 偏移量可以使用Alpah固定值为1
     * @param offset
     */
    public void setOnalyCircleTranslation(float offset){
        this.mBgOffset =(mOffsetWhidth)*offset;
        this.mAlpah=1;
        invalidateView();
    }
    /**
     * 可偏移可设置Alpha
     * @param offset
     */
    public void setCircleTranslationAndAlpha(float offset,boolean isLiftItme){
        if (isLiftItme){
            this.mBgOffset =(mOffsetWhidth)*offset;
            this.mAlpah=1-offset;
        }else {
            this.mBgOffset =(mOffsetWhidth)*(offset-1);
            this.mAlpah=offset;
        }
        invalidateView();
    }



    private void invalidateView() {
        if (Looper.getMainLooper()== Looper.myLooper()){
            invalidate();
        }else {
            postInvalidate();
        }
    }
}
