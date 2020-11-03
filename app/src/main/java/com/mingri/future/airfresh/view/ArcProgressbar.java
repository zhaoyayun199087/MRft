package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.util.LogUtils;

/**
 * 弧形进度条
 *
 * @author Eric
 */
public class ArcProgressbar extends View {
    private int bgStrokeWidth = 10;
    private int barStrokeWidth = 8;
    private int bgColor = 0x20c0c0c0;
    private int barColor = Color.RED;
    private int smallBgColor = Color.WHITE;
    private int progress = 0;
    private int angleOfMoveCircle = 140;// 移动小园的起始角度。
    private int startAngle = 230;
    private int endAngle = 80;
    private Paint mPaintBar = null;
    private Paint mPaintSmallBg = null;
    private Paint mPaintBg = null;
    private Paint mPaintRotate = null;

    private RectF rectBg = null;
    private int mWidth, mHight;
    private float mX,mY;
    private float mArcRadius;
    private float mRotate;
    private Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(),
            R.mipmap.liangd_icon_dian);
    private float mDis = pointBitmap.getWidth() / 2 + 3;
    private boolean showSmallBg = true;// 是否显示小背景。
    private boolean showMoveCircle = true;// 是否显示移动的小园。

    public ArcProgressbar(Context context) {
        super(context);
    }

    public ArcProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintBg = new Paint();
        mPaintBg.setAntiAlias(true);
        mPaintBg.setStyle(Style.STROKE);
        mPaintBg.setStrokeCap(Paint.Cap.ROUND);

        mPaintBg.setStrokeWidth(bgStrokeWidth);
        mPaintBg.setColor(bgColor);

        mPaintBar = new Paint();
        mPaintBar.setAntiAlias(true);
        mPaintBar.setStyle(Style.STROKE);
        mPaintBar.setStrokeWidth(barStrokeWidth);
        mPaintBar.setColor(0x3f27252d);

        mPaintRotate = new Paint();
        mPaintRotate.setStrokeWidth(9);
        mPaintRotate.setAntiAlias(true);
        mPaintRotate.setStyle(Style.STROKE);
        mPaintRotate.setStrokeCap(Paint.Cap.ROUND);
        mPaintRotate.setColor(getResources().getColor(R.color.clock_percent));

        mPaintSmallBg = new Paint();
        mPaintSmallBg.setAntiAlias(true);
        mPaintSmallBg.setStyle(Style.STROKE);
        mPaintSmallBg.setStrokeCap(Paint.Cap.ROUND);
        mPaintSmallBg.setStrokeWidth(barStrokeWidth);
        mPaintSmallBg.setColor(0xef171a1b);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//        return super.onTouchEvent(event);
        int x= (int) (motionEvent.getX()-mX);
        int y= (int) (motionEvent.getY()-mY);
        LogUtils.d("ontouch ddd");
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float PI = (float) (Math.asin(x / Math.sqrt(x * x + y * y)) / Math.PI);
                if((x > 0  && y<0 )||x<=0 && y<0 ){
                    int angle = (int) (PI *180 +40);
                    if(angle<0)angle=0;
                    if(angle>80) angle=80;
                    mRotate= angle;

                    if(listener!=null)listener.curAngle((int)((mRotate + 10f)*0.9f ));
                }
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        invalidate();
        return true;
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHight = getMeasuredHeight();

        mWidth = mWidth- (int)mDis*2;
        mHight = (int) (mWidth / (2*(Math.tan( 70D *Math.PI / 180D))));


        mArcRadius = (float) (mWidth / (4*( Math.sin(70D *Math.PI / 180D)*( Math.cos(70D *Math.PI / 180D)) )));
        LogUtils.d("width is " + mWidth + " hight " + mHight + " radius " + mArcRadius);

        mX = mDis+ mWidth / 2;
        mY = mDis + mArcRadius;
        // 画弧形的矩阵区域。
        rectBg = new RectF(mX - mArcRadius, mY - mArcRadius, mX + mArcRadius, mY + mArcRadius);
        setMeasuredDimension((int) (mWidth + mDis * 2), (int) (mHight + mDis * 2));

        int[] colors = {0xffced34b, 0xff139be8, 0xff139be8};
        SweepGradient gradient = new SweepGradient(rectBg.centerX(), rectBg.centerY(), colors, new float[]{0.01f, 0.216f, 3f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-133, rectBg.centerX(), rectBg.centerY());
        gradient.setLocalMatrix(matrix);
        mPaintRotate.setShader(gradient);
    }

    private void init(Canvas canvas) {


        // 弧形背景。
        canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintBg);

        // 弧形小背景。
        if (showSmallBg) {
            canvas.drawArc(rectBg, startAngle, endAngle, false, mPaintSmallBg);
        }

        // 弧形ProgressBar。

        canvas.drawArc(rectBg, startAngle, progress, false, mPaintBar);

        canvas.drawArc(rectBg, -130,
                mRotate, false, mPaintRotate);
        float x = 0,y = 0;
        if( mRotate <=40 ){
            double v = 40 - mRotate;
            x = (float) (mX - mArcRadius*Math.sin(v * Math.PI / 180));
            y = (float) (mY - mArcRadius*Math.cos(v * Math.PI / 180));
        }else{
            double v =  mRotate - 40;
            x = (float) (mX + mArcRadius*Math.sin(v * Math.PI / 180));
            y = (float) (mY - mArcRadius*Math.cos(v * Math.PI / 180));
        }
        LogUtils.d("dramap " + x + " " + y);
        canvas.translate(x, y);
        canvas.drawBitmap(pointBitmap, null,new RectF(-pointBitmap.getWidth()/2,- pointBitmap.getHeight()/2,pointBitmap.getWidth()/2, pointBitmap.getHeight()/2), null);
        canvas.restore();


    }

    public void addProgress(int _progress) {
        progress += _progress;
        angleOfMoveCircle += _progress;
        System.out.println(progress);
        if (progress > endAngle) {
            progress = 0;
            angleOfMoveCircle = startAngle;
        }
        invalidate();
    }

    /**
     * 设置弧形背景的画笔宽度。
     */
    public void setBgStrokeWidth(int bgStrokeWidth) {
        this.bgStrokeWidth = bgStrokeWidth;
    }

    /**
     * 设置弧形ProgressBar的画笔宽度。
     */
    public void setBarStrokeWidth(int barStrokeWidth) {
        this.barStrokeWidth = barStrokeWidth;
    }

    /**
     * 设置弧形背景的颜色。
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * 设置弧形ProgressBar的颜色。
     */
    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    /**
     * 设置弧形小背景的颜色。
     */
    public void setSmallBgColor(int smallBgColor) {
        this.smallBgColor = smallBgColor;
    }


    /**
     * 是否显示小背景。
     */
    public void setShowSmallBg(boolean showSmallBg) {
        this.showSmallBg = showSmallBg;
    }

    /**
     * 是否显示移动的小圆。
     */
    public void setShowMoveCircle(boolean showMoveCircle) {
        this.showMoveCircle = showMoveCircle;
    }

    private BrightAngleListener  listener;
    public void setAngleListener(BrightAngleListener listener){
        this.listener=listener;
    }
    public interface  BrightAngleListener{
        void  curAngle(int angle);
    }
    public void setAngle(int angle){
        mRotate=angle;
        postInvalidate();
    }
}