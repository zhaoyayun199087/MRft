package com.bakerj.infinitecards.transformer;

import android.util.Log;
import android.view.View;

import com.bakerj.infinitecards.AnimationTransformer;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author BakerJ
 */
public class DefaultCommonTransformer implements AnimationTransformer {
    @Override
    public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
        if(toPosition<fromPosition){
            fraction =1f;
        }else{
            if(fraction !=1f)return;
        }
            int positionCount = toPosition - fromPosition;
            toPosition= toPosition >=2  ? 2:toPosition;
//           float scale = (0.7f + 0.075f *fromPosition ) + (0.075f * fraction * positionCount);
           float scale = (0.7f + 0.075f * toPosition *fraction);
           float scale1= (0.7f - 0.075f * toPosition * fraction);
            ViewHelper.setScaleX(view, scale);
            ViewHelper.setScaleY(view, scale1);

            view.setAlpha(1f - (0.3f * toPosition)*fraction);
//          ViewHelper.setTranslationY(view, -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
//                fromPosition - 0.02f * fraction * positionCount));

    }

    @Override
    public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

    }
}
