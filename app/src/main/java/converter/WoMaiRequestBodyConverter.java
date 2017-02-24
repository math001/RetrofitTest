package converter;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.util.HashMap;

import HttpUtils.HttpUtilss;
import bean.ROConsult;
import json.Jackson;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.Converter;
import security.RSAUtil;
import security.ThreeDES;

/**
 * Created by math on 2017/1/23.
 */

public class WoMaiRequestBodyConverter<T> implements Converter<T, RequestBody> {
    public boolean isEncryption;
    public RequestBody requestBody;
    public static String duichenKey = ThreeDES.genrateRandomPassword(24);
    public HashMap<String, Object> dataParam;
    public Class beanType;

    public WoMaiRequestBodyConverter(Class beanType, boolean isEncryption) {
        this.isEncryption = isEncryption;
        this.beanType = beanType;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        try {
            if (value == null) {
                return null;
            } else {
                dataParam= (HashMap<String, Object>) value;
                if (isEncryption) {
                    String jiamiKEy = null;
                    try {
                        jiamiKEy = RSAUtil.rsaBase64(HttpUtilss.getRSA(), duichenKey);
                        String paramString = Jackson.toJson(value);
                        String data = null;
                        data = ThreeDES.orginalEncoded(duichenKey, paramString);
                        requestBody = new FormBody.Builder()
                                .add("key", jiamiKEy)
                                .add("data", data)
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (ROConsult.class == beanType) {
                    String data = ThreeDES.orginalEncoded(ThreeDES.PUBLIC_KEY, Jackson.toJson(dataParam));
                    requestBody = new FormBody.Builder()
                            .add("data", data)
                            .build();
                } else {
                    String paramString = Jackson.toJson(value);
                    String data = new String(Base64.encodeBase64(paramString.getBytes()), "UTF-8");
                    requestBody = new FormBody.Builder()
                            .add("data", data)
                            .build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
