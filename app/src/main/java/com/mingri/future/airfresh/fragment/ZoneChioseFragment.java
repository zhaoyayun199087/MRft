package com.mingri.future.airfresh.fragment;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mingri.future.airfresh.R;
import com.mingri.future.airfresh.activity.MainActivity;
import com.mingri.future.airfresh.bean.ReceDataFromMachine;
import com.mingri.future.airfresh.bean.SendDataToMachine;
import com.mingri.future.airfresh.util.CommonUtils;
import com.mingri.future.airfresh.util.CreateCmdToMachineFactory;
import com.mingri.future.airfresh.view.weelchar.widget.OnWheelChangedListener;
import com.mingri.future.airfresh.view.weelchar.widget.WheelView;
import com.mingri.future.airfresh.view.weelchar.widget.adapters.ArrayWheelAdapter;
import com.mingri.future.airfresh.view.weelchar.widget.model.CityModel;
import com.mingri.future.airfresh.view.weelchar.widget.model.DistrictModel;
import com.mingri.future.airfresh.view.weelchar.widget.model.ProvinceModel;
import com.mingri.future.airfresh.view.weelchar.widget.service.XmlParserHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mingrifuture.gizlib.code.config.Constants;
import mingrifuture.gizlib.code.provider.MachineStatusForMrFrture;
import mingrifuture.gizlib.code.util.LogUtils;
import mingrifuture.gizlib.code.util.SPUtils;

/**
 * Created by Administrator on 2017/6/27.
 */
public class ZoneChioseFragment extends BaseFragment implements OnWheelChangedListener {

    WheelView idProvince;
    WheelView idCity;


    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_zone_choise, null);
        idProvince = (WheelView)view.findViewById(R.id.id_province);
        idCity = (WheelView)view.findViewById(R.id.id_city);
        updateUI();
        return view;
    }

    private void updateUI() {
        initProvinceDatas();
        setUpListener();
        setUpData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onResume() {
        super.onResume();
        int province_id = (int) SPUtils.get(getActivity(), "province_id", 0);
        final int city_id = (int) SPUtils.get(getActivity(), "city_id", 0);

        idProvince.setCurrentItem(province_id);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                idCity.setCurrentItem(city_id);
//            }
//        }, 1000);
//        LogUtils.d("zone set get " + province_id + "  " + city_id);
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }



    private void setUpListener() {
        // 添加change事件
        idProvince.addChangingListener(this);
        // 添加change事件
        idCity.addChangingListener(this);
    }

    int provinceId = 0;
    int cityId = 0;
    private void setUpData() {
        initProvinceDatas();
        idProvince.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), mProvinceDatas));
        // 设置可见条目数量
        idProvince.setVisibleItems(3);
        idCity.setVisibleItems(3);
        provinceId = (int) SPUtils.get(getActivity(), "province_id", 0);
        cityId = (int) SPUtils.get(getActivity(), "city_id", 0);

        idProvince.setCurrentItem(provinceId);
        LogUtils.d("city id is " + cityId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("city id is1  " + cityId);
                idCity.setCurrentItem(cityId);
            }
        }, 1000);

        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == idProvince) {
            updateCities();
        } else if (wheel == idCity) {
            updateAreas();
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = idCity.getCurrentItem();
        LogUtils.d("zone set set city " + pCurrent);
        SPUtils.put(getActivity(), "city_id", pCurrent);
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        SPUtils.put(getActivity(), "city_name", mCurrentCityName);
        MachineStatusForMrFrture.bUpdateOutDate = true;
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = idProvince.getCurrentItem();
        LogUtils.d("zone set set province " + pCurrent);
        SPUtils.put(getActivity(), "province_id", pCurrent);
        mCurrentProviceName = mProvinceDatas[pCurrent];
        SPUtils.put(getActivity(), "province_name", mCurrentProviceName);
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        idCity.setViewAdapter(new ArrayWheelAdapter<String>(getActivity(), cities));
        if( cities.length > cityId){
            idCity.setCurrentItem(cityId);
        }else {
            idCity.setCurrentItem(0);
        }
        updateAreas();
    }


}
