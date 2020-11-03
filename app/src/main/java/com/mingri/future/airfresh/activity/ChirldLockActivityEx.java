package com.mingri.future.airfresh.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.UnLockChilrdLock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;

/**
 * Created by Administrator on 2017/6/21.
 */
public class ChirldLockActivityEx extends Activity {
    @InjectView(R.id.tv_a)
    TextView tvA;
    @InjectView(R.id.tv_b)
    TextView tvB;
    @InjectView(R.id.tv_c)
    TextView tvC;
    @InjectView(R.id.tv_d)
    TextView tvD;
    @InjectView(R.id.tv_e)
    TextView tvE;
    @InjectView(R.id.tv_f)
    TextView tvF;
    @InjectView(R.id.tv_g)
    TextView tvG;
    @InjectView(R.id.tv_h)
    TextView tvH;
    @InjectView(R.id.tv_i)
    TextView tvI;
    @InjectView(R.id.tv_j)
    TextView tvJ;
    @InjectView(R.id.tv_k)
    TextView tvK;
    @InjectView(R.id.tv_l)
    TextView tvL;
    @InjectView(R.id.tv_error)
    TextView tvError;
    @InjectView(R.id.ll_root)
    LinearLayout llRoot;
    private StringBuffer buffer = new StringBuffer();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_chirld_lock_ex);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        tvError.setVisibility(View.INVISIBLE);

        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String ramStr = getResources().getString(R.string.chilrd_lock_page_all);
        int len = ramStr.length();
        int ramdom = new Random().nextInt(len) - 1;
        if( ramdom < 0 )ramdom = 0;
        List<String> lvTip = new ArrayList<String>();
        while(lvTip.size() < 12){
            String s = ramStr.substring(ramdom,ramdom + 1);
            LogUtils.d("ramdom str  is "+ s );
            if( lvTip.contains(s) ){
                ramdom = new Random().nextInt(len) - 1;
                if( ramdom < 0 )ramdom = 0;
                continue;
            }
            ramdom = new Random().nextInt(len) - 1;
            if( ramdom < 0 )ramdom = 0;
            lvTip.add(s);
        }

        int[] arr = random(4, 11);
        for( int s: arr ){
            LogUtils.d( "" + s);
        }

        lvTip.set(arr[0], getResources().getString(R.string.chilrd_lock_page_e));
        lvTip.set(arr[1], getResources().getString(R.string.chilrd_lock_page_f));
        lvTip.set(arr[2], getResources().getString(R.string.chilrd_lock_page_l));
        lvTip.set(arr[3], getResources().getString(R.string.chilrd_lock_page_a));

        LogUtils.d("lv tips is " );
        for( String s: lvTip ){
            LogUtils.d(s);
        }

        tvA.setText(lvTip.get(0));
        tvB.setText(lvTip.get(1));
        tvC.setText(lvTip.get(2));
        tvD.setText(lvTip.get(3));
        tvE.setText(lvTip.get(4));
        tvF.setText(lvTip.get(5));
        tvG.setText(lvTip.get(6));
        tvH.setText(lvTip.get(7));
        tvI.setText(lvTip.get(8));
        tvJ.setText(lvTip.get(9));
        tvK.setText(lvTip.get(10));
        tvL.setText(lvTip.get(11));

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MachineStatusForMrFrture.iCount = 0;
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 随机生成n个不同的数
     *
     * @param amount
     *            需要的数量
     * @param max
     *            最大值(不含)，例：max为100，则100不能取到，范围为0~99；
     * @return 数组
     */
    private int[] random(int amount, int max) {

        if (amount > max) { // 需要数字总数必须小于数的最大值，以免死循环！
            throw new ArrayStoreException(
                    "The amount of array element must smallar than the maximum value !");
        }
        int[] array = new int[amount];
        for (int i = 0; i < array.length; i++) {
            array[i] = -1; // 初始化数组，避免后面比对时数组内不能含有0。
        }
        Random random = new Random();
        int num;
        amount -= 1; // 数组下标比数组长度小1
        while (amount >= 0) {
            num = random.nextInt(max);
            if (exist(num, array, amount - 1)) {
                continue;
            }
            array[amount] = num;
            amount--;
        }
        return array;
    }

    /**
     * 判断随机的数字是否存在数组中
     *
     * @param num
     *            随机生成的数
     * @param array
     *            判断的数组
     * @param need
     *            还需要的个数
     * @return 存在true，不存在false
     */
    private boolean exist(int num, int[] array, int need) {

        for (int i = array.length - 1; i > need; i--) {// 大于need用于减少循环次数，提高效率。
            if (num == array[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * 随机生成一个数
     *
     * @param max
     *            最大值(不含)
     * @return 整型数
     */
    private int random(int max) {

        return random(1, max)[0];
    }

    private void detectSecret() {
        LogUtils.d("detect secret is " + buffer.toString());
        if (buffer.length() == 4) {
            if (buffer.toString().equals("未来先生")) {
                buffer.delete(0, buffer.length());
                MachineStatusForMrFrture.Child_Security_Lock = false;
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                tvError.setVisibility(View.VISIBLE);
                buffer.delete(0, buffer.length());
                disableTv();
                tvError.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvError.setVisibility(View.INVISIBLE);
                        buffer.delete(0, buffer.length());
                        enableTv();
                        cleanBg();
                    }
                }, 3000);
            }
        } else if (buffer.length() > 4) {
            tvError.setVisibility(View.VISIBLE);
            buffer.delete(0, buffer.length());
            tvError.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvError.setVisibility(View.INVISIBLE);
                    buffer.delete(0, buffer.length());
                }
            }, 3000);
        }
    }

    private void enableTv() {
        tvE.setEnabled(true);
        tvF.setEnabled(true);
        tvL.setEnabled(true);
        tvA.setEnabled(true);
        tvB.setEnabled(true);
        tvC.setEnabled(true);
        tvD.setEnabled(true);
        tvG.setEnabled(true);
        tvH.setEnabled(true);
        tvI.setEnabled(true);
        tvJ.setEnabled(true);
        tvK.setEnabled(true);
    }

    private void disableTv() {
        tvE.setEnabled(false);
        tvF.setEnabled(false);
        tvL.setEnabled(false);
        tvA.setEnabled(false);
        tvB.setEnabled(false);
        tvC.setEnabled(false);
        tvD.setEnabled(false);
        tvG.setEnabled(false);
        tvH.setEnabled(false);
        tvI.setEnabled(false);
        tvJ.setEnabled(false);
        tvK.setEnabled(false);
    }

    private void cleanBg() {
        tvE.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvF.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvL.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvA.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvB.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvC.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvD.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvG.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvH.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvI.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvJ.setBackgroundResource(R.mipmap.btn_jiesuo_n);
        tvK.setBackgroundResource(R.mipmap.btn_jiesuo_n);
    }

    @OnClick({R.id.tv_a, R.id.tv_b, R.id.tv_c, R.id.tv_d, R.id.tv_e, R.id.tv_f, R.id.tv_g, R.id.tv_h, R.id.tv_i, R.id.tv_j, R.id.tv_k, R.id.tv_l, R.id.tv_error, R.id.ll_root})
    public void onViewClicked(View view) {
        TextView tv = (TextView) view;
        buffer.append(tv.getText());
        LogUtils.d("wenzi is " + tv.getText());
        switch (view.getId()) {

            case R.id.tv_e:
                tvE.setBackgroundResource(R.mipmap.btn_jiesuo_p);
                break;
            case R.id.tv_f:
                tvF.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_l:
                tvL.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_a:
                tvA.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_b:
                tvB.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_c:
                tvC.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_d:
                tvD.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_g:
                tvG.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_h:
                tvH.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_i:
                tvI.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_j:
                tvJ.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
            case R.id.tv_k:
                tvK.setBackgroundResource(R.mipmap.btn_jiesuo_p);

                break;
        }
        detectSecret();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final UnLockChilrdLock data) {
        MachineStatusForMrFrture.Child_Security_Lock = false;
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
