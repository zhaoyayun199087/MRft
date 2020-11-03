package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bakerj.infinitecards.AnimationTransformer;
import com.bakerj.infinitecards.CardItem;
import com.bakerj.infinitecards.InfiniteCardView;
import com.bakerj.infinitecards.ZIndexTransformer;
import com.bakerj.infinitecards.transformer.DefaultCommonTransformer;
import com.bakerj.infinitecards.transformer.DefaultTransformerToBack;
import com.bakerj.infinitecards.transformer.DefaultZIndexTransformerCommon;
import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.application.InitApplication;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.ReceiverHcZero;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.view.CircleProgress;
import com.mingri.future.airfresh.view.dialog.ResetDialog;
import com.nineoldandroids.view.ViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;

import butterknife.InjectView;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;


/**
 * Created by Administrator on 2017/6/27.
 */
public class HcsmFragment extends BaseFragment {
    private InfiniteCardView cardView;
    private RelativeLayout rlReset;
    private MyAdatper adatper;
    //索引，
    private int mIndex;
    private GestureDetector detector;
    private ImageView indec1, indec2, indec3, indec4, indec5;
    private boolean bUpdate = true;
    private boolean isFirst=false;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setShowZeroView(msg.what);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_hcsm, container, false);
        initView(v);
        initData();
        EventBus.getDefault().register(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View v) {
        cardView = (InfiniteCardView) v.findViewById(R.id.card_view);
        indec1 = (ImageView) v.findViewById(R.id.iv_indect1);
        indec2 = (ImageView) v.findViewById(R.id.iv_indect2);
        indec3 = (ImageView) v.findViewById(R.id.iv_indect3);
        indec4 = (ImageView) v.findViewById(R.id.iv_indect4);
        indec5 = (ImageView) v.findViewById(R.id.iv_indect5);
        rlReset = (RelativeLayout) v.findViewById(R.id.rl_reset);
        mIndex = 4;
    }


    private void initData() {
        adatper = new MyAdatper();
        cardView.setClickable(true);
        cardView.setAdapter(adatper);
        cardView.setCardAnimationListener(new InfiniteCardView.CardAnimationListener() {
            @Override
            public void onAnimationStart() {
                Log.e("cjh", "  start  " + cardView.getCardFront().index);

                initIndec();
            }

            @Override
            public void onAnimationEnd() {
                Log.e("cjh", "  end  " + cardView.getCardFront().index);
                // 4:出校 0:中校 1:活性 2:高效 3:杀菌
                setIndec(cardView.getCardFront().index);
                mIndex = cardView.getCardFront().index;
            }
        });

        //手势识别
        detector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityX > 700) {
                    frontToBack(1);
                } else if (velocityX < -700) {
                    backToFront(adatper.getCount() - 1);
                }
                return false;
            }
        });


        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });

        rlReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetDialog rd = new ResetDialog(getActivity(), new ResetDialog.ResetDialogCallback() {
                    @Override
                    public void onOk() {
                        // 4:出校 0:中校 1:活性 2:高效 3:杀菌
                        if (mIndex == 4) {
                            MachineStatusForMrFrture.Filter_Life1 = 100;
                            CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_CHU, MachineStatusForMrFrture.Filter_Life1);

                            int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_CHU);
                            EventBus.getDefault().post(new SendDataToMachine(filter11));
                        } else if (mIndex == 0) {
                            MachineStatusForMrFrture.Filter_Life2 = 100;
                            CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_ZHONG, MachineStatusForMrFrture.Filter_Life2);

                            int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_ZHONG);
                            EventBus.getDefault().post(new SendDataToMachine(filter11));
                        } else if (mIndex == 1) {
                            MachineStatusForMrFrture.Filter_Life3 = 100;
                            CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_HUOXING, MachineStatusForMrFrture.Filter_Life3);

                            int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_HUOXING);
                            EventBus.getDefault().post(new SendDataToMachine(filter11));
                        } else if (mIndex == 2) {
                            MachineStatusForMrFrture.Filter_Life4 = 100;
                            CommonUtils.setOrder(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO, MachineStatusForMrFrture.Filter_Life4);

                            int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_LEFTTIME_GAOXIAO);
                            EventBus.getDefault().post(new SendDataToMachine(filter11));
                        } else if (mIndex == 3) {
                            MachineStatusForMrFrture.UVC_Life = 100;
                            CommonUtils.setOrder(Constants.ANDROID_SEND_UVC, MachineStatusForMrFrture.UVC_Life);

                            int[] filter11 = CreateCmdToMachineFactory.createControlCmd(Constants.ANDROID_SEND_UVC);
                            EventBus.getDefault().post(new SendDataToMachine(filter11));
                        }
                        adatper.notifyDataSetChanged();
                        bUpdate = false;
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                rd.show();
            }
        });

       cardView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
           @Override
           public void onGlobalLayout() {
               cardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
               int i=0;
               if(!InitApplication.isChuxiao)i++;
               if(!InitApplication.isZhongxiao)i++;
               if(!InitApplication.isHuoxing)i++;
               if(!InitApplication.isGaoxiao)i++;
               if(!InitApplication.isUVC)i++;
               if(InitApplication.isJumpZeroView && i>1){
                   cardView.postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Log.e("cjh"," mindex="+mIndex+"  " );
                           setShowZeroView(InitApplication.JumpZeroIndex);
                           isFirst=true;
                       }
                   },500);
               }
           }
       });
    }


    private class MyAdatper extends BaseAdapter {

        MyAdatper() {
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lvwang, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv_des);
            CircleProgress circle = (CircleProgress) convertView.findViewById(R.id.v_circle);
            TextView percent = (TextView) convertView.findViewById(R.id.text_view);

            LogUtils.d("getview " + position);
            switch (position) {
                // 4:出校 0:中校 1:活性 2:高效 3:杀菌
                case 0:
                    tv.setText(getResources().getString(R.string.lvwang_chuxiao));
                    circle.setPercent(MachineStatusForMrFrture.Filter_Life1);
                    break;
                case 1:
                    tv.setText(getResources().getString(R.string.lvwang_zhongxiao));
                    circle.setPercent(MachineStatusForMrFrture.Filter_Life2);
                    break;
                case 2:
                    tv.setText(getResources().getString(R.string.lvwang_huoxing));
                    circle.setPercent(MachineStatusForMrFrture.Filter_Life3);
                    break;
                case 3:
                    tv.setText(getResources().getString(R.string.lvwang_gaoxiao));
                    circle.setPercent(MachineStatusForMrFrture.Filter_Life4);
                    break;
                case 4:
                    tv.setText(getResources().getString(R.string.lvwang_shajun));
                    circle.setPercent(MachineStatusForMrFrture.UVC_Life);
                    break;
            }
            percent.setText(circle.getPercen() + "");
            return convertView;
        }
    }


    /**
     * 卡片置前
     */
    private void backToFront(int index) {
        cardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT);
        cardView.setAnimInterpolator(new LinearInterpolator());
        cardView.setTransformerToFront(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                ViewHelper.setScaleX(view, (float) (0.7 - (0.075f * toPosition)));
                ViewHelper.setScaleY(view, (float) (0.7 - (0.075f * toPosition)));
                view.setAlpha(1);
                if (fraction < 0.5) {
                    ViewCompat.setTranslationX(view, -cardWidth * fraction * 1.5f);
                    ViewCompat.setRotationY(view, 45 * fraction);
                } else {
                    ViewCompat.setTranslationX(view, -cardWidth * 1.5f * (1f - fraction));
                    ViewCompat.setRotationY(view, 45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
            }
        });

        cardView.setTransformerToBack(new DefaultTransformerToBack());

        cardView.setZIndexTransformerToBack(new DefaultZIndexTransformerCommon());

        cardView.bringCardToFront(index);
    }

    /**
     * 卡片置后
     */
    private void frontToBack(int index) {
        cardView.setAnimType(InfiniteCardView.ANIM_TYPE_FRONT_TO_LAST);
        cardView.setAnimInterpolator(new OvershootInterpolator(-8));
        cardView.setTransformerToFront(new DefaultCommonTransformer());
        cardView.setTransformerToBack(new AnimationTransformer() {
            @Override
            public void transformAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                int positionCount = fromPosition - toPosition;
                float scale = (0.7f + 0.075f * fromPosition) + (0.1f * fraction * positionCount);
                ViewHelper.setScaleX(view, scale);
                ViewHelper.setScaleY(view, (float) (0.7 - (0.075f * fromPosition)));
                view.setAlpha(1);
                if (fraction < 0.5) {
                    ViewCompat.setTranslationX(view, cardWidth * fraction * 1.5f);
                    ViewCompat.setRotationY(view, -45 * fraction);
                } else {
                    ViewCompat.setTranslationX(view, cardWidth * 1.5f * (1f - fraction));
                    ViewCompat.setRotationY(view, -45 * (1 - fraction));
                }
            }

            @Override
            public void transformInterpolatedAnimation(View view, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
            }
        });
        cardView.setZIndexTransformerToBack(new ZIndexTransformer() {
            @Override
            public void transformAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {
                if (fraction < 0.5f) {
                    card.zIndex = 1f + 0.01f * fromPosition;
                } else {
                    card.zIndex = 1f + 0.01f * toPosition;
                }
            }

            @Override
            public void transformInterpolatedAnimation(CardItem card, float fraction, int cardWidth, int cardHeight, int fromPosition, int toPosition) {

            }
        });
        cardView.bringCardToFront(index);
    }


    private void initIndec() {
        indec1.setImageResource(R.mipmap.icon_dian2);
        indec2.setImageResource(R.mipmap.icon_dian2);
        indec3.setImageResource(R.mipmap.icon_dian2);
        indec4.setImageResource(R.mipmap.icon_dian2);
        indec5.setImageResource(R.mipmap.icon_dian2);
    }

    private void setIndec(int i) {
        switch (i) {
            case 4:
                indec1.setImageResource(R.mipmap.icon_dian1);
                break;
            case 0:
                indec2.setImageResource(R.mipmap.icon_dian1);
                break;
            case 1:
                indec3.setImageResource(R.mipmap.icon_dian1);
                break;
            case 2:
                indec4.setImageResource(R.mipmap.icon_dian1);
                break;
            case 3:
                indec5.setImageResource(R.mipmap.icon_dian1);
                break;
        }
    }

    private int iCount = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(ReceDataFromMachine data) {
        LogUtils.d("filter " + MachineStatusForMrFrture.Filter_Life1 + " " + MachineStatusForMrFrture.Filter_Life2 + " " + MachineStatusForMrFrture.Filter_Life3 + " " + MachineStatusForMrFrture.Filter_Life4);
        if (!bUpdate) {
            iCount++;
            if (iCount > 3) {
                bUpdate = true;
            }
        }
        iCount = 0;
        if (bUpdate)
            adatper.notifyDataSetChanged();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverZero(final ReceiverHcZero zero){
        Log.e("cjh"," 是否  判断 "+zero.getIndex()+"  "+InitApplication.isChuxiao+" "+InitApplication.isZhongxiao+"  "
                +InitApplication.isHuoxing+"  "+InitApplication.isGaoxiao+"  "+InitApplication.isUVC);

        if(InitApplication.isChuxiao && InitApplication.isZhongxiao&&InitApplication.isHuoxing&&InitApplication.isGaoxiao&&InitApplication.isUVC)
//        Log.e("cjh","事件 "+zero.getIndex()+"  是否"+cardView.isAnimating() +"  "+InitApplication.isChuxiao+" "+InitApplication.isZhongxiao+"  "
//        +InitApplication.isHuoxing+"  "+InitApplication.isGaoxiao+"  "+InitApplication.isUVC);

        if(cardView.isAnimating()){
            handler.removeCallbacksAndMessages(null);
            if(!isFirst){
                handler.sendEmptyMessageDelayed(zero.getIndex(),500);
            }else{
                handler.sendEmptyMessageDelayed(zero.getIndex(),500);
            }
        }else{
            handler.sendEmptyMessageDelayed(zero.getIndex(),0);
        }



//        cardView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setShowZeroView(zero.getIndex());
//            }
//        },1000);
    }

    int delay=600;
    /**
     * 显示耗材为零的界面
     * 4:出校 0:中校 1:活性 2:高效 3:杀菌
     */
    public void setShowZeroView(int i) {
        Log.e("cjh","   事件2  "+mIndex);
        if (mIndex == 4) {
            /*****/
            switch (i) {
                case 0:
                    frontToBack(1);
                    break;
                case 1:
                    frontToBack(1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frontToBack(1);
                        }
                    }, delay);
                    break;
                case 2:
                    backToFront(adatper.getCount() - 1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToFront(adatper.getCount() - 1);
                        }
                    }, delay);
                    break;
                case 3:
                    backToFront(adatper.getCount() - 1);
                    break;
            }

        } else if (mIndex == 0) {
            /*****/
            switch (i) {
                case 1:
                    frontToBack(1);
                    break;
                case 2:
                    frontToBack(1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frontToBack(1);
                        }
                    }, delay);
                    break;
                case 3:
                    backToFront(adatper.getCount() - 1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToFront(adatper.getCount() - 1);
                        }
                    }, delay);
                    break;
                case 4:
                    backToFront(adatper.getCount() - 1);
                    break;
            }

        } else if (mIndex == 1) {
            /*****/
            switch (i) {
                case 2:
                    frontToBack(1);
                    break;
                case 3:
                    frontToBack(1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frontToBack(1);
                        }
                    }, delay);
                    break;
                case 4:
                    backToFront(adatper.getCount() - 1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToFront(adatper.getCount() - 1);
                        }
                    }, delay);
                    break;
                case 0:
                    backToFront(adatper.getCount() - 1);
                    break;
            }
        } else if (mIndex == 2) {
            /*****/
            switch (i) {
                case 3:
                    frontToBack(1);
                    break;
                case 4:
                    frontToBack(1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frontToBack(1);
                        }
                    }, delay);
                    break;
                case 0:
                    backToFront(adatper.getCount() - 1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToFront(adatper.getCount() - 1);
                        }
                    }, delay);
                    break;
                case 1:
                    backToFront(adatper.getCount() - 1);
                    break;
            }
        } else if (mIndex == 3) {
            /*****/
            switch (i) {
                case 4:
                    frontToBack(1);
                    break;
                case 0:
                    frontToBack(1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            frontToBack(1);
                        }
                    }, delay);
                    break;
                case 1:
                    backToFront(adatper.getCount() - 1);
                    cardView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            backToFront(adatper.getCount() - 1);
                        }
                    }, delay);
                    break;
                case 2:
                    backToFront(adatper.getCount() - 1);
                    break;
            }
        }
    }
}
