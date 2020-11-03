package com.mingri.future.airfresh.view.dialog;

import android.content.Context;
import android.view.View;

import com.mingri.future.airfresh.R;

/**
 * Created by Administrator on 2017/7/7.
 */
public class AlreadyNewVersionDialog extends BaseDialog {
    public AlreadyNewVersionDialog(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        View v=setView(R.layout.dialog_already_new_version);
        setWindowLayout(256,208);
    }
}
