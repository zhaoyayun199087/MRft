package com.mingri.future.airfresh.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class BlurFrameLayout extends FrameLayout {
    private BlurCalculate mBlurCalculate;

    public BlurFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mBlurCalculate = new BlurCalculate(this);
    }

    public BlurFrameLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    /***
     * radius for linearlayout
     ***/
    public void setRadius(int arg0) {
        if (mBlurCalculate != null)
            mBlurCalculate.setRadius(arg0);
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        mBlurCalculate.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        mBlurCalculate.BluronDetachedFromWindow();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (mBlurCalculate.isCanvasChanged(canvas))
            mBlurCalculate.BlurCanvas();
        else {
            mBlurCalculate.DrawCanvas(canvas);
            super.dispatchDraw(canvas);

        }
    }

    public static class BlurCalculate {
        private View mView;
        private Bitmap bitmap;
        private Canvas mCanvas;
        private Rect mRect;
        private Matrix mMatrix;
        private Matrix mDrawMatrix;
        private int realheight,realwidth;
        // rs
        private RenderScript rs;
        private Allocation input;
        private Allocation output;
        private ScriptIntrinsicBlur script;
        private int radius=20;
        int i=-1;
        private int action=0;
        private static final float BITMAP_RATIO=0.1f;
        public BlurCalculate(View view) {
            this.mView = view;
            rs = RenderScript.create(view.getContext());
            mCanvas=new Canvas();
            mRect=new Rect();
            mMatrix=new Matrix();
            mDrawMatrix=new Matrix();

            //mDrawMatrix.setScale(10.0f, 10.0f);
        }
        public void setaction(int action)
        {
            this.action=action;
        }
        public boolean isCanvasChanged(Canvas canvas)
        {
            return canvas==mCanvas;
        }
        public void onAttachedToWindow() {
            mView.getViewTreeObserver().addOnPreDrawListener(onPreDrawListener);
        }

        public void BluronDetachedFromWindow() {
            mView.getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
            if(bitmap!=null)
                bitmap.recycle();
            bitmap=null;
        }
        public void DrawCanvas(Canvas canvas)
        {
            if(bitmap!=null)
                canvas.drawBitmap(bitmap, mDrawMatrix, null);

        }
        public void BlurCanvas()
        {
            input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            output = Allocation.createTyped(rs, input.getType());
            script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
        }
        public void setRadius(int arg0)
        {
            radius=arg0;
        }
        private void getScreenBitmap()
        {
            mView.getGlobalVisibleRect(mRect);
            realheight=mView.getHeight();
            realwidth=mView.getWidth();
            int w=Math.round(realwidth*BITMAP_RATIO)+4;
            int h=Math.round(realheight*BITMAP_RATIO)+(action==0?-4:4);
            w = w & ~0x03;
            h = h & ~0x03;
            if(w<=0||h<=0)
                return;
            if (bitmap == null || bitmap.getWidth() != w || bitmap.getHeight() != h) {
                bitmap=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                mMatrix.setScale(BITMAP_RATIO, BITMAP_RATIO);
                mMatrix.invert(mDrawMatrix);
            }
            //(mView.getBottom()-mView.getTop())
            float dx = -(Math.min(0, mView.getLeft()) + mRect.left);
            float dy = action==0?(-(Math.min(0, mView.getTop()) + mRect.top)):-(mRect.bottom-((mView.getBottom()-mView.getTop())));
            mCanvas.restoreToCount(1);
            mCanvas.setBitmap(bitmap);
            mCanvas.setMatrix(mMatrix);
            mCanvas.translate(dx, dy);
            mCanvas.save();
            mView.getRootView().draw(mCanvas);
            //mCanvas.setBitmap(null);

        }
        private final ViewTreeObserver.OnPreDrawListener onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mView.getVisibility() == VISIBLE)
                    getScreenBitmap();
                return true;
            }
        };
    }
}