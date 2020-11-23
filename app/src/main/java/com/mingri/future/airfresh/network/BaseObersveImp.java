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
        netCallback.onSucces(jsonObject);

    }

    @Override
    public void onError(Throwable e) {
        netCallback.Onfailed(e);
    }

    @Override
    public void onComplete() {

    }

    public interface NetCallback{
        public void Onfailed(Throwable e);

        void onSucces(JSONObject jsonObject);
    }
}
