package com.mingri.future.airfresh.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mingri.future.airfresh.R;

import mingrifuture.gizlib.code.util.PxUtils;

/**
 * Created by Administrator on 2017/7/7.
 */
public abstract class BaseDialog extends Dialog{
    protected Context context;
    protected Window window;
    public BaseDialog(Context context) {
        super(context, R.style.dialog);
        this.context=context;
        window=getWindow();

        initView();
}

    protected  abstract  void initView();


    protected  View setView(int id){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(id, null);
        window.setContentView(view);
        return view;
    }


    /**
     * 设置Dialog大小  注：需在setContentView后设置才有效果
     * @param width
     * @param height
     */
    protected  void setWindowLayout(int width,int height){
        WindowManager.LayoutParams param=window.getAttributes();
        param.width= PxUtils.dpToPx(width, context);
        param.height=PxUtils.dpToPx(height,context);
        window.setAttributes(param);
    }


}

