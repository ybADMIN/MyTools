package yb.com.exchangeviewlocation.deamo.readDot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import yb.com.exchangeviewlocation.R;

/**
 * Created by Administrator on 2015/8/13.
 */
public class NewView extends View{
    private String mText;
    private int mText_color=0x88ffffff;
    private int mBGColor=0x88ff0000;
    private int mTextSize;
    private Rect mTextBound;
    private Paint mTextPaint;
    private int mViewHeight;//view高度
    private int mViewWhidth;//View 宽度

    public NewView(Context context) {
        this(context,null);
    }

    public NewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray tvl=context.obtainStyledAttributes(attrs, R.styleable.NewView);
        int n = tvl.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr= tvl.getIndex(i);
            switch (attr){
                case R.styleable.NewView_newview_bg:
                    mBGColor =  tvl.getColor(attr,mBGColor);
                    break;
                case R.styleable.NewView_newview_text:
                     mText =  tvl.getString(attr);
                    break;
                case R.styleable.NewView_newview_textcolor:
                    mText_color =tvl.getColor(attr, mText_color);
                    break;
                case R.styleable.NewView_newview_textsize:
                    mTextSize = tvl.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
            }
            }
        tvl.recycle();
        mTextBound = new Rect();
        mTextPaint= new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mText,0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            float textWidth = mTextBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
            float textHeight = mTextBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawPaint(mBgPaint);//绘制背景
        drawText(canvas);//绘制文字
        super.onDraw(canvas);
    }

    private void drawText(Canvas canvas){
//        mTextPaint.setAntiAlias(true)
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG)); ;
        mTextPaint.setColor(mBGColor);
        RectF oval2= new RectF();
        oval2.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawOval(oval2, mTextPaint);
//      canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mTextPaint);
        mTextPaint.setColor(mText_color);
        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2, getHeight() / 2 + mTextBound.height() / 2, mTextPaint);
    }
}
