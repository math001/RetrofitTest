package com.example.math.newsever;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.TaoBao;
import dbcache.DataAccess;
import dbcache.DataChace;
import service.CMSService;
import rx.Observable;
import service.HomDataService;
import subscribers.ProgressSubscriber;
import subscribers.SubscriberOnNextListener;

/**
 * Created by math on 2017/1/18.
 * 获取首页前置机数据
 */

public class HomeRequestor extends ChildenRequestor {
    public HomDataService homDataService;

    public HomeRequestor(Activity context) {
        super(context, HomDataService.BASE_URL);
        homDataService = mRetrofit.create(HomDataService.class);
    }

    @Override
    public boolean isEncryption() {
        return false;
    }

    @Override
    public String getRequestType() {
        return GET;
    }

    @Override
    public boolean isHaveHeader() {
        return false;
    }

    @Override
    public String getWholeUrl() {
        return "http://jkjby.yijia.com";
    }

    @Override
    public boolean isUserRequest() {
        return false;
    }

    @Override
    public DataChace getDataChaceRules() {
        return new DataChace(DataAccess.CACHE_NET_CACHE_NORMAL, 60, null);
    }


    @Override
    public HashMap<String, Object> getOriginalRequestParm() {
        return null;
    }


    /**
     * 获取首页数据
     */
    @Override
    public boolean getData(SubscriberOnNextListener subscriberOnNextListener) {
        if (!super.getData(subscriberOnNextListener)) {
            Observable observable = homDataService.getHomeData("1201778747", "fen_nine_anzhong", "Android", "0")
                    .map(new HttpResultFunc<TaoBao>(this));
            progressSubscriber = new ProgressSubscriber(subscriberOnNextListener, mContext, true);
            toSubscribe(observable, progressSubscriber);
        } else {
            //使用缓存
            TaoBao tobao = getCache();
            subscriberOnNextListener.onNext(tobao);
        }
        return true;

    }

    @Override
    public Class getBeanType() {
        return TaoBao.class;
    }
}
