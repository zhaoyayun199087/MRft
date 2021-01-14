package com.mingri.future.airfresh.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.bean.ShowFilterLife;
import com.mingri.future.airfresh.bean.ShowMainPage;
import com.mingri.future.airfresh.bean.ShowNetWork;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.util.LogUtils;


public class FunctionFragment extends BaseFragment {
    View mView;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    private ListView listview;
    private modeAdapter adapter;

    private BaseFragment brightFrag, childFrag, furFrag, /*fulzFrag,*/ wifiFrag,uvcFrag, resetFrag, updataFrag, clockFrag, langFrag, zoneChoiseFrag;
    private HcsmFragment hcsmFrag;
    private int firstVisible = Integer.MAX_VALUE / 2 + 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_function, null);
        }
        initView();
        initData();
        ButterKnife.inject(this, mView);
        EventBus.getDefault().register(this);
        return mView;
    }

    private void initView() {
        adapter = new modeAdapter();
        listview = (ListView) mView.findViewById(R.id.listview);
        listview.setAdapter(adapter);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    if (listview.getChildAt(0).getTop() < -60) {
                        listview.setSelection(++firstVisible);
                    } else if (listview.getChildAt(0).getTop() < 0) {
                        listview.setSelection(firstVisible);
                    }
                    listview.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setSelected(firstVisible + 2);
                            showModelFragment(firstVisible + 2);
                        }
                    });
                } else if (scrollState == 1) {
                    if (listview.getChildAt(0).getTop() == 0) {

                        listview.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setSelected(firstVisible + 2);
                            }
                        });
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisible = firstVisibleItem;
            }
        });
    }


    private void initData() {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.fl_container, new ClockFragment()).commitAllowingStateLoss();
//        transaction.add(R.id.fl_container,new LanguageFragment()).commitAllowingStateLoss();
        listview.setSelection(firstVisible );
//        adapter.setSelected(firstVisible);
        LogUtils.d("is run ");
//        showModelFragment(7);
//        showModelFragment(3);
        showModelFragment(firstVisible + 2);
    }

    private void showModelFragment(int i) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        hidenFragment(transaction);
        switch (i % 11) {
            case 0://语言
                if (langFrag == null) {
                    langFrag = new LanguageFragment();
                    transaction.add(R.id.fl_container, langFrag);
                } else {
                    transaction.show(langFrag);
                }
                break;
            case 1://亮度
                if (brightFrag == null) {
                    brightFrag = new BrightFragment();
                    transaction.add(R.id.fl_container, brightFrag);
                } else {
                    transaction.show(brightFrag);
                }
                break;
            case 2://童锁
                if (childFrag == null) {
                    childFrag = new ChirldLockFragment();
                    transaction.add(R.id.fl_container, childFrag);
                } else {
                    transaction.show(childFrag);
                }
                break;
            case 3://wifi
                if (wifiFrag == null) {
                    wifiFrag = new WifiFragment();
                    transaction.add(R.id.fl_container, wifiFrag);
                } else {
                    transaction.show(wifiFrag);
                }
                break;
            case 4://uvc
                if (uvcFrag == null) {
                    uvcFrag = new UVCFragment();
                    transaction.add(R.id.fl_container, uvcFrag);
                } else {
                    transaction.show(uvcFrag);
                }
                break;
           /* case 5://负离子
                if (fulzFrag == null) {
                    fulzFrag = new FlzFragment();
                    transaction.add(R.id.fl_container, fulzFrag);
                } else {
                    transaction.show(fulzFrag);
                }
                break;*/
            case 5://辅热
                if (furFrag == null) {
                    furFrag = new FrFragment();
                    transaction.add(R.id.fl_container, furFrag);
                } else {
                    transaction.show(furFrag);
                }
                break;
            case 6://寿命
                if (hcsmFrag == null) {
                    hcsmFrag = new HcsmFragment();
                    transaction.add(R.id.fl_container, hcsmFrag);
                } else {
                    transaction.show(hcsmFrag);
                }
                break;
            case 7://定时
                if (clockFrag == null) {
                    clockFrag = new ClockFragment();
                    transaction.add(R.id.fl_container, clockFrag);
                } else {
                    transaction.show(clockFrag);
                }
                break;
            case 8://地点设置
                if (zoneChoiseFrag == null) {
                    zoneChoiseFrag = new ZoneChioseFragment();
                    transaction.add(R.id.fl_container, zoneChoiseFrag);
                } else {
                    transaction.show(zoneChoiseFrag);
                }
                break;
            case 9://更新
                if (updataFrag == null) {
                    updataFrag = new UpDataFragment();
                    transaction.add(R.id.fl_container, updataFrag);
                } else {
                    transaction.show(updataFrag);
                }
                break;
            case 10://出厂
                if (resetFrag == null) {
                    resetFrag = new ResetFragment();
                    transaction.add(R.id.fl_container, resetFrag);
                } else {
                    transaction.show(resetFrag);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hidenFragment(FragmentTransaction transaction) {
        if (brightFrag != null) transaction.hide(brightFrag);
        if (childFrag != null) transaction.hide(childFrag);
        if (clockFrag != null) transaction.hide(clockFrag);
/*
        if (fulzFrag != null) transaction.hide(fulzFrag);
*/
        if (uvcFrag != null) transaction.hide(uvcFrag);
        if (wifiFrag != null) transaction.hide(wifiFrag);
        if (furFrag != null) transaction.hide(furFrag);
        if (hcsmFrag != null) transaction.hide(hcsmFrag);
        if (updataFrag != null) transaction.hide(updataFrag);
        if (langFrag != null) transaction.hide(langFrag);
        if (resetFrag != null) transaction.hide(resetFrag);
        if (zoneChoiseFrag != null) transaction.hide(zoneChoiseFrag);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        EventBus.getDefault().post(new ShowMainPage(0));
    }

    class modeAdapter extends BaseAdapter {
        int mPosition = Integer.MAX_VALUE / 2 + 2;

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_model, null);
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            int iPosition = position % 11;
            switch (iPosition) {
                case 0:
                    holder.icon.setImageResource(R.drawable.selector_caid_yuy);
                    holder.name.setText(getResources().getString(R.string.caid_yuy));
                    break;
                case 1:
                    holder.icon.setImageResource(R.drawable.selector_caid_liangd);
                    holder.name.setText(getResources().getString(R.string.caid_liangd));
                    break;
                case 2:
                    holder.icon.setImageResource(R.drawable.selector_caid_tongs);
                    holder.name.setText(getResources().getString(R.string.caid_tongs));
                    break;
                case 3:
                    holder.icon.setImageResource(R.drawable.selector_caid_wifi);
                    holder.name.setText(getResources().getString(R.string.caid_wifi));
                    break;
                case 4:
                    holder.icon.setImageResource(R.drawable.selector_caid_uvc);
                    holder.name.setText(getResources().getString(R.string.caid_uvc));
                    break;
            /*    case 5:
                    holder.icon.setImageResource(R.drawable.selector_caid_fulz);
                    holder.name.setText(getResources().getString(R.string.caid_fulz));
                    break;*/
                case 5:
                    holder.icon.setImageResource(R.drawable.selector_caid_ptc);
                    holder.name.setText(getResources().getString(R.string.caid_ptc));
                    break;
                case 6:
                    holder.icon.setImageResource(R.drawable.selector_caid_shoum);
                    holder.name.setText(getResources().getString(R.string.caid_shoum));
                    break;
                case 7:
                    holder.icon.setImageResource(R.drawable.selector_caid_dings);
                    holder.name.setText(getResources().getString(R.string.caid_dings));
                    break;
                case 8:
                    holder.icon.setImageResource(R.mipmap.location_checked);
                    holder.name.setText(getResources().getString(R.string.zone_chise));
                    break;
                case 9:
                    holder.icon.setImageResource(R.drawable.selector_caid_gengx);
                    holder.name.setText(getResources().getString(R.string.caid_gengx));
                    break;
                case 10:
                    holder.icon.setImageResource(R.drawable.selector_caid_huifcc);
                    holder.name.setText(getResources().getString(R.string.caid_huifcc));
                    break;
            }

            LogUtils.d("position is " + position + "mPo " + mPosition );
            if (position == mPosition) {
//                holder.ll.setBackgroundResource(R.mipmap.caid_bg_xuanz);
                holder.icon.setSelected(true);
                holder.icon.setTranslationX(15);
                holder.name.setTranslationX(15);
                holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            } else {
//                holder.ll.setBackgroundResource(0);
                holder.icon.setSelected(false);
                holder.icon.setTranslationX(0);
                holder.name.setTranslationX(0);
                holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            }
            return convertView;
        }

        public void setSelected(int position) {
            mPosition = position;
            notifyDataSetChanged();
        }

        class ViewHolder {
            LinearLayout ll;
            ImageView icon;
            TextView name;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final ShowNetWork data) {
        showModelFragment(3);
        listview.setSelection(1 + 11 * 100000);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendSerialData(final ShowFilterLife data) {
        showModelFragment(6);
        listview.setSelection(4+ 11 * 100000);
    }
}