package com.example.math.newsever;

import android.util.Log;

import java.util.Date;
import HttpUtils.ServiceUtils;
import bean.HttpResult;
import dbcache.CacheManger;
import json.Jackson;
import rx.functions.Func1;

/**
 * Created by math on 2017/1/18.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    private ChildenRequestor childenRequestor;
    public HttpResultFunc(ChildenRequestor childenRequestor) {
        this.childenRequestor = childenRequestor;
    }

    @Override
    public T call(final HttpResult<T> tHttpResult) {
        if (tHttpResult.requestCode.equals(ServiceUtils.RSA_KEY_EXPIRE)) {
            //RSA失效
            new SecurityRequestor(childenRequestor).getData();
            return null;
        } else {
            //// TODO: 2017/1/23 其他requestCode的情况
        }
        //缓存情况
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (childenRequestor.getDataChaceRules() != null) {
                    switch (childenRequestor.getDataChaceRules().dataAccess) {
                        case NET_NORMAL:
                            //  ServiceLog.d("\r\n\r\n\r\n" + url + "->使用最新的网络数据");
                            CacheManger.writeLocal(childenRequestor.mContext, childenRequestor.getWholeUrl(), null,  tHttpResult.data, "0");
                            break;
                        case NET_DELETE:
                            CacheManger.writeLocal(childenRequestor.mContext, childenRequestor.getWholeUrl(), null,  tHttpResult.data, "0");
                            break;
                        case CACHE_NET_CACHE_NORMAL:
                            CacheManger.writeLocal(childenRequestor.mContext, childenRequestor.getWholeUrl(), null, tHttpResult.data, "0");
                            break;
                        case CACHE_NET_CACHE_DELETE:
                            CacheManger.writeLocal(childenRequestor.mContext, childenRequestor.getWholeUrl(), null,  tHttpResult.data, "1");
                            break;
                        default:
                            break;
                    }
                }
            }
        }).run();
        //打日志
        printLog(tHttpResult);
        return tHttpResult.data;
    }

    private void printLog(HttpResult<T> tHttpResult) {
        StringBuffer logBuffer = childenRequestor.getStringBuffer();
        //  logBuffer.append("\n请求开始时间:\n").append(sdf.format(new Date(start)));
        long end = System.currentTimeMillis();
        logBuffer.append("\n请求结束时间:\n").append(ServiceUtils.sdf.format(new Date(end)));
        logBuffer.append("\n请求花费时间(秒):\n").append((end - childenRequestor.startTime) / 1000f);
        logBuffer.append("\n服务器CODE:").append(tHttpResult.requestCode);
        logBuffer.append("\n服务器MESSAGE:").append(tHttpResult.requestMsg);
        String data = Jackson.toJson(tHttpResult.data);
        // 日志
        if (logBuffer != null) {
            logBuffer.append("\n服务器数据:\n").append(data);
        }
        Log.d("math", logBuffer.toString());
    }

}

