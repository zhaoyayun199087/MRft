package com.bakerj.infinitecards.transformer;

import android.util.Log;
import android.view.View;

import com.bakerj.infinitecards.AnimationTransformer;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author BakerJ
 * @date 2017/3/22
 */
public class DefaultTransformerAdd implements AnimationTransformer {
    @Override
    public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
//        fromPosition = toPosition + 1;
       fromPosition= fromPosition>=2 ? 2:fromPosition;
        int positionCount = fromPosition - toPosition;
//        Log.e("ccc","transformAnimation"+"   positionCount="+positionCount);
        float scale = (0.7f + 0.075f *fromPosition ) + (0.1f * fraction * positionCount);
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, (float) (0.7-(0.075f *fromPosition)));
//        ViewHelper.setTranslationY(view, -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
//                fromPosition - 0.02f * fraction * positionCount));
//        ViewHelper.setAlpha(view, fraction);
    }

    @Override
    public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
    }
}
