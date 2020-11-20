package com.mingri.future.airfresh.network;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APIService {


    //城市id
    @GET("/v2/city/lookup")
    Observable<JSONObject> getCityId(@QueryMap Map<String, String> map);


    //天气数据
    @GET("/v7/weather/now")
    Observable<JSONObject> getWeatherDate(@QueryMap Map<String, String> map);


    //空气质量
    @GET("/v7/air/now")
    Observable<JSONObject> getAirQulity(@QueryMap Map<String, String> map);

}
