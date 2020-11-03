package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/7/7.
 */
public class GradientCircleVIew extends View {
    RectF recProgress, recCircle;
    Paint paint, circlePaint;
    int rotation = 0;
    Matrix matrix;
    Paint mTextPaint;
    float textYCenter;
    private int percent;

//    int[] mColors = {0x0007a5ff, 0x4007a5ff, 0xff07a5ff, 0x0007a5ff};
int[] mColors = {0x0007a5ff, 0x0007a5ff, 0xff07a5ff, 0x0007a5ff};

    public GradientCircleVIew(Context context) {
        this(context, null);
    }

    public GradientCircleVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientCircleVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(getResources().getColor(R.color.updata_progress));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(50);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.clock_text));
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);

//      SweepGradient shader=new SweepGradient(getMeasuredWidth()/2,getMeasuredHeight()/2,mColors,null);
        paint.setStyle(Paint.Style.STROKE);

        circlePaint = new Paint();
        circlePaint.setColor(getResources().getColor(R.color.updata_progress));
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(4);
        circlePaint.setStyle(Paint.Style.STROKE);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dis = 30;
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        recProgress = new RectF(dis, dis, getWidth() - dis, getHeight() - dis);
        recCircle = new RectF(dis + 22, dis + 22, getWidth() - dis - 22, getHeight() - dis - 22);

        matrix = new Matrix();
        matrix.setRotate(-90, getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        textYCenter = getHeight() / 2 + (fm.descent - fm.ascent) / 2 - fm.descent;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        float rota = (float) (rotation / 360.0) > 0.6f ? 0.6f:(float) (rotation / 360.0);
        SweepGradient shader = new SweepGradient(getWidth() / 2, getHeight() / 2, mColors, new float[]{0.02f, 0.02f, rota , 1f});
        paint.setShader(shader);
        paint.setStrokeCap(Paint.Cap.ROUND);
        shader.setLocalMatrix(matrix);
        canvas.drawArc(recProgress, -90, rotation, false, paint);
        canvas.drawArc(recCircle, 0, 360, false, circlePaint);
        if(percent!=0)
        canvas.drawText(percent + "%", getWidth() / 2, textYCenter, mTextPaint);


//        this.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                rotation=rotation+2;
//                invalidate();
//                if(rotation>360)rotation=360;
//            }
//        }, 100);

    }

    public void setPercent(int perc){
        this.percent=perc;
        rotation= (int) (perc*3.6f);
        invalidate();
    }

}
