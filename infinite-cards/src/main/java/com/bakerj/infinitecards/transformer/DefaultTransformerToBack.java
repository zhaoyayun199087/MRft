package com.bakerj.infinitecards.transformer;

import android.util.Log;
import android.view.View;

import com.bakerj.infinitecards.AnimationTransformer;
import com.nineoldandroids.view.ViewHelper;

/**
 * @author BakerJ
 */
public class DefaultTransformerToBack implements AnimationTransformer {
    @Override
    public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight,
                                   int fromPosition, int toPosition) {
//        Log.e("DefaultTransformerToBack","fromPosition="+fromPosition+"    toPosition="+toPosition);
        int positionCount = fromPosition - toPosition;
        fromPosition=fromPosition<=2?  2:fromPosition;
        float scale = (0.7f + 0.075f *fromPosition ) ;
        ViewHelper.setScaleX(view, scale);
        ViewHelper.setScaleY(view, (float) (0.7-(0.075f *fromPosition)));
//        ViewHelper.setTranslationY(view, -cardHeight * (0.8f - scale) * 0.5f - cardWidth * (0.02f *
//                fromPosition - 0.02f * fraction * positionCount));
    }

    @Override
    public void transformInterpolatedAnimation(View view, float fraction, int cardWidth,
                                               int cardHeight, int fromPosition, int toPosition) {
    }
}
