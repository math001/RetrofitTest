package service;


import bean.HttpResult;
import bean.TaoBao;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by math on 2017/1/18.
 */

public interface CMSService {
    String BASE_URL = "http://capi.m.womai.com/";
    @GET("index/App_3.3.0_0_index.html")
    Observable<HttpResult<TaoBao>> getHomeData(@Query("cityId") String cityid, @Query("rc") String rc, @Query("keyid") String keyid);

}
