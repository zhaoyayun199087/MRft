package com.mingri.future.airfresh.network;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseObersveImp implements Observer<JSONObject> {

    private NetCallback netCallback;

    public BaseObersveImp(NetCallback netCallback){
        this.netCallback = netCallback;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(JSONObject jsonObject) {
        JSONObject data = null;
        String content = null;
        int code = 0;
        try {
            data = jsonObject.getJSONObject("data");
            content = data.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            if( content == null && data != null){
                content = data.toString();
            }else{
                netCallback.Onfailed(e);
                return;
            }
        }catch (Exception e){
            netCallback.Onfailed(e);
            return;
        }
        netCallback.onSucces(code,content);
    }

    @Override
    public void onError(Throwable e) {
        netCallback.Onfailed(e);
    }

    @Override
    public void onComplete() {

    }

    public interface NetCallback{
        public void onSucces(int code, String content);
        public void Onfailed(Throwable e);
    }
}
