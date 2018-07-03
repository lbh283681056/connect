package com.base.connect.http;


import android.text.TextUtils;
import android.widget.Toast;


import com.base.code.application.BaseApplication;
import com.base.connect.http.bean.BaseResultWrapper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络访问基类
 */
public class ApiServiceHelp {

    public static int load_success_code = 1;
    // 网络访问超时时间
//连接时长，单位：毫秒
    public static final int CONNECT_TIME_OUT = 10;
    //读超时长，单位：毫秒
    public static final int READ_TIME_OUT = 10;

    public static void setLoadSuccessCode(int loadSuccessCode){
        load_success_code = loadSuccessCode;
    }
    /**
     * 未设置为设置超时json解析
     *
     * 属性 baseUrl
     * @return
     */
    //基础的设置
    public static Retrofit.Builder retrofitBuilder(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    /**
     * 默认的json解析与超时
     *
     * 属性 baseUrl
     * @return
     */
    public static Retrofit getUrlRetrofit(String baseUrl) {
        return getUrlRetrofit(baseUrl, CONNECT_TIME_OUT);
    }

    /**
     * 默认的json解析
     *
     * 属性 baseUrl
     * @return
     */
    public static Retrofit getUrlRetrofit(String baseUrl, long timeout) {
        if (timeout == 0) {
            timeout = CONNECT_TIME_OUT;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .build();
        return getUrlRetrofit(baseUrl, client);
    }

    /**
     * 默认的json解析
     *
     * 属性 baseUrl 基本的url
     * 属性 client  okhttp 对象
     * @return
     */
    public static Retrofit getUrlRetrofit(String baseUrl, OkHttpClient client) {
        return retrofitBuilder(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 默认的json解析
     *
     * 属性 baseUrl 基本的url
     * 属性 client  okhttp 对象
     * @return
     */
    public static Retrofit getUrlRetrofit(String baseUrl, OkHttpClient client, Converter.Factory factory) {
        return retrofitBuilder(baseUrl)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }


    /**
     * 验证数据
     *
     * @return
     */
    public static ObservableTransformer applySchedulers() {
        return schedulersTransformer;
    }

    /**
     * 不验证数据
     *
     * @return
     */
    public static ObservableTransformer applySchedulersNoVerify() {
        return schedulersTransformerNoVerify;
    }

    /**
     * 不验证数据且不重复
     *
     * @return
     */
    public static ObservableTransformer applySchedulersNoVerifyNoRetry() {
        return schedulersTransformerNoVerifyNoRetry;
    }


    /**
     * 这个说白了就是封装一些统rx操作一的
     */
    private static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .retry(3)
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                         .map(new ApiResultVerifyFactory.ApiResultVerify())
                    .flatMap(new Function() {
                        @Override
                        public Object apply(Object o) throws Exception {
                            BaseResultWrapper baseResultWrapper = (BaseResultWrapper) o;
                            if (baseResultWrapper == null || baseResultWrapper.code == null) {
                                throw new ApiExection(ApiExection.ERROR_CODE);
                            }
                            if (baseResultWrapper.code != load_success_code) {
                                if (!TextUtils.isEmpty(baseResultWrapper.msg)) {
                                    Toast.makeText(BaseApplication.getInstance(), baseResultWrapper.msg, Toast.LENGTH_LONG).show();
                                }
                                return Observable.error(new ApiThrowable(baseResultWrapper.code));
                            }
                            return Observable.just(baseResultWrapper);
                        }
                    });
        }


    };


    //这个说白了就是封装一些统rx操作一的
    private static final ObservableTransformer schedulersTransformerNoVerifyNoRetry = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {

            return upstream
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };


    //这个说白了就是封装一些统rx操作一的
    private static final ObservableTransformer schedulersTransformerNoVerify = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .retry(3)
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
}

