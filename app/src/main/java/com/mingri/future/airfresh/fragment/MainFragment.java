package com.mingri.future.airfresh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.activity.DetailParaActivity;
import com.mingri.future.airfresh.activity.ModeSettingActivity;
import com.mingri.future.airfresh.activity.TestActivity;
import com.mingri.future.airfresh.activity.TestUpdataActivity;
import com.mingri.future.airfresh.bean.ChirldLock;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.ShowNetWork;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragOne;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragOneAdapter;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragThree;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragThreeAdapter;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragTwo;
import com.mingri.future.airfresh.view.MainPageData.MainDateFragTwoAdapter;
import com.mingri.future.airfresh.view.MainPageData.VerticalViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.util.LogUtils;


public class MainFragment extends Fragment {

    View mView;
    @InjectView(R.id.vv_inpara)
    VerticalViewPager vvInpara;
    @InjectView(R.id.vv_outpara)
    VerticalViewPager vvOutpara;
    @InjectView(R.id.vv_mode)
    VerticalViewPager vvMode;
    @InjectView(R.id.btn_test)
    Button btnTest;
    @InjectView(R.id.ib_up1)
    ImageButton ibUp1;
    @InjectView(R.id.ib_down1)
    ImageButton ibDown1;
    @InjectView(R.id.ib_up2)
    ImageButton ibUp2;
    @InjectView(R.id.ib_down2)
    ImageButton ibDown2;
    @InjectView(R.id.iv_lock)
    ImageView ivLock;

    private MainDateFragThreeAdapter threeDateFragAdapter;
    private MainDateFragTwoAdapter twoDateFragAdapter;
    private MainDateFragOneAdapter mainDateFragAdapter;
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d("mainfragment oncreate view is " + mView);


        if (mView == null) {
            LogUtils.d("mainfragment oncreate mview");
            mView = inflater.inflate(R.layout.fragment_mainpage, null);
            ButterKnife.inject(this, mView);
            mainDateFragAdapter = new MainDateFragOneAdapter(getActivity().getFragmentManager(), new MainDateFragOne.FragOneClick() {
                @Override
                public void dateClick() {
                    LogUtils.d("One click");
                    Intent startIntent = new Intent(getActivity().getApplicationContext(),
                            DetailParaActivity.class);
                    startIntent.putExtra("index", 1);
                    startActivity(startIntent);
                }
            });
            vvInpara.setAdapter(mainDateFragAdapter);
            vvInpara.setCurrentItem(400);
            vvInpara.setOffscreenPageLimit(4);
            twoDateFragAdapter = new MainDateFragTwoAdapter(getActivity().getFragmentManager(), new MainDateFragTwo.OutParaViewClick() {
                @Override
                public void setWifi() {
                    LogUtils.d("set wifi");
                    EventBus.getDefault().post(new ShowNetWork());
                }

                @Override
                public void dateClick() {
                    LogUtils.d("set date");
                    Intent startIntent = new Intent(getActivity().getApplicationContext(),
                            DetailParaActivity.class);
                    startIntent.putExtra("index", 2);
                    startActivity(startIntent);
                }
            });
            vvOutpara.setAdapter(twoDateFragAdapter);
            vvOutpara.setCurrentItem(400);
            vvOutpara.setOffscreenPageLimit(4);
            threeDateFragAdapter = new MainDateFragThreeAdapter(getActivity().getFragmentManager(), new MainDateFragThree.ModeClick() {
                @Override
                public void callback() {
                    ;
                    LogUtils.d("mode click");
                    startActivity(new Intent(getActivity(), ModeSettingActivity.class));
                }

                @Override
                public void longClick(MotionEvent motionEvent) {
                    if (!isFirst) {
                        return;
                    }
                    if ((motionEvent.getEventTime() - motionEvent.getDownTime()) > 5000) {
                        startActivity(new Intent(getActivity(), TestUpdataActivity.class));
                        isFirst = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isFirst = true;
                            }
                        }, 3000);
                        return;
                    }
                    return;
                }
            });
        vvMode.setAdapter(threeDateFragAdapter);

    } else{
        LogUtils.d("mainfragment oncreate fuyong");
    }

        EventBus.getDefault().

    register(this);
        ButterKnife.inject(this,mView);

//        ibUp2.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                startActivity(new Intent(getActivity(), TestUpdataActivity.class));
//                return false;
//            }
//        });

//        ibUp2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if( !isFirst )
//                    return false;
//                if ((motionEvent.getEventTime() - motionEvent.getDownTime()) > 5000) {
//                    startActivity(new Intent(getActivity(), TestUpdataActivity.class));
//                    isFirst = false;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            isFirst = true;
//                        }
//                    },3000);
//                    return false;
//                }
//                return false;
//            }
//        });

        LogUtils.d("mainfragment oncreate view end");
        return mView;
}

    @Override
    public void onResume() {
        LogUtils.d("mainfragment onresume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("mainfragment destroy");
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
        mView = null;
    }

    @OnClick(R.id.btn_test)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), TestActivity.class));
    }

    @OnClick({R.id.ib_up1, R.id.ib_down1, R.id.ib_up2, R.id.ib_down2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_up1:
                vvOutpara.arrowScroll(2);
                break;

            case R.id.ib_down1:
                vvOutpara.arrowScroll(1);
                break;

            case R.id.ib_up2:
                vvInpara.arrowScroll(2);
                break;

            case R.id.ib_down2:
                vvInpara.arrowScroll(1);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        mainDateFragAdapter.notifyDataSetChanged();
        twoDateFragAdapter.notifyDataSetChanged();
        threeDateFragAdapter.notifyDataSetChanged();
        LogUtils.d("change date");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ChirldLock data) {
        if (data.isLock()) {
            ibDown1.setVisibility(View.INVISIBLE);
            ibUp1.setVisibility(View.INVISIBLE);
            ibUp2.setVisibility(View.INVISIBLE);
            ibDown2.setVisibility(View.INVISIBLE);
            ivLock.setVisibility(View.VISIBLE);
        } else {
            ibDown1.setVisibility(View.VISIBLE);
            ibUp1.setVisibility(View.VISIBLE);
            ibUp2.setVisibility(View.VISIBLE);
            ibDown2.setVisibility(View.VISIBLE);
            ivLock.setVisibility(View.GONE);
        }
    }
}