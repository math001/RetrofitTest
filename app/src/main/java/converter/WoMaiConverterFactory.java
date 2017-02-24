package converter;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bean.HttpResult;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by math on 2017/1/23.
 */

public class WoMaiConverterFactory extends Converter.Factory {
    public boolean isEncryption;
    public Class beanType;
    public WoMaiConverterFactory(boolean isEncryption,Class beanType) {
        this.isEncryption = isEncryption;
        this.beanType=beanType;
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new WoMaiResponseBodyConverter<HttpResult>(beanType,isEncryption);
    }
//    @Override
//    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//
//              return new WoMaiRequestBodyConverter(beanType,isEncryption);
////        } else {
////            return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
////        }
//    }

}
