package com.mingri.future.airfresh.network.converter;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by x on 17-6-7.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody,T> {

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return (T) new JSONObject(value.string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
