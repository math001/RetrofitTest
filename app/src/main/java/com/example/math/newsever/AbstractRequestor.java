package com.example.math.newsever;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import HttpUtils.HttpUtilss;
import converter.GetCookiesInterceptor;
import converter.WoMaiConverterFactory;
import json.Jackson;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import security.ThreeDES;


/**
 * Created by math on 2017/1/17.
 */

public abstract class AbstractRequestor {
    public Retrofit mRetrofit;
    public static final int DEFAULT_TIMEOUT = 30;
    public Class beanType;
    public final static String GET = "GET";
    public final static String POST = "POST";
    public Activity mContext;
    /**
     * 服务器RSA失效
     */
    // public static final String RSA_KEY_EXPIRE = "-01";
    public AbstractRequestor(String BASE_URL) {
        //创建Builder，加入header
        beanType = getBeanType();
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(new WoMaiConverterFactory(isEncryption(), beanType))
                .client(createBuilder().build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }
    protected OkHttpClient.Builder createBuilder() {
        Map<String, String> header = null;
        String headerString = Jackson.toJson(HttpUtilss.getHeader());
        try {//头加密
            headerString = ThreeDES.orginalEncoded(ThreeDES.PUBLIC_KEY, headerString);
            header = new HashMap<String, String>();
            header.put("headerData", headerString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Map<String, String> headerFinal = header;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                //设置header
                Request.Builder reBuilder = null;
                // 第一次一般是还未被赋值，若有值则将SessionId发给服务器
                 reBuilder = setHttpHeaders(original, headerFinal);
                if (null != HttpUtilss.getJsessionId()) {
                      reBuilder.addHeader("Cookie", "JSESSIONID=" + HttpUtilss.getJsessionId());
                }
                Request request = reBuilder.build();
                return chain.proceed(request);
            }
        });
      //  builder.addInterceptor(new GetCookiesInterceptor(mContext));
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return builder;
    }

    /**
     * 设置beanType
     */
    public abstract Class getBeanType();

    /**
     * 是否加密
     */
    public abstract boolean isEncryption();
    /**
     *请求方式
     */
    public abstract String  getRequestType();
    /**
     * 是否传header
     */
    public abstract  boolean isHaveHeader();
    /**
     * 获取请求参数(原始)
     */
    public abstract HashMap<String, Object> getOriginalRequestParm();
    /**
     * 设置header
     *
     * @param original
     * @param headers
     */
    public Request.Builder setHttpHeaders(Request original, Map<String, String> headers) {
        Iterator<String> iterator=null;
        if(headers!=null){
            iterator = headers.keySet().iterator();
        }
        Request.Builder build = original.newBuilder();
        String headerName = "";
        String headerValue = "";
        if(!isHaveHeader()){
            if(getRequestType().equals(GET)){
               // build.addHeader("Cache-Control", "no-cache");
            }
            return build;
        }
        if(headers!=null){
            while (iterator.hasNext()) {
                headerName = iterator.next();
                headerName = headerName.trim();
                headerValue = headers.get(headerName);
                build.addHeader(headerName, headerValue);
            }
        }
        if(getRequestType().equals(GET)){
            build.addHeader("Cache-Control", "no-cache");
        }
        return build;
    }
}
