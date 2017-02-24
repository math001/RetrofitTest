package dbcache;

import android.content.Context;

import java.util.Map;

import HttpUtils.HttpUtilss;
import HttpUtils.ServiceUtils;
import json.Jackson;

/**
 * Created by math on 2017/1/25.
 */

public class CacheManger {
    /**
     * 读取本地数据
     *
     * @param context       上下文
     * @param url           地址
     * @param params        参数
     * @param dataCacheTime 数据缓存时间(分钟),该值小于等于0时表示缓存永远无效
     * @param dataValid     数据是否有效 根据minutes判断缓存是否有效(这个一个输出值)
     * @param type          对象类型
     * @return
     */
    public static <T> T readLocal(Context context, String url, Map<String, Object> params, int dataCacheTime,
                                  DataValid dataValid, Class<T> type) {
        T t = null;
        CacheService service = new CacheService(context);
        try {
            Cache cache = service.getCache(url + Jackson.toJson(HttpUtilss.getCacheHeader()) + Jackson.toJson(params));
            if (cache != null && cache.value != null && cache.value.length() > 0) {
                t = Jackson.toObject(cache.value, type);
                if (t != null && service.valid(cache, dataCacheTime)) {
                    dataValid.isValid = true;
                }
            }
        } finally {
            service.close();
        }
        return t;
    }

    /**
     * 写入本地数据
     *
     * @param context 上下文
     * @param url     地址
     * @param params  参数
     * @param t       数据
     */
    public static <T> void writeLocal(Context context, String url, Map<String, Object> params, T t, String tag) {
        if (t != null) {
            CacheService service = new CacheService(context);
            try {
                service.save(new Cache(url + Jackson.toJson(HttpUtilss.getCacheHeader()) + Jackson.toJson(params),
                        Jackson.toJson(t)), tag);
            } finally {
                service.close();
            }
        }
    }
}
