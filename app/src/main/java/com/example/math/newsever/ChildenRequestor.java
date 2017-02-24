package com.example.math.newsever;

import android.app.Activity;
import android.content.Context;
import android.os.Message;

import org.apache.commons.codec.binary.Base64;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dbcache.CacheManger;
import dbcache.DataChace;
import dbcache.DataValid;
import HttpUtils.HttpUtilss;
import bean.ROConsult;
import json.Jackson;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import security.RSAUtil;
import security.ThreeDES;
import subscribers.ProgressSubscriber;
import subscribers.SubscriberOnNextListener;

import static HttpUtils.ServiceUtils.sdf;

/**
 * Created by math on 2017/1/17.
 */

public abstract class ChildenRequestor extends AbstractRequestor {
    public ProgressSubscriber progressSubscriber;
    public Map<String, Object> dataParam;
    //处理RSA请求
    public SubscriberOnNextListener mySubscriberOnNextListener;
    private Object myCache;
    private StringBuffer logBuffer;
    public Long startTime;
    /**
     * 是否读出缓存
     */
    private  boolean isReadCache;
    public ChildenRequestor(Activity context, String BASE_URL) {
        super(BASE_URL);
        mContext = context;
    }

    /**
     * 获取完整URL路径
     */
    public abstract String getWholeUrl();

    /**
     * 是否用户相关
     */
    public abstract boolean isUserRequest();

    /**
     * 是否有缓存
     */
    public abstract DataChace getDataChaceRules();
    /**
     * 获取日志StringBuffer
     */
    public StringBuffer getStringBuffer() {
        if (logBuffer == null) {
            logBuffer = new StringBuffer("\r\n\r\n\r\n--------------------------------------------------->");
        }
        return logBuffer;
    }

    /**
     * 设置请求开始时间
     *
     * @param start
     */
    public void setRequestStartTime(long start) {
        startTime = start;
    }

    /**
     * 获取缓存
     */
    public <T> T getCache() {
        return (T) myCache;
    }

    /**
     * 加密
     */
    public void encryptHeadAndBody(HashMap<String, Object> params) {
        String duichenKey = null;//自己生成，或者指定一个
        if (isUserRequest()) {
            dataParam = HttpUtilss.getUserRequestMap(params);
        } else {
            dataParam = HttpUtilss.getNoUserRequestMap(params);
        }
        try {
            if (params == null) {
                return;
            } else {
                if (isEncryption()) {
                    String jiamiKEy = null;
                    try {
                        jiamiKEy = RSAUtil.rsaBase64(HttpUtilss.getRSA(), duichenKey);
                        String paramString = Jackson.toJson(dataParam);
                        String data = null;
                        data = ThreeDES.orginalEncoded(duichenKey, paramString);
                        dataParam = new HashMap<String, Object>();
                        dataParam.put("key", jiamiKEy);
                        dataParam.put("data", data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (ROConsult.class == getBeanType()) {
                    String data = ThreeDES.orginalEncoded(ThreeDES.PUBLIC_KEY, Jackson.toJson(dataParam));
                    dataParam = new HashMap<String, Object>();
                    dataParam.put("data", data);
                } else {
                    String paramString = Jackson.toJson(dataParam);
                    String data = new String(Base64.encodeBase64(paramString.getBytes()), "UTF-8");
                    dataParam = new HashMap<String, Object>();
                    dataParam.put("data", data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 获取请求参数（处理后）
     *
     * @return
     */
    public Map<String, Object> getDealedRequestParm() {
        return dataParam;
    }

    public boolean getData(SubscriberOnNextListener subscriberOnNextListener) {
        mySubscriberOnNextListener = subscriberOnNextListener;
        encryptHeadAndBody(getOriginalRequestParm());
        //// TODO: 2017/1/23 是否有缓存
        final DataChace dataChace=getDataChaceRules();
        if (dataChace != null) {
            //使用缓存规则
          new Thread(new Runnable() {
              @Override
              public void run() {
                  Object cache = cachingRules(dataChace);
                  myCache=cache;
                  isReadCache=true;
              }
          }).run();
            while (true){
                if(isReadCache){
                    if(myCache != null){
                        return true;
                    }else{
                        // 设置请求日志
                        setRequestLog();
                        return false;
                    }
                }
            }
        } else {
            //设置请求日志
            setRequestLog();
            return false;
        }

    }

    /**
     * 设置请求日志
     */
    private void setRequestLog() {
        long start = System.currentTimeMillis();
        getStringBuffer().append("\n请求开始时间:\n").append(sdf.format(new Date(start)));
        setRequestStartTime(start);
        logBuffer.append("\n请求地址:").append(getWholeUrl());
        String headerString = Jackson.toJson(HttpUtilss.getHeader());
        logBuffer.append("\n请求头信息:\n").append(headerString);
        logBuffer.append("\n请求参数:\n").append(getOriginalRequestParm());

    }

    protected <T> T cachingRules(DataChace dataChace) {
        DataValid dataValid = dataChace.dataValid;
        T t_cache = null;
        switch (dataChace.dataAccess) {
            case NET_NORMAL:
                //  ServiceLog.d("\r\n\r\n\r\n" + url + "->使用最新的网络数据");
                return null;
            case NET_DELETE:
//                 t = requestUrl(url, type);
//                 writeLocal(context, url, null, t, "1");
                //ServiceLog.d("\r\n\r\n\r\n" + url + "->使用最新的网络数据");
                return null;
            case CACHE_NET_CACHE_NORMAL:
                if (dataValid == null) {
                    dataValid = new DataValid(false);
                }
                t_cache = (T) CacheManger.readLocal(mContext, getWholeUrl(), null, dataChace.cacheTime, dataValid, getBeanType());
                if (t_cache != null && dataValid.isValid) {
                    return t_cache;
                } else {
                    //缓存无效，请求最新数据
                    return t_cache;
                }

            case CACHE_NET_CACHE_DELETE:
                if (dataValid == null) {
                    dataValid = new DataValid(false);
                }
                t_cache = (T) CacheManger.readLocal(mContext, getWholeUrl(), null, dataChace.cacheTime, dataValid, getBeanType());
                if (t_cache != null && dataValid.isValid) {
                    return t_cache;
                } else {
                    //缓存无效
                    return null;
                }

            default:
              break;
        }
       return t_cache;
    }


    public <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        //为Subscriber设置缓存规则
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


}
