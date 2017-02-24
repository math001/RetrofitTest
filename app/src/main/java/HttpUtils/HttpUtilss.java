package HttpUtils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by math on 2017/1/19.
 */

public class HttpUtilss {
    public static Map<String, String> getHeader() {
        Map<String, String> header = new ConcurrentHashMap<String, String>();
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));// 间戳(新增)yyyymmddSS
        return header;
    }
    /**
     * 获取缓存的header
     *
     */
    @SuppressLint("SimpleDateFormat")
    public static Map<String, String> getCacheHeader() {
        Map<String, String> header = new ConcurrentHashMap<String, String>();
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        header.put("header_item", "");
        return header;
    }

    /**
     * 获取用户相关接口的请求对象
     *
     * @param data
     * @return
     */
    public static Map<String, Object> getUserRequestMap(Map<String, Object> data) {
        Map<String, Object> DATA = new HashMap<String, Object>();
        if (data != null) {
            DATA.put("data", data);
        }
        DATA.put("common", HttpUtilss.getUserCommon());
        return DATA;
    }

    /**
     * 获取非用户相关接口的请求对象
     *
     * @param data
     * @return
     */
    public static Map<String, Object> getNoUserRequestMap(Map<String, Object> data) {
        Map<String, Object> DATA = new HashMap<String, Object>();
        if (data != null) {
            DATA.put("data", data);
        }
        DATA.put("common", HttpUtilss.getNoUserCommon());
        return DATA;
    }
/**
 *
 {"common":{"userId":"","test1":"","userSession":"","mid":"0","cityCode":"31000"}}
 */
    /**
     * 获取用户相关接口的commom对象
     *
     * @return
     */
    private static Map<String, String> getUserCommon() {
        Map<String, String> common = new ConcurrentHashMap<String, String>();
        common.put("common", "0");
        common.put("common", "");
        common.put("common", "");
        common.put("common", "");
        common.put("common", "");
        return common;
    }

    /**
     * 获取用户非相关接口commom对象
     *
     * @return
     */
    private static Map<String, String> getNoUserCommon() {
        Map<String, String> common = new ConcurrentHashMap<String, String>();
        common.put("common", "0");
        common.put("common", "");
        common.put("common", "");
        common.put("common", "");
        common.put("common", "");
        return common;
    }

    public  static String getJsessionId() {
        return JsessionId;
    }
    public static void  setJessionId(String lsessionId){
        JsessionId=lsessionId;
    }

    public static void setRSA(String rsa) {
        RSA=rsa;
    }

    public static String getRSA() {
        return RSA;
    }
    public static String RSA="";
    public static String JsessionId="";
}
