package service;

import bean.HttpResult;
import bean.TaoBao;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by math on 2017/2/24.
 */

public interface HomDataService {
  //  http://jkjby.yijia.com/jkjby/view/list_api.php?app_id=1201778747&sche=fen_nine_anzhong&app_channel=Android&cid=0
    String BASE_URL = "http://jkjby.yijia.com/";
    @GET("jkjby/view/list_api.php")
    Observable<HttpResult<TaoBao>> getHomeData(@Query("app_id") String app_id, @Query("sche") String sche, @Query("app_channel") String app_channel,@Query("cid") String cid);
}
