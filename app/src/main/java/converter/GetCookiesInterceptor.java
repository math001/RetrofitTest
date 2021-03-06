package converter;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import HttpUtils.HttpUtilss;
import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by math on 2017/2/3.
 * Cookies拦截器
 */

public class GetCookiesInterceptor implements Interceptor {
    private Context context;

    public GetCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            if (cookie.startsWith("JSESSIONID=")) {
                                HttpUtilss.setJessionId(cookie.replace("", "JSESSIONID="));
                            }
                        }
                    });
//            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("cookie", cookieBuffer.toString());
//            editor.commit();
        }
        return originalResponse;
    }

}
