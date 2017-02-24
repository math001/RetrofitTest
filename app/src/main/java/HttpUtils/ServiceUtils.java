package HttpUtils;

import android.annotation.SuppressLint;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;

import java.text.SimpleDateFormat;
import java.util.List;

import bean.HttpResult;
import bean.TaoBao;
import bean.TaoBaoBean;
import json.Jackson;
import security.ThreeDES;

/**
 * Created by math on 2017/1/19.
 */

public class ServiceUtils {

    /**
     * 服务器返回成功的code
     */
    public static final String SUCCESS = "00";

    /**
     * 服务器RSA失效
     */
    public static final String RSA_KEY_EXPIRE = "-01";
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM-dd HH:mm:ss SSSS");

    /**
     * 解析json串，带64位解码和解密
     * <p>
     * 日志
     *
     * @param json
     * @param keybyte
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static HttpResult
    toROObject(String json, byte[] keybyte, Class type) {
        JsonNode jsonNode = Jackson.toJsonNode(json);
        // 获取code 和 message 的值
        HttpResult resp = new HttpResult();

        JsonNode codeNode = jsonNode.get("code");
        if (codeNode != null) {
            resp.requestCode = codeNode.getTextValue();
            if (resp.requestCode == null) {
                resp.requestCode = "";
            }
        }
        JsonNode messageNode = jsonNode.get("message");
        if (messageNode != null) {
            resp.requestMsg = messageNode.getTextValue();
        }
        // 判断code是否存在
        if ((resp.requestCode == null || resp.requestCode.length() == 0) && (resp.requestMsg == null || resp.requestMsg.length() == 0)) {
            resp.requestCode = "00";
            resp.requestMsg = "SUCCESS";
            //return null;
        }
        // 解析data
        String data = "{}";
        if (SUCCESS.equals(resp.requestCode)) {
            String ss=jsonNode.toString();
            //ss=ss.substring(0,ss.length()-1);
          ss="{\"data\""+":"+ss+"}";
             JsonNode jsonNode1=Jackson.toJsonNode(ss);
            JsonNode dataNode = jsonNode1.get("data");
            if (dataNode != null) {
                data = dataNode.toString();
                byte[] Miwen = data.getBytes();
                //Base64.decodeBase64(data.getBytes());
                if ((keybyte != null)) {
                    Miwen = ThreeDES.decryptMode(keybyte, Miwen);
                }
                data = new String(Miwen);
            }
        }
//        // 日志
//        if (logBuffer != null) {
//            logBuffer.append("\n服务器数据:\n").append(data);
//        }
        // 返回值
        resp.data = Jackson.toObject(data, type);
        return resp;
    }
}
