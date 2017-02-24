package service;

import java.util.List;
import java.util.Map;

import bean.HttpResult;
import bean.ROConsult;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by math on 2017/1/18.
 * 前置机接口
 */

public interface GatewayService  {
    String BASE_URL = "http://wapi.m.womai.com/";

    @FormUrlEncoded
    @POST("consult.action")
    Observable<HttpResult<ROConsult>> getRSA(@FieldMap Map<String, Object> fields);
//    @POST("prompt/searchPrompt.action")
//    Observable<HttpResult<ROSearchPrompt>> getSearch(@FieldMap Map<String, Object> fields);
}
