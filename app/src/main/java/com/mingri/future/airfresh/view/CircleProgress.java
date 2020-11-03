package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.util.PxUtils;

/**
 * Created by Administrator on 2017/7/3.
 */
public class CircleProgress extends View {
    private int mHeight;
    private int mWidth;
    Paint mArcPaint;
    RectF mCircleRect;
    float mEndAngle=50;
    private int curPercent=0;

    Paint mTextPaint;

    private float textYCenter;
    private String value = "75 %";
    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.mipmap.houm_tanc_bg);
        mArcPaint=new Paint();
        mArcPaint.setColor(context.getResources().getColor(R.color.lvwang_life));
        mArcPaint.setStrokeWidth(29);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(context.getResources().getColor(R.color.lvwang_life));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(PxUtils.spToPx(33, context));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }
    float dis=35;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = widthSize;
        mHeight = heightSize;
        setMeasuredDimension(mWidth, mHeight);
        mCircleRect = new RectF(dis,  dis, mWidth-dis , mHeight-dis);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mCircleRect,270,mEndAngle-360,false,mArcPaint);
    }

    public void setPercent(int progress){
        if( progress > 100 )
            progress = 100;

        curPercent=progress;
        float p = (100 - progress)*3.6f;
        mEndAngle=p;
        postInvalidate();
    }
    public int getPercen(){
        return curPercent;
    }
}
