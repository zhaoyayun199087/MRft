package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/7/5.
 */
public class ClockView extends ViewGroup implements View.OnTouchListener {

    TextView tvClock;
    ImageView view, circle, r_circle;
    View textView,childview, view_circle,rotate_circle;
    int angle = -90;
    int rotation = 0, centerX, centerY;
    Paint mArcPaint;
    RectF mCircleRect,subRec;

    Bitmap bmp;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = new ImageView(context);
        view.setImageResource(R.mipmap.xuanzhuan3);
        circle = new ImageView(context);

        circle.setBackgroundResource(R.drawable.circle);
        r_circle = new ImageView(context);
        r_circle.setBackgroundResource(R.drawable.rotate_circle);
        setBackgroundResource(R.mipmap.dings_icon_bg);
        tvClock = new TextView(context);
        tvClock.setTextSize(12 );
        tvClock.setText("00:00");
        tvClock.setTextColor(getResources().getColor(R.color.color_4989c5));


        addView(circle);
        addView(r_circle);
        addView(view);
        addView(tvClock);
        setOnTouchListener(this);

        mArcPaint = new Paint();
        mArcPaint.setColor(context.getResources().getColor(R.color.clock_percent));
        mArcPaint.setStrokeWidth(51);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStyle(Paint.Style.STROKE);




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);


    }

    float dis = 25.5f;

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int width = getWidth(), height = getHeight(), childWidth, childHeight;


        textView = getChildAt(3);
        childWidth = textView.getMeasuredWidth();
        childHeight = textView.getMeasuredHeight();
        textView.layout(width/2 - childWidth/2, height/2- childHeight/2, width/2+childWidth/2, height + childHeight/2);

        int childLeft, childTop, childRight, childBottom;
        childview = getChildAt(2);
        childWidth = childview.getMeasuredWidth();
        childHeight = childview.getMeasuredHeight();

        mCircleRect = new RectF(dis, dis, width - dis, height - dis);

        childLeft = width/2-childWidth/2;
        childTop = height / 2 - childHeight / 2;
        childRight = childLeft + childWidth;
        childBottom = childTop + childHeight;
        childview.layout(childLeft, childTop, childRight, childBottom);



        view_circle = getChildAt(0);
        int cir_width = view_circle.getMeasuredWidth();
        int cir_height = view_circle.getMeasuredHeight();
        int cir1_dis = 3;
        view_circle.layout((width - cir_width) / 2, cir1_dis, (width + cir_width) / 2, cir_height + cir1_dis);
        view_circle.setVisibility(View.INVISIBLE);



        int cir1_dis1 = 1;
        rotate_circle=getChildAt(1);
        int rotate_width = rotate_circle.getMeasuredWidth();
        int rotate_height = rotate_circle.getMeasuredHeight();

        rotate_circle.layout((width - rotate_width) / 2, cir1_dis1, (width + rotate_width) / 2, rotate_height + cir1_dis1);

        centerY = childTop + (childBottom - childTop) / 2;
        centerX = childLeft + (childRight - childLeft) / 2;

        rotate_circle.setPivotX(rotate_width / 2);
        rotate_circle.setPivotY(height / 2 - 2);
        rotate_circle.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        childview.setRotation(angle + rotation);
//        view_circle.setVisibility(rotation == 0 ? View.INVISIBLE : View.VISIBLE);
        if(rotation !=0) rotation=rotation;
        canvas.drawArc(mCircleRect, 270, rotation , false, mArcPaint);

//        rotate_circle.setRotation(rotation);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestDisallowInterceptTouchEvent(true);
//                rotate_circle.setVisibility(View.VISIBLE);
            case MotionEvent.ACTION_MOVE:
                int x = (int) (motionEvent.getX() - centerX);
                int y = (int) (motionEvent.getY() - centerY);
                float PI = (float) (Math.asin(x / Math.sqrt(x * x + y * y)) / Math.PI);
                if (x >= 0 && y < 0) {
                    //  0~ 90
                    rotation = (int) (PI * 180);
                } else if (x >= 0 && y >= 0) {
                    // 90  ~180
                    rotation = (int) ((0.5 - PI) * 180 + 90);
                } else if (x < 0 && y >= 0) {
                    //180~270
                    rotation = (int) (180 - PI * 180);
                } else if (x <= 0 && y < 0) {
                    rotation = (int) (360 + PI * 180);
                }
                break;
            case MotionEvent.ACTION_UP:
                change(rotation);
                requestDisallowInterceptTouchEvent(false);
//                if(rotation==0)  rotate_circle.setVisibility(View.INVISIBLE);
                break;
        }
        invalidate();
        return true;
    }

    private int clockValue = 0;
    private void change(int rotation) {
        if (rotation > 0 && rotation <= 15) {
            this.rotation = 0;
            clockValue = 0;
        } else if (rotation > 15 && rotation <= 45) {
            this.rotation = 30;
            clockValue = 1;
        } else if (rotation > 45 && rotation <= 75) {
            this.rotation = 60;
            clockValue = 2;
        } else if (rotation > 75 && rotation <= 105) {
            this.rotation = 90;
            clockValue = 3;
        } else if (rotation > 105 && rotation < 135) {
            this.rotation = 120;
            clockValue = 4;
        } else if (rotation > 135 && rotation <= 165) {
            this.rotation = 150;
            clockValue = 5;
        } else if (rotation > 165 && rotation <= 195) {
            this.rotation = 180;
            clockValue = 6;
        } else if (rotation > 195 && rotation <= 225) {
            this.rotation = 210;
            clockValue = 7;
        } else if (rotation > 225 && rotation <= 255) {
            this.rotation = 240;
            clockValue = 8;
        } else if (rotation > 255 && rotation <= 285) {
            this.rotation = 270;
            clockValue = 9;
        } else if (rotation > 285 && rotation <= 315) {
            this.rotation = 300;
            clockValue = 10;
        } else if (rotation > 315 && rotation <= 345) {
            this.rotation = 330;
            clockValue = 11;
        } else {
            this.rotation = 360;
            clockValue = 12;
        }
        tvClock.setText(String.format("%02d",clockValue) + ":00");
        if( mCallback != null ){
            mCallback.setClock(clockValue);
        }
    }

    public void setClock(int time){
        if( time > 12 )
            time  = 12;
        if( time < 0 )
            time = 0;
        rotation = time * 30;
        clockValue = time;
        postInvalidate();
    }

    public void setClockValue(long time){
        long h=time/3600;
        long m=(time%3600)/60;
        tvClock.setText(String.format("%02d",h) + ":" +String.format("%02d",m) );
        postInvalidate();
    }

    public interface Callback{
        public void setClock(int clock);
    }



    private Callback mCallback;
    public void  setCallBack(Callback cb){
        mCallback = cb;
    }

}
