package converter;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import HttpUtils.ServiceUtils;
import bean.HttpResult;
import bean.ROConsult;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import security.ThreeDES;

/**
 * Created by math on 2017/1/23.
 */

public class WoMaiResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    public Class beanType;
    public static final String UTF8 = "utf-8";
    public boolean isEncryption;
    /**
     * 换行符
     */
    private static final String CRLF = System.getProperty("line.separator");
    /**
     * 响应编码
     */
    private String responseEncoding = UTF8;
    public WoMaiResponseBodyConverter(Class bean, boolean isEncryption) {
        this.beanType = bean;
        this.isEncryption = isEncryption;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response;
        //= value.string();
        // 输入流
        InputStream instream = value.byteStream();
        BufferedInputStream bufferInputStream = new BufferedInputStream(instream);
         //判断是否是gzip格式，Gzip 流 的前两个字节是 0x1f8b
        bufferInputStream.mark(2);
         byte[] headBytes = new byte[2];
            int result = bufferInputStream.read(headBytes);

        int headBytesData = getShort(headBytes);
         bufferInputStream.reset();
        if (result != -1 && headBytesData == 0x1f8b) {
            instream = new GZIPInputStream(bufferInputStream);
        } else {
            instream = bufferInputStream;
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(instream, responseEncoding));
        response = getString(rd);
        rd.close();
        instream.close();
        HttpResult httpResult;
        if (isEncryption) {
            httpResult = ServiceUtils.toROObject(response, WoMaiRequestBodyConverter.duichenKey.getBytes(), beanType);
        } else if(ROConsult.class==beanType){
            httpResult = ServiceUtils.toROObject(response, ThreeDES.PUBLIC_KEY.getBytes(), beanType);
        }else {
            httpResult = ServiceUtils.toROObject(response, null, beanType);
        }
        return (T) httpResult;
    }

    private int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    /**
     * 读取字符串
     *
     * @param rd
     * @return
     * @throws IOException
     */
    private static String getString(BufferedReader rd) throws IOException {
        String tempLine = rd.readLine();
        StringBuffer tempStr = new StringBuffer();
        while (tempLine != null) {
            tempStr.append(tempLine);
            tempStr.append(CRLF);
            tempLine = rd.readLine();
        }
        return tempStr.toString();
    }
}
