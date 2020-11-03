package com.mingri.future.airfresh.view.dialog;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.mingri.future.airfresh.R;

/**
 * 升级检测Dialog
 * Created by Administrator on 2017/7/6.
 */
public class DetectUpdataDialog extends BaseDialog {
    private ImageView load;

    public DetectUpdataDialog(Context context) {
        super(context);
    }

    protected void initView() {
        View view= setView(R.layout.dialog_detect_updata);
        setWindowLayout( 256, 208);

        load = (ImageView) view.findViewById(R.id.iv_load);
        ValueAnimator animator = ObjectAnimator.ofFloat(load, "rotation", 0f, 360f);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();

        setCancelable(false);
    }

}
