package com.bakerj.infinitecards;

import android.view.View;

/**
 * @author BakerJ
 */
public class CardItem {
    public View view;
    public float zIndex;
    int adapterIndex;
    public int index;

    CardItem(View view, float zIndex, int adapterIndex,int index) {
        this.view = view;
        this.zIndex = zIndex;
        this.adapterIndex = adapterIndex;
        this.index=index;
    }
    @Override
    public int hashCode() {
        return view.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CardItem && view.equals(((CardItem) obj).view);
    }
}
