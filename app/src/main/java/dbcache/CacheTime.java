package dbcache;

/**
 * Created by math on 2017/1/23.
 */

public class CacheTime {
    // ---------------------------------------不会被删除的缓存----------------------------
    public static final int getHome = 1; // 首页

    public static final int noticeList = 1; // 公告列表

    public static final int getBrand = 60; // 品牌列表

    public static final int getCategories = 60; // 分类列表

    public static final int getHotWord = 60; // 搜索热词

    public static final int getMidCity = 60; // 站点选择

    public static final int helpCenter = 60; // 帮助中心

    public static final int getCityList = 60 * 24;// 城市选择列表

    public static final int getCityListFirst = 60;// 第一次城市选择列表
    public static final int getSettingUserInfo = 10;// 设置页面用户信息

    public static final int netWorkPrior = 0;// 请求网络数据，网络不通再使用缓存

    // ---------------------------------------可被删除的缓存----------------------------

    public static final int getActivity = 1; // 专题接口

    public static final int getActiProducts = 1; // 活动商品列表(带子标题)

    public static final int getProductsFromActivity = 1; // 活动商品列表(不带子标题)

    public static final int getBrandProducts = 1; // 品牌筛选

    public static final int getCategorieProducts = 1;// 分类筛选

    public static final int getSearchProducts = 1; // 搜索

    public static final int getIdsProducts = 1; // ids

    public static final int getProductDetail = 1; // 商品详情

    public static final int getProductComments = 1; // 商品详情评论

    public static final int scancode = 1;// 扫描根据条码获得商品ID
}
