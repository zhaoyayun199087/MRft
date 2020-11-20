package com.mingri.future.airfresh.network;

import android.content.Context;
import android.util.Log;

import com.mingri.future.airfresh.network.converter.JsonConverterFactory;

import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import mingrifuture.gizlib.code.util.LogUtils;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by x on 17-9-26.
 */

public class RetrofitFactory {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final int THREAD_NUM = 3;
    private static final int CONN_TIMEOUT = 60;
    private static final String TAG = "RetrofitFactory";

    private static Retrofit retrofit = null;
    private static RetrofitFactory instance;


    private Context mContext;
    private String urlRequest;
    private int code;

    public static String getBaseUrl(Context context) {
            return "https://devapi.qweather.com";
    }


    public RetrofitFactory(Context context) {
        mContext = context.getApplicationContext();
        init(mContext, getBaseUrl(mContext));
    }

    public static RetrofitFactory getInstance(Context context) {
        if (instance == null) {
            synchronized (RetrofitFactory.class) {
                if (instance == null) {
                    instance = new RetrofitFactory(context);
                }
            }
        }
        return instance;
    }

    private String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            if (copy == null) {
                return "";
            }
            final Buffer buffer = new Buffer();

            copy.body().writeTo(buffer);
            String ret = buffer.readUtf8();
            return ret;
        } catch (final Exception e) {
            return "";
        }
    }

    private Retrofit init(final Context context, String baseUrl) {

        if (retrofit != null)
            return retrofit;

        LogUtils.d("RetrofitFactory init");
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(THREAD_NUM));
        dispatcher.setMaxRequests(THREAD_NUM);
        dispatcher.setMaxRequestsPerHost(1);


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //set up Okhttpclient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .addInterceptor(loggingInterceptor)
                .connectionPool(new ConnectionPool(THREAD_NUM, CONN_TIMEOUT, TimeUnit.SECONDS))
                .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS).
                readTimeout(CONN_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public <T> T createService(Class<T> reqServer) {
        return retrofit.create(reqServer);
    }

    public final String requestUrlString() {
        if (urlRequest == null) {
            throw new IllegalArgumentException("http request url coult be null.");
        }
        return urlRequest;
    }

    public final int responseCode() {
        return code;
    }

}
