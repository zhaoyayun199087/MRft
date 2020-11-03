package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/7/10.
 */
public class BrightControlView extends ViewGroup implements View.OnTouchListener {

    RectF colorRec;
    private float mX, mY;
    private float mRadus;
    private Bitmap pointBitmap = BitmapFactory.decodeResource(getResources(),
            R.mipmap.liangd_icon_dian);
    Paint paint;
    int rotation = 0;
    int[] colors = {0xffced34b, 0xff139be8, 0xff139be8};
    BrightAngleListener listener;
    public BrightControlView(Context context) {
        this(context, null);
    }

    public BrightControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrightControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.mipmap.liangd_bg);
        setOnTouchListener(this);
        paint = new Paint();
        paint.setStrokeWidth(9);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.clock_percent));

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        float dis = 4;
        float wid_left = 1.571f;  //宽 比率
        float left = getWidth() * wid_left + 20;
        float right = (left - getWidth()) / 2;

        colorRec = new RectF(-right , dis , getWidth() + right , left - dis );
        mX = colorRec.centerX();
        mY = colorRec.centerY();
        mRadus = (getWidth() + right + right)/2;
        LogUtils.d("center point " + getWidth()/2  + "  " + left/2);
        SweepGradient gradient = new SweepGradient(colorRec.centerX(), colorRec.centerY(), colors, new float[]{0.01f, 0.216f, 3f});
        Matrix matrix = new Matrix();
        matrix.setRotate(-130, colorRec.centerX(), colorRec.centerY());
        gradient.setLocalMatrix(matrix);
        paint.setShader(gradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(colorRec, -130,
                rotation, true, paint);
        canvas.save();
        float x = 0,y = 0;
        if( rotation <=40 ){
            double v = 40 - rotation;
            x = (float) (mX - mRadus*Math.sin(v * Math.PI / 180));
            y = (float) (mY - mRadus*Math.cos(v * Math.PI / 180));
        }else{
            double v =  rotation - 40;
            x = (float) (mX + mRadus*Math.sin(v * Math.PI / 180));
            y = (float) (mY - mRadus*Math.cos(v * Math.PI / 180));
        }
        canvas.translate(x, y);
        canvas.drawBitmap(pointBitmap, null,new RectF(-pointBitmap.getWidth()/2,- pointBitmap.getHeight()/2,pointBitmap.getWidth()/2, pointBitmap.getHeight()/2), null);
        canvas.restore();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x= (int) (motionEvent.getX()-colorRec.centerX());
        int y= (int) (motionEvent.getY()-colorRec.centerY());
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float PI = (float) (Math.asin(x / Math.sqrt(x * x + y * y)) / Math.PI);
                if((x > 0  && y<0 )||x<=0 && y<0 ){
                    int angle = (int) (PI *180 +40);
                    if(angle<0)angle=0;
                    if(angle>80) angle=80;
                    rotation= angle;

                    if(listener!=null)listener.curAngle(rotation);
                }
                requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                requestDisallowInterceptTouchEvent(false);
                break;
        }
        invalidate();
        return true;
    }
    public void setAngleListener(BrightAngleListener listener){
        this.listener=listener;
    }
    public interface  BrightAngleListener{
        void  curAngle(int angle);
    }
    public void setAngle(int angle){
        rotation=angle;
        postInvalidate();
    }
}
