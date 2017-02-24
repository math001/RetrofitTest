package com.example.math.newsever;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.List;
import HttpUtils.HttpUtilss;
import dbcache.DataChace;
import rx.Observable;
import service.GatewayService;
import bean.ROConsult;
import subscribers.ProgressSubscriber;
import subscribers.SubscriberOnNextListener;

/**
 * Created by math on 2017/1/19.
 */

public class SecurityRequestor extends ChildenRequestor {
    private SubscriberOnNextListener subscriberOnNextListener;
    private GatewayService myGatewayService;
    public SecurityRequestor(Activity context) {
        super(context, GatewayService.BASE_URL);
        myGatewayService = mRetrofit.create(GatewayService.class);
    }

    public SecurityRequestor(final ChildenRequestor childenRequestor) {
        super(childenRequestor.mContext, GatewayService.BASE_URL);
        myGatewayService = mRetrofit.create(GatewayService.class);
        subscriberOnNextListener = new SubscriberOnNextListener<List<ROConsult>>() {
            @Override
            public void onNext(List<ROConsult> object) {

                HttpUtilss.setRSA(object.get(0).consult.public_key);
                //请求之前一个接口的数据
                childenRequestor.getData(childenRequestor.mySubscriberOnNextListener);
            }
        };
    }


    @Override
    public boolean isEncryption() {
        return false;
    }

    @Override
    public String getRequestType() {
        return POST;
    }

    @Override
    public boolean isHaveHeader() {
        return true;
    }

    @Override
    public String getWholeUrl() {
        return null;
    }

    @Override
    public boolean isUserRequest() {
        return false;
    }

    @Override
    public DataChace getDataChaceRules() {
        return null;
    }

    @Override
    public HashMap<String, Object> getOriginalRequestParm() {
        String src = "abc";
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("consult", src);
        return data;
    }

    @Override
    public boolean getData(SubscriberOnNextListener subscriberOnNextListener) {
        super.getData(subscriberOnNextListener);
        Observable observable =   myGatewayService.getRSA(getDealedRequestParm()).map(new HttpResultFunc<ROConsult>(this));
        progressSubscriber = new ProgressSubscriber(subscriberOnNextListener, mContext,true);
        toSubscribe(observable, progressSubscriber);
        return true;
    }

    public void getData() {
        this.getData(subscriberOnNextListener);
        myGatewayService.getRSA(getDealedRequestParm());
    }

    @Override
    public Class getBeanType() {
        return ROConsult.class;
    }
}
