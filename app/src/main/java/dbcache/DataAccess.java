package dbcache;

/**
 * Created by math on 2017/1/23.
 */

public enum  DataAccess {
    /**
     * 请求网络数据，并将数据存入缓存，缓存数据记录不会被删除
     */
    NET_NORMAL,

    /**
     * 请求网络数据，并将数据存入缓存，缓存记录数过大时会被删除
     */
    NET_DELETE,

    /**
     * 缓存-网络-缓存：缓存失效时使用网络，网络失败时再次使用缓存，缓存数据记录不会被删除
     */
    CACHE_NET_CACHE_NORMAL,

    /**
     * 缓存-网络-缓存-canclear：缓存失效时使用网络，网络失败时再次使用缓存, 缓存记录数过大时会被删除
     */
    CACHE_NET_CACHE_DELETE
}
