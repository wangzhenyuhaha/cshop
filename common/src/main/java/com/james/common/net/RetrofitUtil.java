package com.james.common.net;


import com.blankj.utilcode.util.AppUtils;
import com.james.common.netcore.networking.http.core.HiConverterFactory;
import com.james.common.netcore.networking.http.core.PassThroughExecutor;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lqx
 */
public class RetrofitUtil {
    private static volatile RetrofitUtil instance;
    private static volatile Retrofit retrofit;
    private static volatile Retrofit retrofitOther;
    private static final int DEFAULT_TIMEOUT = 90;
    private static volatile Interceptor interceptor;
    private static volatile OkHttpClient okHttpClient;

    private static String baseUrl = "";//IConstant getSellerUrl()
    private static String baseOtherUrl = "";//IConstant getSellerUrl()

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        RetrofitUtil.baseUrl = baseUrl;
    }

    public static void setOtherBaseUrl(String baseUrl) {
        baseOtherUrl = baseUrl;
    }

    public static String getOtherBaseUrl() {
        return baseOtherUrl;
    }

    public static Interceptor getInterceptor() {
        return interceptor;
    }

    public static void setInterceptor(Interceptor interceptor) {
        RetrofitUtil.interceptor = interceptor;
    }

    public RetrofitUtil() {
        initRetrofit();
        initOtherRetrofitUrl();
    }

    public static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    public static OkHttpClient initOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(interceptor);
        if (AppUtils.isAppDebug()) {
            httpClientBuilder.addInterceptor(logging);
        }
        OkHttpClient client = httpClientBuilder.build();
        return client;
    }

    public static Retrofit initRetrofit(String baseUrl) {
        OkHttpClient client = initOkHttpClient();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(HiConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(new PassThroughExecutor())
                .client(client)
                .build();
        RetrofitUtil.okHttpClient = client;
        return mRetrofit;
    }

    public static Retrofit initRetrofit() {
        retrofit = initRetrofit(baseUrl);
        return retrofit;
    }

    public static Retrofit initOtherRetrofitUrl() {
        retrofitOther = initRetrofit(baseOtherUrl);
        return retrofitOther;
    }

    public static Retrofit initOtherRetrofitUrl(String baseUrl) {
        retrofitOther = initRetrofit(baseUrl);
        return retrofitOther;
    }

    public Retrofit getOtherRetrofit() {
        return retrofitOther;
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
