package com.xiaokun.advance_practive.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xiaokun.advance_practive.R;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/28
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ProgressView extends View {

    private static final String TAG = "ProgressView";

    private float mRingWidth;
    private float mStartAngle;
    private int mUnreachColor;
    private int mReachColor;
    private Paint mPaint;
    private float mProgress;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, 0);

        mRingWidth = ta.getDimension(R.styleable.ProgressView_ringWidth, 9);
        mStartAngle = ta.getFloat(R.styleable.ProgressView_startAngle, 0);
        mUnreachColor = ta.getColor(R.styleable.ProgressView_unReachColor, Color.parseColor("#CCD1D0"));
        mReachColor = ta.getColor(R.styleable.ProgressView_reachColor, Color.parseColor("#7D807F"));
        mProgress = ta.getFloat(R.styleable.ProgressView_progress, 0);

        ta.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mUnreachColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mRingWidth);
        //直接绘制底层圆
        canvas.drawCircle(width / 2, height / 2, (width - mRingWidth) / 2, mPaint);

        //换颜色绘制圆弧
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mRingWidth);
        RectF rectF = new RectF(mRingWidth / 2, mRingWidth / 2, width - mRingWidth / 2, height - mRingWidth / 2);
        canvas.drawArc(rectF, mStartAngle, mProgress, false, mPaint);

        //绘制百分比字
        String text = (int) (mProgress * 100 / 360) + "%";
        mPaint.setTextSize(30);

        //文字高度
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        float textHeight = bounds.height();
        float textWidth = mPaint.measureText(text);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        canvas.drawText(text, (width - textWidth) / 2, (height + textHeight) / 2, mPaint);
    }

    public void setProgress(float progress) {
        mProgress = progress * 360 / 100;
        invalidate();
        Log.e(TAG, "setProgress(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + progress);
    }

}
